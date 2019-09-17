/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay.exception;

import com.alipay.api.AlipayApiException;

/**
 * @author 应卓
 */
public class AlipayException extends RuntimeException {

    public AlipayException(AlipayApiException cause) {
        super(cause);
    }

}
