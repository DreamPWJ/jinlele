package com.jinlele.model;

import java.util.Date;

public class CashApply {
    private Integer id;

    private Double money;

    private Integer userId;

    private String dealcode;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDealcode() {
        return dealcode;
    }

    public void setDealcode(String dealcode) {
        this.dealcode = dealcode == null ? null : dealcode.trim();
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