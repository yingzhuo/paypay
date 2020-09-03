/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechat.notify;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 应卓
 * @since 1.2.0
 */
@FunctionalInterface
public interface GroupNameResolver {

    public String resolve(HttpServletRequest request);

}
