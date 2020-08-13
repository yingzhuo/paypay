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

import com.github.yingzhuo.paypay.alipay.NotifyCallback;
import com.github.yingzhuo.paypay.alipay.model.NotifyParams;
import lombok.val;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public class NotifyFilter extends OncePerRequestFilter {

    private final NotifyCallback notifyCallback;

    public NotifyFilter(NotifyCallback notifyCallback) {
        this.notifyCallback = notifyCallback;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        // 解析阿里回调参数
        val notifyParams = NotifyParams.parse(request);

//        try {
//            boolean isValidSign = AlipaySignature.rsaCheckV1(
//                    getValidationMap(request),
//                    props.getPublicKey(),
//                    notifyParams.getCharset(),
//                    notifyParams.getSignType());
//
//            if (!isValidSign) {
//                notifyCallback.onInvalidSign(request, response, notifyParams);
//                return;
//            }
//        } catch (AlipayApiException e) {
//            notifyCallback.onAlipayApiException(request, response, notifyParams, e);
//        }

        if ("TRADE_SUCCESS".equals(notifyParams.getTradeStatus())) {
            notifyCallback.onTradeSuccess(request, response, notifyParams);
        } else {
            notifyCallback.onTradeFailure(request, response, notifyParams);
        }
    }

//    private Map<String, String> getValidationMap(HttpServletRequest request) {
//        final Map<String, String> validationMap = new HashMap<>();
//        final Map<String, String[]> requestParams = request.getParameterMap();
//
//        for (String key : requestParams.keySet()) {
//            String[] values = requestParams.get(key);
//            String valueStr = StringUtils.join(values);
//            validationMap.put(key, valueStr);
//        }
//        return validationMap;
//    }

    private String decode(String path) {
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }

}
