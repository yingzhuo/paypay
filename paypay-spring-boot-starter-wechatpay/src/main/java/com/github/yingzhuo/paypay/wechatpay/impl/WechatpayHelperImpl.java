/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechatpay.impl;

import com.github.yingzhuo.paypay.wechatpay.WechatpayAmountTransformer;
import com.github.yingzhuo.paypay.wechatpay.WechatpayHelper;
import com.github.yingzhuo.paypay.wechatpay.autoconfig.WechatpayConfigProps;
import com.github.yingzhuo.paypay.wechatpay.exception.WechatPayPrepaymentParamsCreationException;
import com.github.yingzhuo.paypay.wechatpay.exception.WechatpayException;
import com.github.yingzhuo.paypay.wechatpay.model.PrepaymentParams;
import com.github.yingzhuo.paypay.wechatpay.util.DocumentUtils;
import com.github.yingzhuo.paypay.wechatpay.util.HttpUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public class WechatpayHelperImpl implements WechatpayHelper {

    private final WechatpayConfigProps props;
    private final WechatpayAmountTransformer transformer;

    public WechatpayHelperImpl(WechatpayConfigProps props, WechatpayAmountTransformer transformer) {
        this.props = props;
        this.transformer = transformer;
    }

    @Override
    public PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String passbackParams, String subject, String timeExpire, String ip) {

        amountInCent = transformer.transform(amountInCent);

        PrepaymentParams prepaymentParams = new PrepaymentParams();
        Map<String, String> params = new HashMap<>();
        params.put("appid", props.getAppId());
        params.put("mch_id", props.getMchId());
        params.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
        params.put("sign_type", "MD5");
        params.put("body", subject);
        params.put("out_trade_no", tradeId);
        params.put("total_fee", Long.toString(amountInCent));
        params.put("spbill_create_ip", ip);
        params.put("notify_url", props.getCallbackNotifyUrl());
        params.put("trade_type", "APP");
        params.put("attach", passbackParams);

        if (timeExpire != null) {
            params.put("time_expire", timeExpire);  //prepay_id只有两小时的有效期
        }

        String sign = createSign(params, props.getSecretKey());
        params.put("sign", sign);
        String wxReqStr = DocumentUtils.mapToXml(params);

        String resultInfo = HttpUtils.postWithXmlBody("https://api.mch.weixin.qq.com/pay/unifiedorder", wxReqStr);
        Map<String, String> resultObject = DocumentUtils.xmlToMap(resultInfo);

        if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("return_code"))) {
            if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("result_code"))) {
                prepaymentParams.setAppId(props.getAppId());
                prepaymentParams.setPartnerId(props.getMchId());
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
                String paySign = createSign(hashMap, props.getSecretKey());

                prepaymentParams.setSign(paySign);
                prepaymentParams.setTradeId(tradeId);
            } else {
                String subMsg = resultObject.get("err_code_des");
                String code = resultObject.get("err_code");
                throw new WechatPayPrepaymentParamsCreationException(code, subMsg);
            }
        } else {
            String subMsg = resultObject.get("return_msg");
            throw new WechatPayPrepaymentParamsCreationException(null, subMsg);
        }

        return prepaymentParams;
    }

    @Override
    public boolean isTradeSuccess(String tradeId) {
        if (StringUtils.isBlank(tradeId)) {
            return false;
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put("appid", props.getAppId());
            params.put("mch_id", props.getMchId());
            params.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
            params.put("out_trade_no", tradeId);//微信的订单号，优先使用 transaction_id  二选一
            String sign = createSign(params, props.getSecretKey());
            params.put("sign", sign);
            String wxReqStr = DocumentUtils.mapToXml(params);

            String resultInfo = HttpUtils.postWithXmlBody("https://api.mch.weixin.qq.com/pay/orderquery", wxReqStr);
            Map<String, String> resultObject = DocumentUtils.xmlToMap(resultInfo);

            if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("return_code"))) {

                if (StringUtils.equalsIgnoreCase("SUCCESS", resultObject.get("result_code"))) {

                    String trade_state = resultObject.get("trade_state");
                    return StringUtils.equalsIgnoreCase("SUCCESS", trade_state);

                } else {
                    String code = resultObject.get("err_code");
                    String subMsg = resultObject.get("err_code_des");
                    throw new WechatPayPrepaymentParamsCreationException(code, subMsg);
                }
            } else {
                String subMsg = resultObject.get("return_msg");
                throw new WechatPayPrepaymentParamsCreationException(null, subMsg);
            }
        } catch (Exception e) {
            throw new WechatpayException(e);
        }
    }

    private String createSign(Map<String, String> params, String keyValue) {
        Set<String> keysSet = params.keySet();
        List<String> keyList = new ArrayList<>(keysSet);
        keyList.sort(StringUtils::compareIgnoreCase);

        StringBuilder buf = new StringBuilder();
        for (String key : keyList) {
            buf.append("&").append(key).append("=");
            String value = params.get(key);
            buf.append(value);
        }

        buf.append("&").append("key").append("=");
        buf.append(keyValue);
        String sign = DigestUtils.md5Hex(buf.substring(1));
        return sign.toUpperCase();
    }

}
