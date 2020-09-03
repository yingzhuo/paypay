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
import com.github.yingzhuo.paypay.ali.impl.AlipayHelperImpl;
import com.github.yingzhuo.paypay.common.AmountTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author 应卓
 */
class AlipayAutoConfig {

    @Autowired(required = false)
    private AmountTransformer transformer;

    @Bean
    @ConditionalOnMissingBean
    public AlipayHelper alipayHelper() {
        final AlipayHelperImpl bean = new AlipayHelperImpl();
        bean.setAmountTransformer(transformer);
        return bean;
    }

}
