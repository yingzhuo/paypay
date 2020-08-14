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

import com.github.yingzhuo.paypay.ali.notify.NotifyCallback;
import com.github.yingzhuo.paypay.ali.notify.NotifyFilter;
import com.github.yingzhuo.paypay.ali.properties.AlipayProperties;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author 应卓
 * @since 1.2.0
 */
@EnableConfigurationProperties(AlipayProperties.class)
@ConditionalOnProperty(prefix = "paypay.alipay", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class AlipayWebAutoConfig {

    @Bean
    public FilterRegistrationBean<NotifyFilter> notifyFilter(AlipayProperties props, NotifyCallback callback) {
        val filter = new NotifyFilter(callback);
        val bean = new FilterRegistrationBean<>(filter);
        bean.setName(NotifyFilter.class.getName());
        bean.addUrlPatterns(props.getNotifyFilter().getPath());
        bean.setOrder(props.getNotifyFilter().getOrder());
        return bean;
    }

}
