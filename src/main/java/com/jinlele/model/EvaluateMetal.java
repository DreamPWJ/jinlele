package com.jinlele.model;

import java.util.Date;

public class EvaluateMetal {
    private Integer id;

    private Integer serviceId;

    private String materialTypeValue;

    private String purityValue;

    private Double materialWeight;

    private Double unitprice;

    private Double depreciation;

    private Double materialPrice;

    private Date createTime;

    private String deleteCode;

    public EvaluateMetal() {
    }

    public EvaluateMetal(Integer serviceId,String materialTypeValue,String purityValue,Double materialWeight,Double materialPrice,Double unitprice,Double depreciation){
        this.serviceId=serviceId;
        this.materialTypeValue=materialTypeValue;
        this.purityValue=purityValue;
        this.materialWeight=materialWeight;
        this.materialPrice=materialPrice;
        this.unitprice=unitprice;
        this.depreciation=depreciation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getMaterialTypeValue() {
        return materialTypeValue;
    }

    public void setMaterialTypeValue(String materialTypeValue) {
        this.materialTypeValue = materialTypeValue == null ? null : materialTypeValue.trim();
    }

    public String getPurityValue() {
        return purityValue;
    }

    public void setPurityValue(String purityValue) {
        this.purityValue = purityValue == null ? null : purityValue.trim();
    }

    public Double getMaterialWeight() {
        return materialWeight;
    }

    public void setMaterialWeight(Double materialWeight) {
        this.materialWeight = materialWeight;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public Double getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(Double depreciation) {
        this.depreciation = depreciation;
    }

    public Double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(Double materialPrice) {
        this.materialPrice = materialPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeleteCode() {
        return deleteCode;
    }

    public void setDeleteCode(String deleteCode) {
        this.deleteCode = deleteCode == null ? null : deleteCode.trim();
    }
}