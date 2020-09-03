/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali;

import com.github.yingzhuo.paypay.ali.model.AlipayPrepayment;

/**
 * @author 应卓
 * @since 1.2.0
 */
public interface AlipayHelper {

    public AlipayPrepayment prepay(AlipayConfig config, String tradeId, long amountInCent, String subject, String passbackParams, String timeoutExpress);

    public String status(AlipayConfig config, String tradeId);

    public boolean isSuccess(AlipayConfig config, String tradeId);

}
