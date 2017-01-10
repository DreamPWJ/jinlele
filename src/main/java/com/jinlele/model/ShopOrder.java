package com.jinlele.model;

import java.util.Date;

public class ShopOrder {
    private String orderno;

    private Double totalprice;

    private Integer totalnum;

    private Double freightprice;

    private Double actualpayprice;

    private String userlogisticsnocomp;

    private String userlogisticsno;

    private String logisticsnocomp;

    private String logisticsno;

    private Integer userId;

    private Integer storeId;

    private String type;

    private String qrcodeUrl;

    private Integer descriplevel;

    private String shoporderstatuscode;

    private Integer receiptAddressId;

    private String comment;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    private String payResult;

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

    public String getUserlogisticsnocomp() {
        return userlogisticsnocomp;
    }

    public void setUserlogisticsnocomp(String userlogisticsnocomp) {
        this.userlogisticsnocomp = userlogisticsnocomp == null ? null : userlogisticsnocomp.trim();
    }

    public String getUserlogisticsno() {
        return userlogisticsno;
    }

    public void setUserlogisticsno(String userlogisticsno) {
        this.userlogisticsno = userlogisticsno == null ? null : userlogisticsno.trim();
    }

    public String getLogisticsnocomp() {
        return logisticsnocomp;
    }

    public void setLogisticsnocomp(String logisticsnocomp) {
        this.logisticsnocomp = logisticsnocomp == null ? null : logisticsnocomp.trim();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl == null ? null : qrcodeUrl.trim();
    }

    public Integer getDescriplevel() {
        return descriplevel;
    }

    public void setDescriplevel(Integer descriplevel) {
        this.descriplevel = descriplevel;
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

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult == null ? null : payResult.trim();
    }

    public ShopOrder() {
    }
    public ShopOrder(String orderno,Double actualpayprice,String orderstatus,String payresult,Date endtime) {
        this.orderno = orderno;
        this.actualpayprice=actualpayprice;
        this.shoporderstatuscode = orderstatus;
        this.payResult = payresult;
        this.updateTime=endtime;
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


    public ShopOrder(String orderno,Integer totalnum ,Double totalprice, Double actualpayprice, Integer userId, Integer storeId, String type, String shoporderstatuscode, Integer receiptAddressId ,Date createTime) {
        this.orderno = orderno;
        this.totalnum = totalnum;
        this.totalprice = totalprice;
        this.actualpayprice = actualpayprice;
        this.userId = userId;
        this.storeId = storeId;
        this.type = type;
        this.shoporderstatuscode = shoporderstatuscode;
        this.receiptAddressId = receiptAddressId;
        this.createTime = createTime;
    }
}