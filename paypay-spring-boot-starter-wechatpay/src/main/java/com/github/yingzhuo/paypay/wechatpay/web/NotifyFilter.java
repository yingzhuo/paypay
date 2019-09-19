/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechatpay.web;

import com.github.yingzhuo.paypay.wechatpay.WechatpayNotifyCallback;
import com.github.yingzhuo.paypay.wechatpay.autoconfig.WechatpayConfigProps;
import com.github.yingzhuo.paypay.wechatpay.util.DocumentUtils;
import com.github.yingzhuo.paypay.wechatpay.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    private final WechatpayConfigProps props;
    private final WechatpayNotifyCallback callback;

    public NotifyFilter(WechatpayConfigProps props, WechatpayNotifyCallback callback) {
        this.props = props;
        this.callback = callback;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        doLog(request);

        final String requestXml = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);

        if (StringUtils.isBlank(requestXml)) {
            callback.onEmptyRequestBody(response);
        }

        Map<String, String> requestObjMap = DocumentUtils.xmlToMap(requestXml);
        log.trace("request body xml :\n{}", requestXml);

        try {

            if (StringUtils.equalsIgnoreCase("SUCCESS", requestObjMap.get("return_code"))) {
                if (StringUtils.equalsIgnoreCase("SUCCESS", requestObjMap.get("result_code"))) {
                    Map<String, String> payparms = new HashMap<>();
                    payparms.put("result_code", requestObjMap.get("result_code"));
                    payparms.put("appid", requestObjMap.get("appid"));
                    payparms.put("mch_id", requestObjMap.get("mch_id"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("device_info")))
                        payparms.put("device_info", requestObjMap.get("device_info"));
                    payparms.put("nonce_str", requestObjMap.get("nonce_str"));
                    payparms.put("return_code", requestObjMap.get("return_code"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("err_code")))
                        payparms.put("err_code", requestObjMap.get("err_code"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("err_code_des")))
                        payparms.put("err_code_des", requestObjMap.get("err_code_des"));
                    payparms.put("openid", requestObjMap.get("openid"));
                    payparms.put("is_subscribe", requestObjMap.get("is_subscribe"));
                    payparms.put("trade_type", requestObjMap.get("trade_type"));
                    payparms.put("bank_type", requestObjMap.get("bank_type"));
                    payparms.put("total_fee", requestObjMap.get("total_fee"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("fee_type")))
                        payparms.put("fee_type", requestObjMap.get("fee_type"));
                    payparms.put("cash_fee", requestObjMap.get("cash_fee"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("cash_fee_type")))
                        payparms.put("cash_fee_type", requestObjMap.get("cash_fee_type"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_fee")))
                        payparms.put("coupon_fee", requestObjMap.get("coupon_fee"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_count")))
                        payparms.put("coupon_count", requestObjMap.get("coupon_count"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_id_1")))
                        payparms.put("coupon_id_1", requestObjMap.get("coupon_id_1"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_fee_1")))
                        payparms.put("coupon_fee_1", requestObjMap.get("coupon_fee_1"));
                    payparms.put("transaction_id", requestObjMap.get("transaction_id"));
                    payparms.put("out_trade_no", requestObjMap.get("out_trade_no"));
                    if (StringUtils.isNoneBlank(requestObjMap.get("attach")))
                        payparms.put("attach", requestObjMap.get("attach"));
                    payparms.put("time_end", requestObjMap.get("time_end"));
                    log.info("结果字典是:{} 封装字典是:{}", requestObjMap, payparms);

                    String signLocal = SignUtils.createSign(payparms, props.getSecretKey());

                    log.info("输出结果是: 支付结果通知 sign:{},signLocal:{}", requestObjMap.get("sign"), signLocal);
                    boolean isValidSign = StringUtils.equalsIgnoreCase(requestObjMap.get("sign"), signLocal);

                    if (isValidSign) {
                        callback.onTradeSuccess(request, response, requestObjMap);
                    } else {
                        callback.onInvalidSign(request, response, requestObjMap);
                    }
                } else {
                    callback.onTradeFailure(request, response, requestObjMap);
                }
            } else {
                callback.onTradeFailure(request, response, requestObjMap);
            }
        } catch (Exception e) {
            callback.onException(request, response, requestObjMap, e);
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

    private String decode(String path) {
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }
}
