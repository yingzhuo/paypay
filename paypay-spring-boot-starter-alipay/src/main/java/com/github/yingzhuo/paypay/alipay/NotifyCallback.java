/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay;

import com.github.yingzhuo.paypay.alipay.model.NotifyParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应卓
 */
public interface NotifyCallback {

    public default void onTradeSuccess(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException {
        response.getWriter().write("success");
        response.getWriter().flush();
    }

    public default void onTradeFailure(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException {
        response.getWriter().write("failure");
        response.getWriter().flush();
    }

}
