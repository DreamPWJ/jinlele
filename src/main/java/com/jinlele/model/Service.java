package com.jinlele.model;

import java.util.Date;

/**
 * 5大服务 实体类
 */

public class Service {
    private Integer id;

    private Double price;

    private Double aturalprice;

    private Integer userId;

    private String descrip;

    private Integer storeId;

    private String sendway;

    private String getway;

    private String serviceaddress;

    private String servciephone;

    private String servicelinkman;

    private String checkreport;

    private String buyertransno;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public Service() {
    }

    public Service(Double price, Integer userId, String descrip, Integer storeId) {
        this.price = price;
        this.userId = userId;
        this.descrip = descrip;
        this.storeId = storeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAturalprice() {
        return aturalprice;
    }

    public void setAturalprice(Double aturalprice) {
        this.aturalprice = aturalprice;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip == null ? null : descrip.trim();
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getSendway() {
        return sendway;
    }

    public void setSendway(String sendway) {
        this.sendway = sendway == null ? null : sendway.trim();
    }

    public String getGetway() {
        return getway;
    }

    public void setGetway(String getway) {
        this.getway = getway == null ? null : getway.trim();
    }

    public String getServiceaddress() {
        return serviceaddress;
    }

    public void setServiceaddress(String serviceaddress) {
        this.serviceaddress = serviceaddress == null ? null : serviceaddress.trim();
    }

    public String getServciephone() {
        return servciephone;
    }

    public void setServciephone(String servciephone) {
        this.servciephone = servciephone == null ? null : servciephone.trim();
    }

    public String getServicelinkman() {
        return servicelinkman;
    }

    public void setServicelinkman(String servicelinkman) {
        this.servicelinkman = servicelinkman == null ? null : servicelinkman.trim();
    }

    public String getCheckreport() {
        return checkreport;
    }

    public void setCheckreport(String checkreport) {
        this.checkreport = checkreport == null ? null : checkreport.trim();
    }

    public String getBuyertransno() {
        return buyertransno;
    }

    public void setBuyertransno(String buyertransno) {
        this.buyertransno = buyertransno == null ? null : buyertransno.trim();
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