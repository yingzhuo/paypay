/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.notify;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.yingzhuo.paypay.ali.model.NotifyParams;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 应卓
 * @since 1.2.0
 */
public final class NotifyFilter extends OncePerRequestFilter {

    private final NotifyCallback notifyCallback;

    public NotifyFilter(NotifyCallback notifyCallback) {
        this.notifyCallback = Objects.requireNonNull(notifyCallback);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        // 解析阿里回调参数
        val notifyParams = NotifyParams.parseFromRequest(request);

        val manager = notifyCallback.getConfigGroupManager();
        val publicKey = notifyCallback.getPublicKey(manager, notifyParams);

        try {
            boolean isValidSign = AlipaySignature.rsaCheckV1(
                    getValidationMap(request),
                    publicKey,
                    notifyParams.getCharset(),
                    notifyParams.getSignType());

            // 签名不正确
            if (!isValidSign) {
                notifyCallback.onInvalidSign(request, response, notifyParams);
                return;
            }
        } catch (AlipayApiException e) {
            notifyCallback.onAlipayApiException(request, response, notifyParams, e);
            return;
        }

        if ("TRADE_SUCCESS".equalsIgnoreCase(notifyParams.getTradeStatus())) {
            notifyCallback.onTradeSuccess(request, response, notifyParams);
        } else {
            notifyCallback.onTradeFailure(request, response, notifyParams);
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

}
