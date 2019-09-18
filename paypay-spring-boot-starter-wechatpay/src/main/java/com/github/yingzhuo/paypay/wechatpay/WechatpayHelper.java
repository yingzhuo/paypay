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

    public PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String passbackParams, String subject, String timeExpire, String ip);

    public boolean isTradeSuccess(String tradeId);

}
