/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.common;

/**
 * 金额变换器。一般用于测试或调试环境，如需要可将金额转换成一分钱等较小的金额。
 *
 * @author 应卓
 */
@FunctionalInterface
public interface AmountTransformer {

    public long transform(long amountInCent);

}
