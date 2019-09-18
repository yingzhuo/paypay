/*
 ____
|  _ \ __ _ _   _ _ __   __ _ _   _
| |_) / _` | | | | '_ \ / _` | | | |
|  __/ (_| | |_| | |_) | (_| | |_| |
|_|   \__;_|\__; | .__/ \__;_|\__; |
            |___/|_|          |___/

 https://github.com/yingzhuo/paypay
*/
package com.github.yingzhuo.paypay.alipay.model;

import lombok.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author 白宝鹏
 * @author 应卓
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyParams implements Serializable {

    public static NotifyParams parse(HttpServletRequest request) {
        return NotifyParams.builder()
                .notifyId(request.getParameter("notify_time"))
                .notifyType(request.getParameter("notify_type"))
                .notifyId(request.getParameter("notify_id"))
                .appId(request.getParameter("app_id"))
                .charset(request.getParameter("charset"))
                .version(request.getParameter("version"))
                .signType(request.getParameter("sign_type"))
                .sign(request.getParameter("sign"))
                .tradeNo(request.getParameter("trade_no"))
                .outTradeNo(request.getParameter("out_trade_no"))
                .outBizNo(request.getParameter("out_biz_no"))
                .buyerId(request.getParameter("buyer_id"))
                .buyerLogonId(request.getParameter("buyer_logon_id"))
                .sellerId(request.getParameter("seller_id"))
                .sellerEmail(request.getParameter("seller_email"))
                .tradeStatus(request.getParameter("trade_status"))
                .totalAmount(request.getParameter("total_amount"))
                .receiptAmount(request.getParameter("receipt_amount"))
                .invoiceAmount(request.getParameter("invoice_amount"))
                .buyerPayAmount(request.getParameter("buyer_pay_amount"))
                .pointAmount(request.getParameter("point_amount"))
                .refundFee(request.getParameter("refund_fee"))
                .subject(request.getParameter("subject"))
                .body(request.getParameter("body"))
                .gmtCreate(request.getParameter("gmt_create"))
                .gmtPayment(request.getParameter("gmt_payment"))
                .gmtRefund(request.getParameter("gmt_refund"))
                .gmtClose(request.getParameter("gmt_close"))
                .fundBillList(request.getParameter("fund_bill_list"))
                .passbackParams(request.getParameter("passback_params"))
                .voucherDetailList(request.getParameter("voucher_detail_list"))
                .build();
    }

    private String notifyTime;//1 通知的发送时间
    private String notifyType;//1 通知的类型 trade_status_sync
    private String notifyId;//1 通知校验ID
    private String appId;//1 支付宝分配给开发者的应用Id
    private String charset;//1 编码格式，如utf-8、gbk、gb2312等
    private String version;//1 接口版本
    private String signType;//1 签名类型:RSA2
    private String sign;//1 签名
    private String tradeNo; //1 支付宝交易凭证号
    private String outTradeNo; //1 商户订单号 交易单号
    private String outBizNo; //0 商户业务号
    private String buyerId; //0 买家支付宝用户号 以2088开头的纯16位数字
    private String buyerLogonId; //0 买家支付宝账号
    private String sellerId;//0 卖家支付宝用户号
    private String sellerEmail;//0 卖家支付宝账号
    private String tradeStatus;//0 交易目前所处的状态，
    private String totalAmount;//0 本次交易支付的订单金额，单位为人民币（元）
    private String receiptAmount;//0 商家在交易中实际收到的款项，单位为元
    private String invoiceAmount;//0 用户在交易中支付的可开发票的金额
    private String buyerPayAmount;//0 用户在交易中支付的金额
    private String pointAmount;//0 使用集分宝支付的金额
    private String refundFee;//0 总退款金额
    private String subject;//0 订单标题
    private String body;//0 商品描述
    private String gmtCreate; //0 交易创建时间
    private String gmtPayment; //0 付款时间
    private String gmtRefund; //0 交易退款时间
    private String gmtClose; //0 交易结束时间
    private String fundBillList; //0 支付金额信息
    private String passbackParams; //0 公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。
    private String voucherDetailList; //0 优惠券信息

}
