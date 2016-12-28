package com.jinlele.dao;

import com.jinlele.model.ServicePicture;

public interface ServicePictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ServicePicture record);

    int insertSelective(ServicePicture record);

    ServicePicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ServicePicture record);

    int updateByPrimaryKey(ServicePicture record);
}