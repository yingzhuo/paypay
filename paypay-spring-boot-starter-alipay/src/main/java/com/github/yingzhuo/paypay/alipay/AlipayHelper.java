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

    // 允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。

    public PrepaymentParams createPrepaymentParams(String configGroupName, String tradeId, long amountInCent, String subject, String passbackParams, String timeoutExpress);

    public String getStatus(String configGroupName, String tradeId);

    public default boolean isTradeSuccess(String configGroupName, String tradeId) {
        return "TRADE_SUCCESS".equals(getStatus(configGroupName, tradeId));
    }

}
