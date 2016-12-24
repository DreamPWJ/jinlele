package com.jinlele.model;

import java.util.Date;

public class ShopOrder {
    private String orderno;

    private Double totalprice;

    private Integer totalnum;

    private Double freightprice;

    private Double actualpayprice;

    private String logisticsno;

    private Integer userId;

    private Integer storeId;

    private String shoporderstatuscode;

    private Integer receiptAddressId;

    private String comment;

    private String deletecode;

    private Date createTime;

    private Date updateTime;


    public ShopOrder() {
    }

    public ShopOrder(String orderno, Double totalprice, Integer totalnum, Integer userId, Integer storeId, Integer receiptAddressId,String deletecode) {
        this.orderno = orderno;
        this.totalprice = totalprice;
        this.totalnum = totalnum;
        this.userId = userId;
        this.storeId = storeId;
        this.receiptAddressId = receiptAddressId;
        this.deletecode = deletecode;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }

    public Integer getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(Integer totalnum) {
        this.totalnum = totalnum;
    }

    public Double getFreightprice() {
        return freightprice;
    }

    public void setFreightprice(Double freightprice) {
        this.freightprice = freightprice;
    }

    public Double getActualpayprice() {
        return actualpayprice;
    }

    public void setActualpayprice(Double actualpayprice) {
        this.actualpayprice = actualpayprice;
    }

    public String getLogisticsno() {
        return logisticsno;
    }

    public void setLogisticsno(String logisticsno) {
        this.logisticsno = logisticsno == null ? null : logisticsno.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getShoporderstatuscode() {
        return shoporderstatuscode;
    }

    public void setShoporderstatuscode(String shoporderstatuscode) {
        this.shoporderstatuscode = shoporderstatuscode == null ? null : shoporderstatuscode.trim();
    }

    public Integer getReceiptAddressId() {
        return receiptAddressId;
    }

    public void setReceiptAddressId(Integer receiptAddressId) {
        this.receiptAddressId = receiptAddressId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getDeletecode() {
        return deletecode;
    }

    public void setDeletecode(String deletecode) {
        this.deletecode = deletecode == null ? null : deletecode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}