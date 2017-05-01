package com.jinlele.model.dto;

import java.math.BigDecimal;

/**
 * Created by hfl on 2017/5/1 0001.
 * 退款金额model
 */
public class RefundModel {
    private BigDecimal refundAmout;//退款金额
    private BigDecimal totalAmount;//订单金额
    private String sn;//订单号
    private String type;//订单状态

    public RefundModel() {
    }

    public BigDecimal getRefundAmout() {
        return refundAmout;
    }
    public void setRefundAmout(BigDecimal refundAmout) {
        this.refundAmout = refundAmout;
    }
    public String getSn() {
        return sn;
    }
    public void setSn(String sn) {
        this.sn = sn;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
