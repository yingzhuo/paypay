/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechatpay;

import com.github.yingzhuo.paypay.wechatpay.util.XmlResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 应卓
 */
public interface WechatpayNotifyCallback {

    public static final String TRADE_ID_KEY = "out_trade_no";

    public default void onEmptyRequestBody(HttpServletResponse response) throws IOException {
        String xml = XmlResponse.of("FAIL", "Empty Request Body");
        response.setStatus(500);
        response.getWriter().write(xml);
        response.getWriter().flush();
    }

    public default void onInvalidSign(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap) throws IOException {
        String xml = XmlResponse.of("FAIL", "Invalid sign.");
        response.getWriter().write(xml);
        response.getWriter().flush();
    }

    public default void onTradeSuccess(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap) throws IOException {
        String xml = XmlResponse.of("SUCCESS", "SUCCESS");
        response.getWriter().write(xml);
        response.getWriter().flush();
    }

    public default void onTradeFailure(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap) throws IOException {
        String xml = XmlResponse.of("FAIL", "FAIL");
        response.getWriter().write(xml);
        response.getWriter().flush();
    }

    public default void onException(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap, Exception ex) throws IOException {
        String xml = XmlResponse.of("FAIL", "Exception");
        response.getWriter().write(xml);
        response.getWriter().flush();
    }

}
