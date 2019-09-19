/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay.autoconfig;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author 白宝鹏
 * @author 应卓
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "paypay.alipay")
public class AlipayConfigProps implements Serializable, InitializingBean, ResourceLoaderAware {

    @Getter(AccessLevel.NONE)
    private ResourceLoader resourceLoader;

    private boolean enabled = true;
    private String appId;
    private String privateKey;
    private String privateKeyLocation;
    private String publicKey;
    private String publicKeyLocation;
    private String callbackNotifyUrl;
    private String callbackNotifyPath = "/paypay/alipay-notify";

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!enabled) {
            return;
        }

        Assert.hasText(appId, "Config error! 'paypay.alipay.app-id' is blank.");
        Assert.hasText(callbackNotifyUrl, "Config error! 'paypay.alipay.callback-notify-url' is blank.");

        if (StringUtils.isNotBlank(privateKeyLocation)) {
            this.privateKey = loadResourceAsString(privateKeyLocation);
        }
        Assert.hasText(privateKey, "Config error! 'paypay.alipay.private-key' and 'paypay.alipay.private-key-location' both blank.");

        if (StringUtils.isNotBlank(publicKeyLocation)) {
            this.publicKey = loadResourceAsString(publicKeyLocation);
        }
        Assert.hasText(this.publicKey, "Config error! 'paypay.alipay.public-key' and 'paypay.alipay.public-key-location' both blank.");
    }

    private String loadResourceAsString(String location) throws IOException {
        val sb = new StringBuilder();
        val resource = resourceLoader.getResource(location);
        val lines = IOUtils.lineIterator(resource.getInputStream(), StandardCharsets.UTF_8);

        while (lines.hasNext()) {
            val line = lines.nextLine();
            sb.append(StringUtils.trim(line));
        }

        return sb.toString();
    }

}
