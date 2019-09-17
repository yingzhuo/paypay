package com.github.yingzhuo.paypay.wechatpay.impl;

import com.github.yingzhuo.paypay.wechatpay.WechatpayHelper;
import com.github.yingzhuo.paypay.wechatpay.autoconfig.WechatpayConfigProps;
import com.github.yingzhuo.paypay.wechatpay.model.PrepaymentParams;
import com.github.yingzhuo.paypay.wechatpay.util.HttpUtils;
import com.github.yingzhuo.paypay.wechatpay.util.WXPayUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public class WechatpayHelperImpl implements WechatpayHelper {


    private static final String URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private final WechatpayConfigProps props;

    public WechatpayHelperImpl(WechatpayConfigProps props) {
        this.props = props;
    }

    @Override
    public PrepaymentParams createPrepaymentParams(String tradeId, long paymentCent, String passbackParams, String subject, String timeExpire, String ip) {

        PrepaymentParams prepaymentParams = new PrepaymentParams();

        String nonce_str = RandomStringUtils.randomAlphanumeric(32);
        String sign_type = "MD5";
        long total_fee = paymentCent;
        String trade_type = "APP";

        Map<String, String> params = new HashMap<>();
        params.put("appid", props.getAppId());
        params.put("mch_id", props.getMchId());
        params.put("nonce_str", nonce_str);
        params.put("sign_type", sign_type);
        params.put("body", subject);
        params.put("out_trade_no", tradeId);
        params.put("total_fee", total_fee + "");
        params.put("spbill_create_ip", ip);
        params.put("notify_url", props.getCallbackNotifyUrl());
        params.put("trade_type", trade_type);
        params.put("attach", passbackParams);

        if (timeExpire != null) {
            params.put("time_expire", timeExpire);//prepay_id只有两小时的有效期
        }

        String sign = createSign(params, props.getSecretKey());
        params.put("sign", sign);
        String wxReqStr = WXPayUtil.mapToXml(params);

        String resultInfo = HttpUtils.syncXmlPost(URL, wxReqStr);
        Map<String, String> resultObject = WXPayUtil.xmlToMap(resultInfo);

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
                Assert.isTrue(false, resultObject.get("err_code") + "-" + resultObject.get("err_code_des"));
            }
        } else {
            Assert.isTrue(false, resultObject.get("return_msg"));
        }

        return null;
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
