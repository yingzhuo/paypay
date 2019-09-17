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

@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "paypay.alipay", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AlipayNotifyAutoConfig {

    @Autowired(required = false)
    private AlipayNotifyCallback alipayNotifyCallback;

    @Bean
    public FilterRegistrationBean<NotifyFilter> notifyFilterFilterRegistrationBean(AlipayConfigProps props) {
        val reg = new FilterRegistrationBean<NotifyFilter>(new NotifyFilter(props, alipayNotifyCallback != null ? alipayNotifyCallback : new AlipayNotifyCallback() {
        }));
        reg.setName(NotifyFilter.class.getName());
        reg.setOrder(Ordered.LOWEST_PRECEDENCE);
        reg.addUrlPatterns(props.getCallbackNotifyPath());
        return reg;
    }

}
