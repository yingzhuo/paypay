/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay;

import com.github.yingzhuo.paypay.alipay.model.PrepaymentParams;

/**
 * @author 应卓
 */
public interface AlipayHelper {

    public PrepaymentParams createPrepaymentParams(String tradeId, long amountInCent, String subject, String passbackParams, String timeExpire);

    public boolean isTradeSuccess(String tradeId);

}
