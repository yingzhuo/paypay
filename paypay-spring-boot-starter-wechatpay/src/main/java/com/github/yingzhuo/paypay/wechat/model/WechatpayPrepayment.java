/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat.model;

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
public class WechatpayPrepayment implements Prepayment {

    /**
     * 交易编号
     */
    private String tradeId;

    /**
     * 微信 应用ID
     */
    private String appId;

    /**
     * 微信 商户号
     */
    private String partnerId;

    /**
     * 微信 预支付交易会话ID
     */
    private String prepayId;

    /**
     * 微信 扩展字段
     */
    private String packageValue;

    /**
     * 微信 随机字符串
     */
    private String nonceStr;

    /**
     * 微信 时间戳
     */
    private long timestamp = 0L;

    /**
     * 微信 签名
     */
    private String sign;

}
