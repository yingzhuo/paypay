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

import com.github.yingzhuo.paypay.alipay.AlipayNotifyCallback;
import com.github.yingzhuo.paypay.alipay.web.NotifyFilter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * @author 应卓
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "paypay.alipay", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AlipayNotifyAutoConfig {

    @Autowired(required = false)
    private AlipayNotifyCallback callback;

    @Bean
    public FilterRegistrationBean<NotifyFilter> alipayNotifyFilterFilterRegistrationBean(AlipayConfigProps props) {
        if (callback == null) {
            callback = new AlipayNotifyCallback() {
            };
        }

        val reg = new FilterRegistrationBean<NotifyFilter>();
        reg.setFilter(new NotifyFilter(props, callback));
        reg.setName(NotifyFilter.class.getName());
        reg.setOrder(Ordered.LOWEST_PRECEDENCE);
        reg.addUrlPatterns(props.getCallbackNotifyPath());
        return reg;
    }

}
