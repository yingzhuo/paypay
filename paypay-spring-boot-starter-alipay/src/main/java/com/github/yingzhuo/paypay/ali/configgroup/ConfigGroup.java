/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.configgroup;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.2.0
 */
@Getter
@Setter
@ToString
public class ConfigGroup implements Serializable {

    private String appId;
    private String privateKey;
    private String publicKey;
    private String callbackNotifyUrl;

}
