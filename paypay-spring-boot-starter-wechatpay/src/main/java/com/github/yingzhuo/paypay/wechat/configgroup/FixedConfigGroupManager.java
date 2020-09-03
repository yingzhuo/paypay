/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat.configgroup;

/**
 * @author 应卓
 * @since 1.2.0
 */
public class FixedConfigGroupManager implements ConfigGroupManager {

    private final ConfigGroup configGroup;

    public FixedConfigGroupManager(ConfigGroup configGroup) {
        this.configGroup = configGroup;
    }

    @Override
    public ConfigGroup find(String groupName) {
        return configGroup;
    }

}
