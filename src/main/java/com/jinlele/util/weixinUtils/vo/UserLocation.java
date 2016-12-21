package com.jinlele.util.weixinUtils.vo;

/**
 * 用户地理位置信息的实体类
 */
public class UserLocation {

    //用户的OpenId,唯一值
    private String openID;
    //用户发送过来的地理位置的经度值
    private String lng;
    //用户发送过来的地理位置的纬度
    private String lat;
    //经过百度坐标转化的经度值
    private String bd09Lng;
    //经过百度坐标转化的纬度值
    private String bd09Lat;


    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBd09Lng() {
        return bd09Lng;
    }

    public void setBd09Lng(String bd09Lng) {
        this.bd09Lng = bd09Lng;
    }

    public String getBd09Lat() {
        return bd09Lat;
    }

    public void setBd09Lat(String bd09Lat) {
        this.bd09Lat = bd09Lat;
    }
}
