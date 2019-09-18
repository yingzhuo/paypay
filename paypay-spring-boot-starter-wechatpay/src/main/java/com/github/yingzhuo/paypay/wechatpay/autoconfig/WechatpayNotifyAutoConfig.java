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

import com.github.yingzhuo.paypay.wechatpay.WechatpayNotifyCallback;
import com.github.yingzhuo.paypay.wechatpay.web.NotifyFilter;
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
@ConditionalOnProperty(prefix = "paypay.wechatpay", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WechatpayNotifyAutoConfig {

    @Autowired(required = false)
    private WechatpayNotifyCallback callback;

    @Bean
    public FilterRegistrationBean<NotifyFilter> wechatPayNotifyFilterFilterRegistrationBean(WechatpayConfigProps props) {

        if (callback == null) {
            callback = new WechatpayNotifyCallback() {
            };
        }

        val reg = new FilterRegistrationBean<NotifyFilter>();
        reg.setFilter(new NotifyFilter(props, callback));
        reg.addUrlPatterns(props.getCallbackNotifyPath());
        reg.setName(NotifyFilter.class.getName());
        reg.setOrder(Ordered.LOWEST_PRECEDENCE);
        return reg;
    }

}
