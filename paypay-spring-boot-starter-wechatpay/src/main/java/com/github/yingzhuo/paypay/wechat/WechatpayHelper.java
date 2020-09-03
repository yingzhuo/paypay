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

import com.github.yingzhuo.paypay.wechat.model.Prepayment;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public interface WechatpayHelper {

    public Prepayment prepay(WechatpayConfig config, String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire);

    public String status(WechatpayConfig config, String tradeId);

    public boolean isSuccess(WechatpayConfig config, String tradeId);

}
