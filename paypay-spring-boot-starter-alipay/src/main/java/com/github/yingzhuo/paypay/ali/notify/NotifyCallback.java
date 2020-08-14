/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__,_|\__, | .__/ \__,_|\__, |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.ali.notify;

import com.alipay.api.AlipayApiException;
import com.github.yingzhuo.paypay.ali.configgroup.ConfigGroupManager;
import com.github.yingzhuo.paypay.ali.model.NotifyParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应卓
 */
public interface NotifyCallback {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public ConfigGroupManager getConfigGroupManager();

    public String getPublicKey(ConfigGroupManager configGroupManager, NotifyParams notifyParams);

    public void onTradeSuccess(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException;

    public void onTradeFailure(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException;

    public void onInvalidSign(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException;

    public void onAlipayApiException(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams, AlipayApiException exception) throws IOException;

}
