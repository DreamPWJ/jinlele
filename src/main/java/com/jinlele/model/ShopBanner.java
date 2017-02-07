package com.jinlele.model;

import java.util.Date;

public class ShopBanner {
    private Integer id;

    private String imgurl;

    private String title;

    private Integer positon;

    private String shown;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getPositon() {
        return positon;
    }

    public void setPositon(Integer positon) {
        this.positon = positon;
    }

    public String getShown() {
        return shown;
    }

    public void setShown(String shown) {
        this.shown = shown == null ? null : shown.trim();
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