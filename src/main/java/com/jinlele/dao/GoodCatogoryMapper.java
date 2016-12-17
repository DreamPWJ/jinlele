package com.jinlele.dao;

import com.jinlele.model.GoodCatogory;

import java.util.List;

public interface GoodCatogoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodCatogory record);

    int insertSelective(GoodCatogory record);

    GoodCatogory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodCatogory record);

    int updateByPrimaryKey(GoodCatogory record);

    /**
     *   获取首页一级分类
     * @return
     */
     List getFirstCatogory();
}