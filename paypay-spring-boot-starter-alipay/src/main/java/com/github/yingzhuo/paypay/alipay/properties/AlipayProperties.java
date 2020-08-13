/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay.properties;

import com.github.yingzhuo.paypay.alipay.configgroup.ConfigGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白宝鹏
 * @author 应卓
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "paypay.alipay")
public class AlipayProperties {

    private boolean enabled = true;
    private Map<String, ConfigGroup> configGroups = new HashMap<>();

}
