package com.jinlele.model;

import java.util.Date;

public class ExchangeChart {
    private Integer id;

    private Integer userId;

    private Integer goodchildId;

    private Integer serviceId;

    private Integer goodId;

    private Integer num;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    private Integer checked;

    public ExchangeChart(){}

    public ExchangeChart(Integer serviceId, Integer goodId, Integer goodchildId, Integer num, Integer checked){
        this.serviceId=serviceId;
        this.goodId=goodId;
        this.goodchildId=goodchildId;
        this.num=num;
        this.checked=checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodchildId() {
        return goodchildId;
    }

    public void setGoodchildId(Integer goodchildId) {
        this.goodchildId = goodchildId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }
}