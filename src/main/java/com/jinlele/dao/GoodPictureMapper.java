package com.jinlele.dao;

import com.jinlele.model.GoodPicture;

public interface GoodPictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodPicture record);

    int insertSelective(GoodPicture record);

    GoodPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodPicture record);

    int updateByPrimaryKey(GoodPicture record);
}