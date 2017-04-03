package com.jinlele.model;

import java.util.Date;

public class EvaluateDiamond {
    private Integer id;

    private Integer serviceId;

    private String materialTypeValue;

    private String purityValue;

    private String certificateValue;

    private String colorValue;

    private String cleanessValue;

    private String florescenceValue;

    private String cutValue;

    private String symmetryValue;

    private String polishValue;

    private String qualityValue;

    private Double materialWeight;

    private Double mainDiamondWeight;

    private Double secDiamondWeight;

    private Double materialPrice;

    private Double mainDiamondPrice;

    private Double secDiamondPrice;

    private Date createTime;

    private String deleteCode;

    public EvaluateDiamond(){}

    /**
     * 主石+副石+贵金属
     */
    public EvaluateDiamond(Integer serviceId,String materialTypeValue,String purityValue,String certificateValue,String colorValue,String cleanessValue,String florescenceValue,String cutValue,String symmetryValue,String polishValue,String qualityValue,Double materialPrice,Double mainDiamondPrice,Double secDiamondPrice,Double materialWeight,Double mainDiamondWeight,Double secDiamondWeight) {
        this.serviceId = serviceId;
        this.materialTypeValue = materialTypeValue;
        this.purityValue = purityValue;
        this.certificateValue = certificateValue;
        this.colorValue = colorValue;
        this.cleanessValue = cleanessValue;
        this.florescenceValue = florescenceValue;
        this.cutValue = cutValue;
        this.symmetryValue = symmetryValue;
        this.polishValue = polishValue;
        this.qualityValue = qualityValue;
        this.materialPrice = materialPrice;
        this.mainDiamondPrice = mainDiamondPrice;
        this.secDiamondPrice = secDiamondPrice;
        this.materialWeight = materialWeight;
        this.mainDiamondWeight = mainDiamondWeight;
        this.secDiamondWeight = secDiamondWeight;
    }

    /**
     * 副石+贵金属
     */
    public EvaluateDiamond(Integer serviceId,String materialTypeValue,String purityValue,String qualityValue,Double materialPrice,Double secDiamondPrice,Double materialWeight,Double mainDiamondWeight,Double secDiamondWeight){
        this.serviceId=serviceId;
        this.materialTypeValue = materialTypeValue;
        this.purityValue=purityValue;
        this.qualityValue=qualityValue;
        this.materialPrice=materialPrice;
        this.secDiamondPrice=secDiamondPrice;
        this.materialWeight=materialWeight;
        this.mainDiamondWeight=mainDiamondWeight;
        this.secDiamondWeight=secDiamondWeight;
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

    public String getCertificateValue() {
        return certificateValue;
    }

    public void setCertificateValue(String certificateValue) {
        this.certificateValue = certificateValue == null ? null : certificateValue.trim();
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue == null ? null : colorValue.trim();
    }

    public String getCleanessValue() {
        return cleanessValue;
    }

    public void setCleanessValue(String cleanessValue) {
        this.cleanessValue = cleanessValue == null ? null : cleanessValue.trim();
    }

    public String getFlorescenceValue() {
        return florescenceValue;
    }

    public void setFlorescenceValue(String florescenceValue) {
        this.florescenceValue = florescenceValue == null ? null : florescenceValue.trim();
    }

    public String getCutValue() {
        return cutValue;
    }

    public void setCutValue(String cutValue) {
        this.cutValue = cutValue == null ? null : cutValue.trim();
    }

    public String getSymmetryValue() {
        return symmetryValue;
    }

    public void setSymmetryValue(String symmetryValue) {
        this.symmetryValue = symmetryValue == null ? null : symmetryValue.trim();
    }

    public String getPolishValue() {
        return polishValue;
    }

    public void setPolishValue(String polishValue) {
        this.polishValue = polishValue == null ? null : polishValue.trim();
    }

    public String getQualityValue() {
        return qualityValue;
    }

    public void setQualityValue(String qualityValue) {
        this.qualityValue = qualityValue == null ? null : qualityValue.trim();
    }

    public Double getMaterialWeight() {
        return materialWeight;
    }

    public void setMaterialWeight(Double materialWeight) {
        this.materialWeight = materialWeight;
    }

    public Double getMainDiamondWeight() {
        return mainDiamondWeight;
    }

    public void setMainDiamondWeight(Double mainDiamondWeight) {
        this.mainDiamondWeight = mainDiamondWeight;
    }

    public Double getSecDiamondWeight() {
        return secDiamondWeight;
    }

    public void setSecDiamondWeight(Double secDiamondWeight) {
        this.secDiamondWeight = secDiamondWeight;
    }

    public Double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(Double materialPrice) {
        this.materialPrice = materialPrice;
    }

    public Double getMainDiamondPrice() {
        return mainDiamondPrice;
    }

    public void setMainDiamondPrice(Double mainDiamondPrice) {
        this.mainDiamondPrice = mainDiamondPrice;
    }

    public Double getSecDiamondPrice() {
        return secDiamondPrice;
    }

    public void setSecDiamondPrice(Double secDiamondPrice) {
        this.secDiamondPrice = secDiamondPrice;
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