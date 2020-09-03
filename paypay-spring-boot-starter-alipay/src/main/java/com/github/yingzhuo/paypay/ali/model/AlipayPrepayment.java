/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.model;

import lombok.*;

import java.io.Serializable;

/**
 * 预支付参数
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlipayPrepayment implements Serializable {

    /**
     * 交易编号
     */
    private String tradeId;

    /**
     * 阿里支付参数
     */
    private String params;

}
