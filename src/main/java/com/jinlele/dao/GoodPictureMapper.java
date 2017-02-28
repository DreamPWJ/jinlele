package com.jinlele.dao;

import com.jinlele.model.GoodPicture;

import java.util.List;

public interface GoodPictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodPicture record);

    int insertSelective(GoodPicture record);

    GoodPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodPicture record);

    int updateByPrimaryKey(GoodPicture record);

    /**
     * 获取商品详情图片
     * @param goodId
     * @return
     */
    List getGoodPicture(int goodId);
}