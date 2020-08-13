/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay.configgroup;

/**
 * @author 应卓
 * @since 1.2.0
 */
@FunctionalInterface
public interface ConfigGroupManager {

    public default ConfigGroup findDefault() {
        return find("default");
    }

    public ConfigGroup find(String groupName);

}
