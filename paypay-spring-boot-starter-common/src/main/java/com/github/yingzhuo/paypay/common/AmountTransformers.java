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
 * @author 应卓
 * @since 1.2.0
 */
public final class AmountTransformers {

    private AmountTransformers() {
    }

    public static AmountTransformer alwaysOneCent() {
        return a -> 1L;
    }

    public static AmountTransformer nop() {
        return a -> a;
    }

}
