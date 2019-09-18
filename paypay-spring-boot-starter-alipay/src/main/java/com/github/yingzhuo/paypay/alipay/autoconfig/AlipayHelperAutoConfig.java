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

import com.github.yingzhuo.paypay.alipay.AlipayAmountTransformer;
import com.github.yingzhuo.paypay.alipay.AlipayHelper;
import com.github.yingzhuo.paypay.alipay.impl.AlipayHelperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 应卓
 */
@ConditionalOnProperty(prefix = "paypay.alipay", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AlipayConfigProps.class)
public class AlipayHelperAutoConfig {

    @Autowired(required = false)
    private AlipayAmountTransformer transformer;

    @Bean
    @ConditionalOnMissingBean
    public AlipayHelper alipayHelper(AlipayConfigProps props) {
        return new AlipayHelperImpl(props, transformer != null ? transformer : (a) -> a);
    }

}
