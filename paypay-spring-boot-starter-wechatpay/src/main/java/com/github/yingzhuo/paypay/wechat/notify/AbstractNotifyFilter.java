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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;

/**
 * @author 应卓
 * @since 1.2.0
 */
@Deprecated
public abstract class AbstractNotifyFilter extends OncePerRequestFilter implements Filter, InitializingBean {
//
//    public static final String TRADE_ID_KEY = "out_trade_no";
//
//    protected ConfigGroupManager configGroupManager;
//    protected GroupNameResolver groupNameResolver;
//
//    @Override
//    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
//        val secretKey = configGroupManager.find(groupNameResolver.resolve(request)).getSecretKey();
//
//        final String requestXml = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
//
//        if (StringUtils.isBlank(requestXml)) {
//            onEmptyRequestBody(response);
//            return;
//        }
//
//        Map<String, String> requestObjMap = DocumentUtils.xmlToMap(requestXml);
//        log.trace("request body xml :\n{}", requestXml);
//
//        try {
//
//            if (StringUtils.equalsIgnoreCase("SUCCESS", requestObjMap.get("return_code"))) {
//                if (StringUtils.equalsIgnoreCase("SUCCESS", requestObjMap.get("result_code"))) {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("result_code", requestObjMap.get("result_code"));
//                    map.put("appid", requestObjMap.get("appid"));
//                    map.put("mch_id", requestObjMap.get("mch_id"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("device_info")))
//                        map.put("device_info", requestObjMap.get("device_info"));
//                    map.put("nonce_str", requestObjMap.get("nonce_str"));
//                    map.put("return_code", requestObjMap.get("return_code"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("err_code")))
//                        map.put("err_code", requestObjMap.get("err_code"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("err_code_des")))
//                        map.put("err_code_des", requestObjMap.get("err_code_des"));
//                    map.put("openid", requestObjMap.get("openid"));
//                    map.put("is_subscribe", requestObjMap.get("is_subscribe"));
//                    map.put("trade_type", requestObjMap.get("trade_type"));
//                    map.put("bank_type", requestObjMap.get("bank_type"));
//                    map.put("total_fee", requestObjMap.get("total_fee"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("fee_type")))
//                        map.put("fee_type", requestObjMap.get("fee_type"));
//                    map.put("cash_fee", requestObjMap.get("cash_fee"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("cash_fee_type")))
//                        map.put("cash_fee_type", requestObjMap.get("cash_fee_type"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_fee")))
//                        map.put("coupon_fee", requestObjMap.get("coupon_fee"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_count")))
//                        map.put("coupon_count", requestObjMap.get("coupon_count"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_id_1")))
//                        map.put("coupon_id_1", requestObjMap.get("coupon_id_1"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("coupon_fee_1")))
//                        map.put("coupon_fee_1", requestObjMap.get("coupon_fee_1"));
//                    map.put("transaction_id", requestObjMap.get("transaction_id"));
//                    map.put("out_trade_no", requestObjMap.get("out_trade_no"));
//                    if (StringUtils.isNoneBlank(requestObjMap.get("attach")))
//                        map.put("attach", requestObjMap.get("attach"));
//                    map.put("time_end", requestObjMap.get("time_end"));
//                    log.info("结果字典是:{} 封装字典是:{}", requestObjMap, map);
//
//                    String signLocal = SignUtils.createSign(map, secretKey);
//
//                    log.info("输出结果是: 支付结果通知 sign:{},signLocal:{}", requestObjMap.get("sign"), signLocal);
//                    boolean isValidSign = StringUtils.equalsIgnoreCase(requestObjMap.get("sign"), signLocal);
//
//                    if (isValidSign) {
//                        onTradeSuccess(request, response, requestObjMap);
//                    } else {
//                        onInvalidSign(request, response, requestObjMap);
//                    }
//
//                } else {
//                    onTradeFailure(request, response, requestObjMap);
//                }
//
//            } else {
//                onTradeFailure(request, response, requestObjMap);
//            }
//
//        } catch (Exception e) {
//            onException(request, response, requestObjMap, e);
//        }
//    }
//
//    protected void onEmptyRequestBody(HttpServletResponse response) throws IOException {
//        XmlResponse.write(response, "SUCCESS", "OK");
//    }
//
//    protected void onInvalidSign(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap) throws IOException {
//        XmlResponse.write(response, "SUCCESS", "OK");
//    }
//
//    protected void onTradeSuccess(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap) throws IOException {
//        XmlResponse.write(response, "SUCCESS", "OK");
//    }
//
//    protected void onTradeFailure(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap) throws IOException {
//        XmlResponse.write(response, "SUCCESS", "OK");
//    }
//
//    protected void onException(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestObjMap, Exception ex) {
//        try {
//            XmlResponse.write(response, "SUCCESS", "OK");
//        } catch (IOException ignored) {
//        }
//    }
//
//    @Override
//    public void afterPropertiesSet() throws ServletException {
//        super.afterPropertiesSet();
//        Assert.notNull(configGroupManager, () -> null);
//        Assert.notNull(groupNameResolver, () -> null);
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
