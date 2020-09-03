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

/**
 * @author 应卓
 */
@FunctionalInterface
public interface AmountTransformer {

    public static AmountTransformer alwaysOneCentTransformer() {
        return n -> 1L;
    }

    public static AmountTransformer nopTransformer() {
        return n -> n;
    }

    public long transform(long amountInCent);

}
