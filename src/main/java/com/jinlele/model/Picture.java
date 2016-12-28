package com.jinlele.model;

import java.util.Date;

/**
 * 图片 实体类 用于存储5大类的照片 和 收货地址照片等
 */
public class Picture {
    private Integer id;

    private String url;

    private String catogory;

    private Integer userId;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public Picture() {
    }

    public Picture(String url, Integer userId) {
        this.url = url;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getCatogory() {
        return catogory;
    }

    public void setCatogory(String catogory) {
        this.catogory = catogory == null ? null : catogory.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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