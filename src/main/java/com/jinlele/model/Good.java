package com.jinlele.model;

import java.util.Date;

public class Good {
    private Integer id;

    private String title;

    private Double saleprice;

    private String bannerurl;

    private Integer categoryId;

    private Integer salesvol;

    private String shortinfo;

    private Double weight;

    private String purity;

    private String material;

    private Integer canchange;

    private String kind;

    private Integer stocknum;

    private Double discprice;

    private Integer comments;

    private Double price;

    private String colorcode;

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

    public Double getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(Double saleprice) {
        this.saleprice = saleprice;
    }

    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl == null ? null : bannerurl.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSalesvol() {
        return salesvol;
    }

    public void setSalesvol(Integer salesvol) {
        this.salesvol = salesvol;
    }

    public String getShortinfo() {
        return shortinfo;
    }

    public void setShortinfo(String shortinfo) {
        this.shortinfo = shortinfo == null ? null : shortinfo.trim();
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity == null ? null : purity.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public Integer getCanchange() {
        return canchange;
    }

    public void setCanchange(Integer canchange) {
        this.canchange = canchange;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind == null ? null : kind.trim();
    }

    public Integer getStocknum() {
        return stocknum;
    }

    public void setStocknum(Integer stocknum) {
        this.stocknum = stocknum;
    }

    public Double getDiscprice() {
        return discprice;
    }

    public void setDiscprice(Double discprice) {
        this.discprice = discprice;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode == null ? null : colorcode.trim();
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