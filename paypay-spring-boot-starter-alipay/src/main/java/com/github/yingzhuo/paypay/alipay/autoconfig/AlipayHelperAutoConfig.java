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

import com.github.yingzhuo.paypay.alipay.AlipayHelper;
import com.github.yingzhuo.paypay.alipay.AmountTransformer;
import com.github.yingzhuo.paypay.alipay.configgroup.ConfigGroup;
import com.github.yingzhuo.paypay.alipay.configgroup.ConfigGroupManager;
import com.github.yingzhuo.paypay.alipay.configgroup.FixedConfigGroupManager;
import com.github.yingzhuo.paypay.alipay.configgroup.MapConfigGroupManager;
import com.github.yingzhuo.paypay.alipay.impl.AlipayHelperImpl;
import com.github.yingzhuo.paypay.alipay.properties.AlipayProperties;
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
public class AlipayHelperAutoConfig {

    @Autowired(required = false)
    private AmountTransformer transformer;

    @Autowired(required = false)
    private ConfigGroupManager configGroupManager;

    @Bean
    @ConditionalOnMissingBean
    public AlipayHelper alipayHelper(AlipayProperties props) {
        final AlipayHelperImpl bean = new AlipayHelperImpl();
        bean.setAmountTransformer(transformer);
        bean.setConfigGroupManager(getManager(props));
        return bean;
    }

    private ConfigGroupManager getManager(AlipayProperties props) {
        if (this.configGroupManager != null) return configGroupManager;

        Map<String, ConfigGroup> map = props.getConfigGroups();
        if (map.size() == 1) {
            return new FixedConfigGroupManager(map.values().iterator().next());
        } else {
            return new MapConfigGroupManager(map);
        }
    }

}
