/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.configgroup;

/**
 * @author 应卓
 * @since 1.2.0
 */
public final class BrokenConfigGroupManager implements ConfigGroupManager {

    @Override
    public ConfigGroup findDefault() {
        throw new UnsupportedOperationException("No config-group found");
    }

    @Override
    public ConfigGroup find(String groupName) {
        throw new UnsupportedOperationException("No config-group found");
    }

}
