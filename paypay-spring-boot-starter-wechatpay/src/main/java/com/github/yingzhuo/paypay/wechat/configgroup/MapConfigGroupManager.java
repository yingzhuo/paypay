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

import java.util.Map;
import java.util.Objects;

/**
 * @author 应卓
 * @since 1.2.0
 */
public class MapConfigGroupManager implements ConfigGroupManager {

    private final Map<String, ConfigGroup> map;

    public MapConfigGroupManager(Map<String, ConfigGroup> map) {
        this.map = Objects.requireNonNull(map);
    }

    @Override
    public ConfigGroup find(String groupName) {
        return map.get(groupName);
    }

}
