package com.jinlele.model;

public class ServiceGood {
    private Integer id;

    private String orderno;

    private Integer goodId;

    private Integer goodchildId;

    private Integer buynum;

    private Double unitPrice;

    private Double leftAmount;

    public ServiceGood(String orderno,Integer goodId,Integer goodchildId,Integer num,Double price,Double amount ){
        this.orderno=orderno;
        this.goodId=goodId;
        this.goodchildId=goodchildId;
        this.buynum=num;
        this.unitPrice=price;
        this.leftAmount=amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Integer getGoodchildId() {
        return goodchildId;
    }

    public void setGoodchildId(Integer goodchildId) {
        this.goodchildId = goodchildId;
    }

    public Integer getBuynum() {
        return buynum;
    }

    public void setBuynum(Integer buynum) {
        this.buynum = buynum;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(Double leftAmount) {
        this.leftAmount = leftAmount;
    }
}