package com.jinlele.dao;

import com.jinlele.model.MetalCalculation;

import java.util.List;

public interface MetalCalculationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MetalCalculation record);

    int insertSelective(MetalCalculation record);

    MetalCalculation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MetalCalculation record);

    int updateByPrimaryKey(MetalCalculation record);
    //查询子集
    List getSubSet(String category,Integer pid);
    //根据类型及纯度查询实体计算数据
    MetalCalculation selectByUQ(String type,String series);
    //获取材质
    List getMaterial();
    //获取品质
    List getQuality();
}