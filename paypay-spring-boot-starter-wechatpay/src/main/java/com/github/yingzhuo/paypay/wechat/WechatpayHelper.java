/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat;

import com.github.yingzhuo.paypay.wechat.model.PrepaymentParams;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public interface WechatpayHelper {

    public PrepaymentParams createPrepaymentParams(String configGroupName, String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire, String ip);

    public default PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire, String ip) {
        return createPrepaymentParams("default", tradeId, amountInCent, subject, passbackParams, timeExpire, ip);
    }

    public String getTradeStatus(String configGroupName, String tradeId);

    public default boolean isTradeSuccess(String configGroupName, String tradeId) {
        return "SUCCESS".equalsIgnoreCase(getTradeStatus(configGroupName, tradeId));
    }

}
