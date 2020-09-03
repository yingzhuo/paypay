/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 白宝鹏
 * @author 应卓
 */
public final class SignUtils {

    private SignUtils() {
    }

    public static String createSign(Map<String, String> params, String keyValue) {
        Set<String> keysSet = params.keySet();
        List<String> keyList = new ArrayList<>(keysSet);
        keyList.sort(StringUtils::compareIgnoreCase);

        StringBuilder buf = new StringBuilder();
        for (String key : keyList) {
            buf.append("&").append(key).append("=");
            String value = params.get(key);
            buf.append(value);
        }

        buf.append("&").append("key").append("=");
        buf.append(keyValue);
        String sign = DigestUtils.md5Hex(buf.substring(1));
        return sign.toUpperCase();
    }

}
