/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechatpay.exception;

/**
 * @author 应卓
 */
public class WechatPayPrepaymentParamsCreationException extends RuntimeException {

    private String code;
    private String subMessage;

    public WechatPayPrepaymentParamsCreationException(String code, String subMessage) {
        super(code);
        this.code = code;
        this.subMessage = subMessage;
    }

    public String getCode() {
        return code;
    }

    public String getSubMessage() {
        return subMessage;
    }

}
