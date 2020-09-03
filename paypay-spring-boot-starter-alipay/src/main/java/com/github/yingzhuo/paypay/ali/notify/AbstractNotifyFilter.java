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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;

/**
 * @author 应卓
 * @since 1.2.0
 */
@Deprecated
public abstract class AbstractNotifyFilter extends OncePerRequestFilter implements Filter, InitializingBean {

//    public static final String SUCCESS = "success";
//    public static final String FAILURE = "failure";
//
//    protected ConfigGroupManager configGroupManager;
//    protected GroupNameResolver groupNameResolver;
//
//    @Override
//    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
//
//        // 解析阿里回调参数
//        val notifyParams = NotifyParams.parseFromRequest(request);
//        val publicKey = configGroupManager.find(groupNameResolver.resolve(request)).getPublicKey();
//
//        try {
//            boolean isValidSign = AlipaySignature.rsaCheckV1(
//                    getValidationMap(request),
//                    publicKey,
//                    notifyParams.getCharset(),
//                    notifyParams.getSignType());
//
//            // 签名不正确
//            if (!isValidSign) {
//                onInvalidSign(request, response, notifyParams);
//                return;
//            }
//        } catch (AlipayApiException e) {
//            onAlipayApiException(request, response, notifyParams, e);
//            return;
//        }
//
//        if ("TRADE_SUCCESS".equalsIgnoreCase(notifyParams.getTradeStatus())) {
//            onTradeSuccess(request, response, notifyParams);
//        } else {
//            onTradeFailure(request, response, notifyParams);
//        }
//    }
//
//    protected void onTradeSuccess(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException {
//        response.setStatus(200);
//        response.getWriter().print(SUCCESS);
//        response.getWriter().flush();
//    }
//
//    protected void onTradeFailure(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException {
//        response.setStatus(200);
//        response.getWriter().print(FAILURE);
//        response.getWriter().flush();
//    }
//
//    protected void onInvalidSign(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams) throws IOException {
//        response.setStatus(200);
//        response.getWriter().print(FAILURE);
//        response.getWriter().flush();
//    }
//
//    protected void onAlipayApiException(HttpServletRequest request, HttpServletResponse response, NotifyParams notifyParams, AlipayApiException exception) throws IOException {
//        response.setStatus(200);
//        response.getWriter().print(FAILURE);
//        response.getWriter().flush();
//    }
//
//    @Override
//    public void afterPropertiesSet() throws ServletException {
//        super.afterPropertiesSet();
//        Assert.notNull(configGroupManager, () -> null);
//        Assert.notNull(groupNameResolver, () -> null);
//    }
//
//    private Map<String, String> getValidationMap(HttpServletRequest request) {
//        final Map<String, String> validationMap = new HashMap<>();
//        final Map<String, String[]> requestParams = request.getParameterMap();
//
//        for (String key : requestParams.keySet()) {
//            String[] values = requestParams.get(key);
//            String valueStr = StringUtils.join(values);
//            validationMap.put(key, valueStr);
//        }
//        return validationMap;
//    }
//
//    public void setConfigGroupManager(ConfigGroupManager configGroupManager) {
//        this.configGroupManager = configGroupManager;
//    }
//
//    public void setGroupNameResolver(GroupNameResolver groupNameResolver) {
//        this.groupNameResolver = groupNameResolver;
//    }

}
