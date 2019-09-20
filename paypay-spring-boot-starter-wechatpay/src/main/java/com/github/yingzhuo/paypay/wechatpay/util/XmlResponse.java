/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.wechatpay.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 白宝鹏
 * @author 应卓
 * @since 1.0.2
 */
public final class XmlResponse {

    private XmlResponse() {
    }

    public static String of(String returnCode, String returnMsg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml><return_code><![CDATA[")
                .append(returnCode)
                .append("]]></return_code><return_msg><![CDATA[")
                .append(returnMsg)
                .append("]]></return_msg></xml>");
        return sb.toString();
    }

    public static void write(HttpServletResponse response, String returnCode, String returnMsg) throws IOException {
        String xml = of(returnCode, returnMsg);
        response.getWriter().write(xml);
        response.getWriter().flush();
    }

}
