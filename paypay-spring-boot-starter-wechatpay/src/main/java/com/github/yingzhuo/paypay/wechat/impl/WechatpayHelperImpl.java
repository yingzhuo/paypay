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

import com.github.yingzhuo.paypay.wechat.AmountTransformer;
import com.github.yingzhuo.paypay.wechat.WechatpayHelper;
import com.github.yingzhuo.paypay.wechat.configgroup.ConfigGroupManager;
import com.github.yingzhuo.paypay.wechat.exception.WechatpayException;
import com.github.yingzhuo.paypay.wechat.exception.WechatpayPrepaymentParamsCreationException;
import com.github.yingzhuo.paypay.wechat.model.PrepaymentParams;
import com.github.yingzhuo.paypay.wechat.util.DocumentUtils;
import com.github.yingzhuo.paypay.wechat.util.HttpUtils;
import com.github.yingzhuo.paypay.wechat.util.SignUtils;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public class WechatpayHelperImpl implements WechatpayHelper {

    private AmountTransformer transformer;
    private ConfigGroupManager configGroupManager;

    public void setTransformer(AmountTransformer transformer) {
        this.transformer = transformer;
    }

    public void setConfigGroupManager(ConfigGroupManager configGroupManager) {
        this.configGroupManager = configGroupManager;
    }

    @Override
    public PrepaymentParams createPrepaymentParams(String configGroupName, String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire, String ip) {

        val configGroup = configGroupManager.find(configGroupName);
        if (configGroup == null) {
            throw new IllegalArgumentException("ConfigGroup NOT fround. name = '" + configGroupName + "'.");
        }

        if (transformer != null) {
            amountInCent = transformer.transform(amountInCent);
        }

        PrepaymentParams prepaymentParams = new PrepaymentParams();
        Map<String, String> params = new HashMap<>();
        params.put("appid", configGroup.getAppId());
        params.put("mch_id", configGroup.getMchId());
        params.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
        params.put("sign_type", "MD5");
        params.put("body", subject);
        params.put("out_trade_no", tradeId);
        params.put("total_fee", Long.toString(amountInCent));
        params.put("spbill_create_ip", ip);
        params.put("notify_url", configGroup.getCallbackNotifyUrl());
        params.put("trade_type", "APP");
        params.put("attach", passbackParams);

        if (timeExpire != null) {
            params.put("time_expire", timeExpire);
        }

        String sign = SignUtils.createSign(params, configGroup.getSecretKey());
        params.put("sign", sign);
        String wxReqStr = DocumentUtils.mapToXml(params);

        String resultInfo = HttpUtils.postWithXmlBody("https://api.mch.weixin.qq.com/pay/unifiedorder", wxReqStr);
        Map<String, String> resultObject = DocumentUtils.xmlToMap(resultInfo);

        if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("return_code"))) {
            if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("result_code"))) {
                prepaymentParams.setAppId(configGroup.getAppId());
                prepaymentParams.setPartnerId(configGroup.getMchId());
                prepaymentParams.setPrepayId(resultObject.get("prepay_id"));
                prepaymentParams.setPackageValue("Sign=WXPay");
                prepaymentParams.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
                prepaymentParams.setTimestamp(System.currentTimeMillis() / 1000);

                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("appid", prepaymentParams.getAppId());
                hashMap.put("partnerid", prepaymentParams.getPartnerId());
                hashMap.put("prepayid", prepaymentParams.getPrepayId());
                hashMap.put("noncestr", prepaymentParams.getNonceStr());
                hashMap.put("timestamp", Long.toString(prepaymentParams.getTimestamp()));
                hashMap.put("package", prepaymentParams.getPackageValue());
                String paySign = SignUtils.createSign(hashMap, configGroup.getSecretKey());

                prepaymentParams.setSign(paySign);
                prepaymentParams.setTradeId(tradeId);
            } else {
                String subMsg = resultObject.get("err_code_des");
                String code = resultObject.get("err_code");
                throw new WechatpayPrepaymentParamsCreationException(code, subMsg);
            }
        } else {
            String subMsg = resultObject.get("return_msg");
            throw new WechatpayPrepaymentParamsCreationException(null, subMsg);
        }

        return prepaymentParams;
    }

    @Override
    public String getTradeStatus(String configGroupName, String tradeId) {

        val configGroup = configGroupManager.find(configGroupName);
        if (configGroup == null) {
            throw new IllegalArgumentException("ConfigGroup NOT fround. name = '" + configGroupName + "'.");
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put("appid", configGroup.getAppId());
            params.put("mch_id", configGroup.getMchId());
            params.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
            params.put("out_trade_no", tradeId);//微信的订单号，优先使用 transaction_id  二选一
            String sign = SignUtils.createSign(params, configGroup.getSecretKey());
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

}
