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

import com.github.yingzhuo.paypay.common.AmountTransformer;
import com.github.yingzhuo.paypay.wechat.WechatpayHelper;
import com.github.yingzhuo.paypay.wechat.impl.WechatpayHelperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @author 应卓
 */
class WechatpayAutoConfig {

    @Autowired(required = false)
    private AmountTransformer transformer;

    @Bean
    public WechatpayHelper wechatpayHelper() {
        final WechatpayHelperImpl bean = new WechatpayHelperImpl();
        bean.setTransformer(transformer);
        return bean;
    }

}
