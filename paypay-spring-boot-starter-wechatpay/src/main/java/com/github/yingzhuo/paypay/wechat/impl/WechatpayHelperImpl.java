/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat.impl;

import com.github.yingzhuo.paypay.common.AmountTransformer;
import com.github.yingzhuo.paypay.wechat.WechatpayConfig;
import com.github.yingzhuo.paypay.wechat.WechatpayHelper;
import com.github.yingzhuo.paypay.wechat.exception.WechatpayException;
import com.github.yingzhuo.paypay.wechat.exception.WechatpayPrepaymentParamsCreationException;
import com.github.yingzhuo.paypay.wechat.model.WechatpayPrepayment;
import com.github.yingzhuo.paypay.wechat.util.DocumentUtils;
import com.github.yingzhuo.paypay.wechat.util.HttpUtils;
import com.github.yingzhuo.paypay.wechat.util.SignUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public class WechatpayHelperImpl implements WechatpayHelper {

    private static final String URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private AmountTransformer transformer;

    public void setTransformer(AmountTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public WechatpayPrepayment prepay(WechatpayConfig config, String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire) {
        validateConfig(config);
        Validate.notEmpty(tradeId);
        Validate.isTrue(amountInCent >= 0L);

        if (transformer != null) {
            amountInCent = transformer.transform(amountInCent);
        }

        final String nonce = RandomStringUtils.randomAlphanumeric(32);

        WechatpayPrepayment prepayment = new WechatpayPrepayment();
        Map<String, String> params = new HashMap<>();
        params.put("appid", config.getAppId());
        params.put("mch_id", config.getMchId());
        params.put("nonce_str", nonce);
        params.put("sign_type", "MD5");
        params.put("body", subject);
        params.put("out_trade_no", tradeId);
        params.put("total_fee", Long.toString(amountInCent));
        params.put("spbill_create_ip", "0.0.0.0");
        params.put("notify_url", config.getCallbackNotifyUrl());
        params.put("trade_type", "APP");
        params.put("attach", passbackParams);

        if (timeExpire != null) {
            params.put("time_expire", timeExpire);
        }

        String sign = SignUtils.createSign(params, config.getSecretKey());
        params.put("sign", sign);
        String wxReqStr = DocumentUtils.mapToXml(params);

        String resultInfo = HttpUtils.postWithXmlBody(URL, wxReqStr);
        Map<String, String> resultObject = DocumentUtils.xmlToMap(resultInfo);

        if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("return_code"))) {
            if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("result_code"))) {
                prepayment.setAppId(config.getAppId());
                prepayment.setPartnerId(config.getMchId());
                prepayment.setPrepayId(resultObject.get("prepay_id"));
                prepayment.setPackageValue("Sign=WXPay");
                prepayment.setNonceStr(nonce);
                prepayment.setTimestamp(System.currentTimeMillis() / 1000);
                prepayment.setTradeId(tradeId);

                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("appid", prepayment.getAppId());
                hashMap.put("partnerid", prepayment.getPartnerId());
                hashMap.put("prepayid", prepayment.getPrepayId());
                hashMap.put("noncestr", prepayment.getNonceStr());
                hashMap.put("timestamp", Long.toString(prepayment.getTimestamp()));
                hashMap.put("package", prepayment.getPackageValue());
                String paySign = SignUtils.createSign(hashMap, config.getSecretKey());
                prepayment.setSign(paySign);
            } else {
                String subMsg = resultObject.get("err_code_des");
                String code = resultObject.get("err_code");
                throw new WechatpayPrepaymentParamsCreationException(code, subMsg);
            }
        } else {
            String subMsg = resultObject.get("return_msg");
            throw new WechatpayPrepaymentParamsCreationException(null, subMsg);
        }

        return prepayment;
    }

    @Override
    public String status(WechatpayConfig config, String tradeId) {
        validateConfig(config);
        Validate.notEmpty(tradeId);

        try {
            Map<String, String> params = new HashMap<>();
            params.put("appid", config.getAppId());
            params.put("mch_id", config.getMchId());
            params.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
            params.put("out_trade_no", tradeId);//微信的订单号，优先使用 transaction_id  二选一
            String sign = SignUtils.createSign(params, config.getSecretKey());
            params.put("sign", sign);
            String wxReqStr = DocumentUtils.mapToXml(params);

            String resultInfo = HttpUtils.postWithXmlBody("https://api.mch.weixin.qq.com/pay/orderquery", wxReqStr);
            Map<String, String> resultObject = DocumentUtils.xmlToMap(resultInfo);

            if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("return_code"))) {
                if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("result_code"))) {
                    return resultObject.get("trade_state");
                } else {
                    String code = resultObject.get("err_code");
                    String subMsg = resultObject.get("err_code_des");
                    throw new WechatpayPrepaymentParamsCreationException(code, subMsg);
                }
            } else {
                String subMsg = resultObject.get("return_msg");
                throw new WechatpayPrepaymentParamsCreationException(null, subMsg);
            }
        } catch (Exception e) {
            throw new WechatpayException(e);
        }
    }

    @Override
    public boolean isSuccess(WechatpayConfig config, String tradeId) {
        final String status = status(config, tradeId);
        return StringUtils.equalsIgnoreCase("SUCCESS", status);
    }

    private void validateConfig(WechatpayConfig config) {
        Validate.notNull(config);
        Validate.notEmpty(config.getAppId());
        Validate.notEmpty(config.getCallbackNotifyUrl());
        Validate.notEmpty(config.getMchId());
        Validate.notEmpty(config.getSecretKey());
    }

}
