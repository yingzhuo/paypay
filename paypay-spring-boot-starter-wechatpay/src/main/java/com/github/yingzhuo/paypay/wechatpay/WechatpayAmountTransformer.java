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

/**
 * @author 应卓
 */
@FunctionalInterface
public interface WechatpayAmountTransformer {

    public static WechatpayAmountTransformer alwaysOneCentTransformer() {
        return n -> 1L;
    }

    public static WechatpayAmountTransformer nopTransformer() {
        return n -> n;
    }

    public long transform(long amountInCent);

}
