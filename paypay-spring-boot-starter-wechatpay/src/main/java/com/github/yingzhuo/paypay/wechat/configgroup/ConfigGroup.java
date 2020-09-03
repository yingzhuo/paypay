/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat.configgroup;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 应卓
 * @since 1.2.0
 */
@Getter
@Setter
@ToString
public class ConfigGroup {

    private String appId;
    private String mchId;
    private String secretKey;
    private String callbackNotifyUrl;

}
