package com.jinlele.model;

import java.util.Date;

public class ShopOrderGood {
    private Integer id;

    private String shoporderNo;

    private Integer goodchildId;

    private Integer goodId;

    private Double unitprice;

    private Integer num;

    private Integer commentId;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public ShopOrderGood() {
    }

    public ShopOrderGood(String shoporderNo, Integer goodchildId, Integer goodId,Double unitprice, Integer num, String deletecode) {
        this.shoporderNo = shoporderNo;
        this.goodchildId = goodchildId;
        this.goodId = goodId;
        this.unitprice = unitprice;
        this.num = num;
        this.deletecode = deletecode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShoporderNo() {
        return shoporderNo;
    }

    public void setShoporderNo(String shoporderNo) {
        this.shoporderNo = shoporderNo == null ? null : shoporderNo.trim();
    }

    public Integer getGoodchildId() {
        return goodchildId;
    }

    public void setGoodchildId(Integer goodchildId) {
        this.goodchildId = goodchildId;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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