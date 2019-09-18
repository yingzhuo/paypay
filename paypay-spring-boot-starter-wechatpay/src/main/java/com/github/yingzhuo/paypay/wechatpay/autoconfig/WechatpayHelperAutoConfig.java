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

import com.github.yingzhuo.paypay.wechatpay.WechatpayHelper;
import com.github.yingzhuo.paypay.wechatpay.impl.WechatpayHelperImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 应卓
 */
@EnableConfigurationProperties(WechatpayConfigProps.class)
public class WechatpayHelperAutoConfig {

    @Bean
    private WechatpayHelper wechatpayHelper(WechatpayConfigProps props) {
        return new WechatpayHelperImpl(props);
    }

}
