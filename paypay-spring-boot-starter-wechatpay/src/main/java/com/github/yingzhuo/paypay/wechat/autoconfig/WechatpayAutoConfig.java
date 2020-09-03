/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat.autoconfig;

import com.github.yingzhuo.paypay.wechat.AmountTransformer;
import com.github.yingzhuo.paypay.wechat.WechatpayHelper;
import com.github.yingzhuo.paypay.wechat.configgroup.*;
import com.github.yingzhuo.paypay.wechat.impl.WechatpayHelperImpl;
import com.github.yingzhuo.paypay.wechat.properties.WechatpayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * @author 应卓
 */
@EnableConfigurationProperties(WechatpayProperties.class)
public class WechatpayAutoConfig {

    @Autowired(required = false)
    private AmountTransformer transformer;

    @Bean(name = "wechatpayConfigGroupManager")
    public ConfigGroupManager configGroupManager(WechatpayProperties props) {
        final Map<String, ConfigGroup> map = props.getConfigGroups();

        if (map == null || map.isEmpty()) {
            return new BrokenConfigGroupManager();
        }

        if (map.size() == 1) {
            return new FixedConfigGroupManager(map.values().iterator().next());
        } else {
            return new MapConfigGroupManager(map);
        }
    }

    @Bean
    private WechatpayHelper wechatpayHelper(ConfigGroupManager configGroupManager) {
        final WechatpayHelperImpl bean = new WechatpayHelperImpl();
        bean.setConfigGroupManager(configGroupManager);
        bean.setTransformer(transformer);
        return bean;
    }

}
