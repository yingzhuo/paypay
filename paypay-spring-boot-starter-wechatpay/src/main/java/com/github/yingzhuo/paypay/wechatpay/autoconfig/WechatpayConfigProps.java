/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechatpay.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author 白宝鹏
 * @author 应卓
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "paypay.wechatpay")
public class WechatpayConfigProps implements Serializable, InitializingBean {

    private boolean enabled = true;
    private String appId;
    private String mchId;
    private String secretKey;
    private String callbackNotifyUrl;
    private String callbackNotifyPath = "/paypay/wechatpay-notify";

    @Override
    public void afterPropertiesSet() {
        if (!enabled) {
            return;
        }

        Assert.hasText(appId, "Config error! 'paypay.wechatpay.app-id' is blank.");
        Assert.hasText(mchId, "Config error! 'paypay.wechatpay.mch-id' is blank.");
        Assert.hasText(secretKey, "Config error! 'paypay.wechatpay.secret-key' is blank.");
        Assert.hasText(callbackNotifyUrl, "Config error! 'paypay.wechatpay.callback-notify-url' is blank.");
    }

}
