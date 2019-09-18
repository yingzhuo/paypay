/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechatpay;

import com.github.yingzhuo.paypay.wechatpay.model.PrepaymentParams;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public interface WechatpayHelper {

    public PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire, String ip);

    public default PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire) {
        return createPrepaymentParams(tradeId, amountInCent, subject, passbackParams, timeExpire, "127.0.0.1");
    }

    public default PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String subject, String passbackParams) {
        return createPrepaymentParams(tradeId, amountInCent, subject, passbackParams, null);
    }

    public default PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String subject) {
        return createPrepaymentParams(tradeId, amountInCent, subject, WechatpayHelper.class.getName());
    }

    public boolean isTradeSuccess(String tradeId);

}
