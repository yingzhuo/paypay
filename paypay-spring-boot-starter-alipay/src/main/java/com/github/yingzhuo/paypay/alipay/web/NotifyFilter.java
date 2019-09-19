/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.yingzhuo.paypay.alipay.AlipayNotifyCallback;
import com.github.yingzhuo.paypay.alipay.autoconfig.AlipayConfigProps;
import com.github.yingzhuo.paypay.alipay.model.NotifyParams;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 白宝鹏
 * @author 应卓
 */
@Slf4j
@SuppressWarnings("Duplicates")
public class NotifyFilter extends OncePerRequestFilter {

    private final AlipayConfigProps props;
    private final AlipayNotifyCallback alipayNotifyCallback;

    public NotifyFilter(AlipayConfigProps props, AlipayNotifyCallback alipayNotifyCallback) {
        this.props = props;
        this.alipayNotifyCallback = alipayNotifyCallback;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        doLog(request);

        // 解析阿里回调参数
        val notifyParams = NotifyParams.parse(request);

        try {
            boolean isValidSign = AlipaySignature.rsaCheckV1(
                    getValidationMap(request),
                    props.getPublicKey(),
                    notifyParams.getCharset(),
                    notifyParams.getSignType());

            // 签名不正确
            if (!isValidSign) {
                alipayNotifyCallback.onInvalidSign(request, response, notifyParams);
                return;
            }
        } catch (AlipayApiException e) {
            alipayNotifyCallback.onAlipayApiException(request, response, notifyParams, e);
        }

        if (StringUtils.equalsIgnoreCase("TRADE_SUCCESS", notifyParams.getTradeStatus())) {
            alipayNotifyCallback.onTradeSuccess(request, response, notifyParams);
        } else {
            alipayNotifyCallback.onTradeFailure(request, response, notifyParams);
        }
    }

    private Map<String, String> getValidationMap(HttpServletRequest request) {
        final Map<String, String> validationMap = new HashMap<>();
        final Map<String, String[]> requestParams = request.getParameterMap();

        for (String key : requestParams.keySet()) {
            String[] values = requestParams.get(key);
            String valueStr = StringUtils.join(values);
            validationMap.put(key, valueStr);
        }
        return validationMap;
    }

    private String decode(String path) {
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }

    private void doLog(HttpServletRequest request) {
        try {
            log.debug(StringUtils.repeat('-', 120));

            log.debug("[Path]: ");
            log.debug("\t\t\t{}", decode(request.getRequestURI()));

            log.debug("[Method]: ");
            log.debug("\t\t\t{}", request.getMethod());

            log.debug("[Headers]: ");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                log.debug("\t\t\t{} = {}", name, name.equalsIgnoreCase("cookie") ? StringUtils.abbreviate(value, 60) : value);
            }

            log.debug("[Params]: ");
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String name = paramNames.nextElement();
                String value = request.getParameter(name);
                log.debug("\t\t\t{} = {}", name, value);
            }

            log.debug(StringUtils.repeat('-', 120));
        } catch (Exception ignore) {
        }
    }

}
