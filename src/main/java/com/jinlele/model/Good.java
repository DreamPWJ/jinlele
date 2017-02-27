package com.jinlele.model;

import java.util.Date;

public class Good {
    private Integer id;

    private String title;

    private String bannerurl;

    private String hotimgurl;

    private String shortinfo;

    private Double price;

    private Double oldprice;

    private Integer salesvol;

    private Integer canchange;

    private Integer stocknum;

    private Integer position;

    private Integer comments;

    private String ishotcode;

    private String showcode;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl == null ? null : bannerurl.trim();
    }

    public String getHotimgurl() {
        return hotimgurl;
    }

    public void setHotimgurl(String hotimgurl) {
        this.hotimgurl = hotimgurl == null ? null : hotimgurl.trim();
    }

    public String getShortinfo() {
        return shortinfo;
    }

    public void setShortinfo(String shortinfo) {
        this.shortinfo = shortinfo == null ? null : shortinfo.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOldprice() {
        return oldprice;
    }

    public void setOldprice(Double oldprice) {
        this.oldprice = oldprice;
    }

    public Integer getSalesvol() {
        return salesvol;
    }

    public void setSalesvol(Integer salesvol) {
        this.salesvol = salesvol;
    }

    public Integer getCanchange() {
        return canchange;
    }

    public void setCanchange(Integer canchange) {
        this.canchange = canchange;
    }

    public Integer getStocknum() {
        return stocknum;
    }

    public void setStocknum(Integer stocknum) {
        this.stocknum = stocknum;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getIshotcode() {
        return ishotcode;
    }

    public void setIshotcode(String ishotcode) {
        this.ishotcode = ishotcode == null ? null : ishotcode.trim();
    }

    public String getShowcode() {
        return showcode;
    }

    public void setShowcode(String showcode) {
        this.showcode = showcode == null ? null : showcode.trim();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}