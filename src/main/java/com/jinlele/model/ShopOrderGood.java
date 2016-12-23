package com.jinlele.model;

import java.util.Date;

public class ShopOrderGood {
    private Integer id;

    private String shoporderNo;

    private Integer goodchildId;

    private Integer goodId;

    private String comment;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

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