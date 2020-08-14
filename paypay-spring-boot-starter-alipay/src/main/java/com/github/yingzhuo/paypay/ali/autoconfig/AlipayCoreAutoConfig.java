/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.autoconfig;

import com.github.yingzhuo.paypay.ali.AlipayHelper;
import com.github.yingzhuo.paypay.ali.AmountTransformer;
import com.github.yingzhuo.paypay.ali.configgroup.*;
import com.github.yingzhuo.paypay.ali.impl.AlipayHelperImpl;
import com.github.yingzhuo.paypay.ali.properties.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * @author 应卓
 */
@EnableConfigurationProperties(AlipayProperties.class)
@ConditionalOnProperty(prefix = "paypay.alipay", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AlipayCoreAutoConfig {

    @Autowired(required = false)
    private AmountTransformer transformer;

    @Bean
    public ConfigGroupManager configGroupManager(AlipayProperties props) {
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
    @ConditionalOnMissingBean
    public AlipayHelper aliHelper(ConfigGroupManager configGroupManager) {
        final AlipayHelperImpl bean = new AlipayHelperImpl();
        bean.setAmountTransformer(transformer);
        bean.setConfigGroupManager(configGroupManager);
        return bean;
    }

}
