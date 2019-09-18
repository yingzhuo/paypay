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

import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 应卓
 */
public interface WechatpayNotifyCallback {

    public static final String TRADE_ID_KEY = "out_trade_no";

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

    /**
     * 微信格式化输出
     */
    public static class XmlResponse {

        public static String of(String returnCode, String returnMsg) {
            val xml = new XmlResponse();
            xml.returnCode = returnCode;
            xml.returnMsg = returnMsg;
            return xml.toXmlString();
        }

        private String returnCode = "SUCCESS";
        private String returnMsg = "OK";

        private String toXmlString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<xml><return_code><![CDATA[")
                    .append(returnCode)
                    .append("]]></return_code><return_msg><![CDATA[")
                    .append(returnMsg)
                    .append("]]></return_msg></xml>");
            return sb.toString();
        }
    }

}
