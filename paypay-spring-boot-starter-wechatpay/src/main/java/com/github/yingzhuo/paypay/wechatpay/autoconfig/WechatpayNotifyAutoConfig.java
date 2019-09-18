package com.github.yingzhuo.paypay.wechatpay.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "paypay.wechatpay", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WechatpayNotifyAutoConfig {
}
