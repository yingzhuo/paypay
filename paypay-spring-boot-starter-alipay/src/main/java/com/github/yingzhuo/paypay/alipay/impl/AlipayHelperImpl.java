/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.yingzhuo.paypay.alipay.AlipayHelper;
import com.github.yingzhuo.paypay.alipay.AmountTransformer;
import com.github.yingzhuo.paypay.alipay.configgroup.ConfigGroupManager;
import com.github.yingzhuo.paypay.alipay.exception.AlipayBusinessException;
import com.github.yingzhuo.paypay.alipay.exception.AlipayClientException;
import com.github.yingzhuo.paypay.alipay.model.PrepaymentParams;
import lombok.val;

import java.text.DecimalFormat;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public class AlipayHelperImpl implements AlipayHelper {

    private final static String URL = "https://openapi.alipay.com/gateway.do";
    private final static String PACKAGE_FORMAT = "json";
    private final static String CHARSET = "UTF-8";
    private final static String SIGN_TYPE = "RSA2";
    private final static String PRODUCT_CODE = "QUICK_MSECURITY_PAY";
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("######0.00");

    private AmountTransformer amountTransformer;
    private ConfigGroupManager configGroupManager;

    public void setAmountTransformer(AmountTransformer amountTransformer) {
        this.amountTransformer = amountTransformer;
    }

    public void setConfigGroupManager(ConfigGroupManager configGroupManager) {
        this.configGroupManager = configGroupManager;
    }

    @Override
    public PrepaymentParams createPrepaymentParams(String configGroupName, String tradeId, long amountInCent, String subject, String passbackParams, String timeoutExpress) {

        val configGroup = configGroupManager.find(configGroupName);
        if (configGroup == null) {
            throw new IllegalArgumentException("ConfigGroup NOT fround. name = '" + configGroupName + "'.");
        }

        if (amountTransformer != null) {
            amountInCent = amountTransformer.transform(amountInCent);
        }

        AlipayClient alipayClient = new DefaultAlipayClient(
                URL,
                configGroup.getAppId(),
                configGroup.getPrivateKey(),
                PACKAGE_FORMAT,
                CHARSET,
                configGroup.getPublicKey(),
                SIGN_TYPE);

        val request = new AlipayTradeAppPayRequest();
        val model = new AlipayTradeAppPayModel();
        model.setPassbackParams(passbackParams);
        model.setSubject(subject);
        model.setOutTradeNo(tradeId);
        model.setTimeoutExpress(timeoutExpress);

        final String amount = parseAmount(amountInCent);
        model.setTotalAmount(amount);
        model.setProductCode(PRODUCT_CODE);
        request.setBizModel(model);
        request.setNotifyUrl(configGroup.getCallbackNotifyUrl());

        PrepaymentParams params = new PrepaymentParams();

        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if (!response.isSuccess()) {
                throw new AlipayBusinessException(response.getMsg(), response.getSubMsg());
            }

            params.setAliParams(response.getBody());
            params.setTradeId(tradeId);
        } catch (AlipayApiException e) {
            throw new AlipayClientException(e);
        }

        return params;
    }

    @Override
    public String getStatus(String configGroupName, String tradeId) {
        val configGroup = configGroupManager.find(configGroupName);
        if (configGroup == null) {
            throw new IllegalArgumentException("ConfigGroup NOT fround. name = '" + configGroupName + "'.");
        }

        val request = new AlipayTradeQueryRequest();

        val alipayClient = new DefaultAlipayClient(
                URL,
                configGroup.getAppId(),
                configGroup.getPrivateKey(),
                PACKAGE_FORMAT,
                CHARSET,
                configGroup.getPublicKey(),
                SIGN_TYPE
        );

        val model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(tradeId);

        request.setBizModel(model);

        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);

            if (!response.isSuccess()) {
                throw new AlipayBusinessException(response.getMsg(), response.getSubMsg());
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

    private String parseAmount(long amountInCent) {
        double payment = (double) amountInCent;
        payment = payment / 100.00;
        return DECIMAL_FORMAT.format(payment);
    }

}
