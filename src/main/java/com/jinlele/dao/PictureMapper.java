package com.jinlele.dao;

import com.jinlele.model.Picture;

import java.util.List;

public interface PictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Picture record);

    int insertSelective(Picture record);

    Picture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);

    /**
     * 根据订单号获取服务类订单中图片组信息
     */
    List getServiceOrderPicturesInfoByOrderno(String orderno);
}