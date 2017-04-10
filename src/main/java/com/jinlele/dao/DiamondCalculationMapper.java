package com.jinlele.dao;

import com.jinlele.model.DiamondCalculation;

import java.util.Map;

public interface DiamondCalculationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DiamondCalculation record);

    int insertSelective(DiamondCalculation record);

    DiamondCalculation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DiamondCalculation record);

    int updateByPrimaryKey(DiamondCalculation record);
    //根据总量等级获取主石价格信息
    Map<String,Object> getMainPriceInfo(Double weightLevel);
}