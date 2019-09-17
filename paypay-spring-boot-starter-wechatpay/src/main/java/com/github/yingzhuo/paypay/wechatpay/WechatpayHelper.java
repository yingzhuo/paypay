package com.github.yingzhuo.paypay.wechatpay;

import com.github.yingzhuo.paypay.wechatpay.model.PrepaymentParams;

public interface WechatpayHelper {

    public PrepaymentParams createPrepaymentParams(String tradeId, long paymentCent, String passbackParams, String subject, String timeExpire, String ip);

}
