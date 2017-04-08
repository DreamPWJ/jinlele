package com.jinlele.model;

import java.util.Date;

public class GoodChild {
    private Integer id;

    private Integer goodId;

    private String color;

    private String weight;

    private String material;

    private String mainstorespec;

    private String handsize;

    private String necklaceLen;

    private String braceletLen;

    private Double exprice;

    private Double price;

    private Integer stocknumber;

    private String imgurl;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public String getMainstorespec() {
        return mainstorespec;
    }

    public void setMainstorespec(String mainstorespec) {
        this.mainstorespec = mainstorespec == null ? null : mainstorespec.trim();
    }

    public String getHandsize() {
        return handsize;
    }

    public void setHandsize(String handsize) {
        this.handsize = handsize == null ? null : handsize.trim();
    }

    public String getNecklaceLen() {
        return necklaceLen;
    }

    public void setNecklaceLen(String necklaceLen) {
        this.necklaceLen = necklaceLen == null ? null : necklaceLen.trim();
    }

    public String getBraceletLen() {
        return braceletLen;
    }

    public void setBraceletLen(String braceletLen) {
        this.braceletLen = braceletLen == null ? null : braceletLen.trim();
    }

    public Double getExprice() {
        return exprice;
    }

    public void setExprice(Double exprice) {
        this.exprice = exprice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStocknumber() {
        return stocknumber;
    }

    public void setStocknumber(Integer stocknumber) {
        this.stocknumber = stocknumber;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
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