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

/**
 * 金额变换器。一般用于测试或调试环境，如需要可将金额转换成一分钱等较小的金额。
 *
 * @author 应卓
 */
@FunctionalInterface
public interface AlipayAmountTransformer {

    public static AlipayAmountTransformer alwaysOneCentTransformer() {
        return n -> 1L;
    }

    public static AlipayAmountTransformer nopTransformer() {
        return n -> n;
    }

    public long transform(long amountInCent);

}
