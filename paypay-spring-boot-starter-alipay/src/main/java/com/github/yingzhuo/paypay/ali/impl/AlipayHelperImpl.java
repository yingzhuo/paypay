/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.yingzhuo.paypay.ali.AlipayConfig;
import com.github.yingzhuo.paypay.ali.AlipayHelper;
import com.github.yingzhuo.paypay.ali.exception.AlipayBusinessException;
import com.github.yingzhuo.paypay.ali.exception.AlipayClientException;
import com.github.yingzhuo.paypay.ali.model.AlipayPrepayment;
import com.github.yingzhuo.paypay.common.AmountTransformer;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * @author 应卓
 * @author 白宝鹏
 */
public class AlipayHelperImpl implements AlipayHelper {

    private final static String URL = "https://openapi.alipay.com/gateway.do";
    private final static String PACKAGE_FORMAT = "json";
    private final static String CHARSET = "UTF-8";
    private final static String SIGN_TYPE = "RSA2";
    private final static String PRODUCT_CODE = "QUICK_MSECURITY_PAY";
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("######0.00");

    private AmountTransformer amountTransformer;

    public void setAmountTransformer(AmountTransformer amountTransformer) {
        this.amountTransformer = amountTransformer;
    }

    @Override
    public AlipayPrepayment prepay(AlipayConfig config, String tradeId, long amountInCent, String subject, String passbackParams, String timeoutExpress) {
        // 检查支付所需的参数是否正确
        validateConfig(config);
        validateTradeId(tradeId);
        validateAmount(amountInCent);

        if (amountTransformer != null) {
            amountInCent = amountTransformer.transform(amountInCent);
        }

        val aliClient = new DefaultAlipayClient(
                URL,
                config.getAppId(),
                config.getAppPrivateKey(),
                PACKAGE_FORMAT,
                CHARSET,
                config.getAlipayPublicKey(),
                SIGN_TYPE);

        val request = new AlipayTradeAppPayRequest();
        val model = new AlipayTradeAppPayModel();
        model.setProductCode(PRODUCT_CODE);
        model.setPassbackParams(encode(passbackParams));
        model.setSubject(encode(subject));
        model.setOutTradeNo(encode(tradeId));
        model.setTimeoutExpress(encode(timeoutExpress));
        model.setTotalAmount(encode(parseAmount(amountInCent)));
        request.setBizModel(model);
        request.setNotifyUrl(encode(config.getCallbackNotifyUrl()));

        try {
            AlipayTradeAppPayResponse response = aliClient.sdkExecute(request);
            if (!response.isSuccess()) {
                throw new AlipayBusinessException(response.getMsg(), response.getSubMsg());
            }
            return new AlipayPrepayment(tradeId, response.getBody());
        } catch (AlipayApiException e) {
            throw new AlipayClientException(e);
        }
    }

    @Override
    public String status(AlipayConfig config, String tradeId) {
        validateConfig(config);
        validateTradeId(tradeId);

        val request = new AlipayTradeQueryRequest();

        val aliClient = new DefaultAlipayClient(
                URL,
                config.getAppId(),
                config.getAppPrivateKey(),
                PACKAGE_FORMAT,
                CHARSET,
                config.getAlipayPublicKey(),
                SIGN_TYPE);

        val model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(tradeId);

        request.setBizModel(model);

        try {
            AlipayTradeQueryResponse response = aliClient.execute(request);

            if (!response.isSuccess()) {
                if (StringUtils.equalsIgnoreCase(response.getSubCode(), "ACQ.TRADE_NOT_EXIST")) {
                    return "TRADE_NOT_EXIST";
                } else {
                    throw new AlipayBusinessException(response.getMsg(), response.getSubMsg());
                }
            }

            // WAIT_BUYER_PAY (交易创建，等待买家付款)
            // TRADE_CLOSED (未付款交易超时关闭，或支付完成后全额退款)
            // TRADE_SUCCESS (交易支付成功)
            // TRADE_FINISHED (交易结束，不可退款)
            return response.getTradeStatus();

        } catch (AlipayApiException e) {
            throw new AlipayClientException(e);
        }
    }

    @Override
    public boolean isSuccess(AlipayConfig config, String tradeId) {
        final String status = status(config, tradeId);
        return StringUtils.equalsIgnoreCase("TRADE_SUCCESS", status);
    }

    private void validateConfig(AlipayConfig config) {
        Validate.notNull(config);
        Validate.notBlank(config.getAppId());
        Validate.notBlank(config.getCallbackNotifyUrl());
        Validate.notBlank(config.getAppPrivateKey());
        Validate.notBlank(config.getAlipayPublicKey());
    }

    private void validateTradeId(String tradeId) {
        Validate.notBlank(tradeId);
    }

    private void validateAmount(long amountInCent) {
        Validate.isTrue(amountInCent >= 0L);
    }

    private String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }

    private String parseAmount(long amountInCent) {
        double payment = (double) amountInCent;
        payment = payment / 100.00;
        return DECIMAL_FORMAT.format(payment);
    }

}
