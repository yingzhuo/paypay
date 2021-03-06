/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.exception;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class AlipayBusinessException extends RuntimeException {

    private final String subMessage;

    public AlipayBusinessException(String message, String subMessage) {
        super(message);
        this.subMessage = subMessage;
    }

    public String getSubMessage() {
        return subMessage;
    }

}
