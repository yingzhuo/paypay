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
import com.github.yingzhuo.paypay.alipay.AlipayAmountTransformer;
import com.github.yingzhuo.paypay.alipay.AlipayHelper;
import com.github.yingzhuo.paypay.alipay.autoconfig.AlipayConfigProps;
import com.github.yingzhuo.paypay.alipay.exception.AlipayException;
import com.github.yingzhuo.paypay.alipay.exception.AlipayPrepaymentParamsCreationException;
import com.github.yingzhuo.paypay.alipay.exception.AlipaySuccessCheckingException;
import com.github.yingzhuo.paypay.alipay.model.PrepaymentParams;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

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

    private final AlipayConfigProps props;
    private final AlipayAmountTransformer transformer;
    private final DecimalFormat df = new DecimalFormat("######0.00");

    public AlipayHelperImpl(AlipayConfigProps props, AlipayAmountTransformer transformer) {
        this.props = props;
        this.transformer = transformer;
    }

    @Override
    public PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire) {

        amountInCent = transformer.transform(amountInCent);

        AlipayClient alipayClient = new DefaultAlipayClient(
                URL,
                props.getAppId(),
                props.getEffectivePrivateKey(),
                PACKAGE_FORMAT,
                CHARSET,
                props.getPublicKey(),
                SIGN_TYPE);

        val request = new AlipayTradeAppPayRequest();
        val model = new AlipayTradeAppPayModel();
        model.setPassbackParams(passbackParams);
        model.setSubject(subject);
        model.setOutTradeNo(tradeId);

        if (timeExpire != null) {
            model.setTimeExpire(timeExpire);
        } else {
            model.setTimeoutExpress("2h");
        }

        final String amount = parseAmount(amountInCent);
        model.setTotalAmount(amount);
        model.setProductCode(PRODUCT_CODE);
        request.setBizModel(model);
        request.setNotifyUrl(props.getCallbackNotifyUrl());

        PrepaymentParams params = new PrepaymentParams();

        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if (!response.isSuccess()) {
                throw new AlipayPrepaymentParamsCreationException(response.getMsg(), response.getSubMsg());
            }

            params.setAliParams(response.getBody());
            params.setTradeId(tradeId);
        } catch (AlipayApiException e) {
            throw new AlipayException(e);
        }

        return params;
    }

    @Override
    public boolean isTradeSuccess(String tradeId) {
        if (StringUtils.isBlank(tradeId)) {
            return false;
        }

        boolean ok = false;

        val request = new AlipayTradeQueryRequest();

        val alipayClient = new DefaultAlipayClient(
                URL,
                props.getAppId(),
                props.getEffectivePrivateKey(),
                PACKAGE_FORMAT,
                CHARSET,
                props.getPublicKey(),
                SIGN_TYPE);

        val model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(tradeId);

        request.setBizModel(model);

        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);

            if (!response.isSuccess()) {
                throw new AlipaySuccessCheckingException(response.getMsg(), response.getSubMsg());
            }

            // WAIT_BUYER_PAY (交易创建，等待买家付款)
            // TRADE_CLOSED (未付款交易超时关闭，或支付完成后全额退款)
            // TRADE_SUCCESS (交易支付成功)
            // TRADE_FINISHED (交易结束，不可退款)
            if (StringUtils.equalsIgnoreCase("TRADE_SUCCESS", response.getTradeStatus())) {
                ok = true;
            }

        } catch (AlipayApiException e) {
            throw new AlipayException(e);
        }

        return ok;
    }

    private String parseAmount(long amountInCent) {
        double payment = (double) amountInCent;
        payment = payment / 100.00;
        return df.format(payment);
    }

}
