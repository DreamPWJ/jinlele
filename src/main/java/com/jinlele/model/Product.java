package com.jinlele.model;

import java.util.Date;

public class Product {
    private Integer id;

    private Integer catogoryId;

    private String type;

    private Integer serviceId;

    private Integer commentId;

    private Integer num;

    private String memo;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public Product() {
    }

    public Product(Integer catogoryId, String type, Integer serviceId, Integer num, String memo) {
        this.catogoryId = catogoryId;
        this.type = type;
        this.serviceId = serviceId;
        this.num = num;
        this.memo = memo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCatogoryId() {
        return catogoryId;
    }

    public void setCatogoryId(Integer catogoryId) {
        this.catogoryId = catogoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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