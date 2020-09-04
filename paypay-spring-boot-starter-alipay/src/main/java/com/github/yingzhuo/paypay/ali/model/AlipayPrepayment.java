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

import com.github.yingzhuo.paypay.common.Prepayment;
import lombok.*;

/**
 * 预支付参数
 *
 * @author 应卓
 * @author 白宝鹏
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlipayPrepayment implements Prepayment {

    /**
     * 交易编号
     */
    private String tradeId;

    /**
     * 阿里支付参数
     */
    private String params;

}
