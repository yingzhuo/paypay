/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali;

import lombok.*;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.2.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AlipayConfig implements Serializable {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 回调接口
     */
    private String callbackNotifyUrl;

    // 应当注意的是: alipayPublicKey 与 appPrivateKey 并不是一对RSA秘钥

    /**
     * 阿里支付私钥 (RSA2)
     */
    private String alipayPublicKey;

    // 应当注意的是: alipayPublicKey 与 appPrivateKey 并不是一对RSA秘钥

    /**
     * 应用私钥 (RSA2)
     */
    private String appPrivateKey;

}
