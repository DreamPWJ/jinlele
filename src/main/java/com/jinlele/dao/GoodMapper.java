package com.jinlele.dao;

import com.jinlele.model.Good;

import java.util.List;

public interface GoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Good record);

    int insertSelective(Good record);

    Good selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Good record);

    int updateByPrimaryKeyWithBLOBs(Good record);

    int updateByPrimaryKey(Good record);

    /**
     * 获取首页新品推荐
     *
     * @return
     */
    List getNewProducts();
}