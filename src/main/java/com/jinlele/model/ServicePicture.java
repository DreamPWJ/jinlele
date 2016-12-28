package com.jinlele.model;

public class ServicePicture {
    private Integer id;

    private Integer serviceId;

    private Integer pictureId;

    private String type;


    public ServicePicture() {
    }

    public ServicePicture(Integer serviceId, Integer pictureId, String type) {
        this.serviceId = serviceId;
        this.pictureId = pictureId;
        this.type = type;
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

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}