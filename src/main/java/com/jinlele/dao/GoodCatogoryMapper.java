package com.jinlele.dao;

import com.jinlele.model.GoodCatogory;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 获取一级分类下的二级分类
     * @param pid 一级分类id
     * @return  二级分类集合
     */
    List getSecondCatogaryByPid(@Param("pid")Integer pid);

    /**
     * 不分页的
     * 获取一级分类下的二级分类
     * @param pid 一级分类id
     * @return  二级分类集合
     */
    List getSecondCatogByPid(@Param("pid")Integer pid);



}