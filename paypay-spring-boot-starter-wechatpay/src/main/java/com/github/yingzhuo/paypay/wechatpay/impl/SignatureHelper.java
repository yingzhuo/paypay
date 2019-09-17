package com.github.yingzhuo.paypay.wechatpay.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
class SignatureHelper {

//    public static enum DigestType {
//        MD5,
//
//        SHA1
//    }
//
//    public static enum CharacterCase {
//        /*大写*/
//        UPPER_CASE,
//
//        /*小写*/
//        LOWER_CASE
//    }
//
//    private String getSign2(Map<String, String> params, String keyName, String keyValue, boolean encode,
//                           DigestType digestType, CharacterCase characterCase) {
//
//        Set<String> keysSet = params.keySet();
//        List<String> keyList = new ArrayList<>(keysSet);
//        keyList.sort(StringUtils::compareIgnoreCase);
//
//        StringBuilder buf = new StringBuilder();
//        for (String key : keyList) {
//            buf.append("&").append(key).append("=");
//            String value = params.get(key);
//            if (encode) {
//                try {
//                    buf.append(URLEncoder.encode(value, "UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    throw new AssertionError();
//                }
//            } else {
//                buf.append(value);
//            }
//        }
//
//        if (StringUtils.isNotEmpty(keyName)) {
//            buf.append("&").append(keyName).append("=");
//        }
//        buf.append(keyValue);
//        String sign;
//        if (digestType == DigestType.SHA1) {
//
//            sign = DigestUtils.sha1Hex(buf.substring(1));
//        } else {
//            sign = DigestUtils.md5Hex(buf.substring(1));
//        }
//        if (characterCase == CharacterCase.UPPER_CASE) {
//            sign = sign.toUpperCase();
//        } else {
//            sign = sign.toLowerCase();
//        }
//        log.info("sign = {}", sign);
//        return sign;
//    }

}
