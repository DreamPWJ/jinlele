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
     * 获取首页商品一级分类
     */
    List getFirstCatogory();

    /**
     * 获取首页商品二级分类
     */
    List getSecondCatogory();
}