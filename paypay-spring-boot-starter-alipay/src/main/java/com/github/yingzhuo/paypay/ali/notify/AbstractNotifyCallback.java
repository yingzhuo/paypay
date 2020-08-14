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

import com.github.yingzhuo.paypay.ali.configgroup.ConfigGroupManager;
import com.github.yingzhuo.paypay.ali.model.NotifyParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应卓
 * @since 1.2.0
 */
public abstract class AbstractNotifyCallback implements NotifyCallback {

    private final ConfigGroupManager configGroupManager;

    public AbstractNotifyCallback(ConfigGroupManager configGroupManager) {
        this.configGroupManager = configGroupManager;
    }

    @Override
    public final ConfigGroupManager getConfigGroupManager() {
        return this.configGroupManager;
    }

    @Override
    public void onTradeFailure(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException {
        response.setStatus(200);
        response.getWriter().print(FAILURE);
        response.getWriter().flush();
    }

    @Override
    public void onInvalidSign(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException {
        response.setStatus(200);
        response.getWriter().print(FAILURE);
        response.getWriter().flush();
    }

}
