package com.jinlele.dao;

import com.jinlele.model.DiamondCalculation;

public interface DiamondCalculationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DiamondCalculation record);

    int insertSelective(DiamondCalculation record);

    DiamondCalculation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DiamondCalculation record);

    int updateByPrimaryKey(DiamondCalculation record);
}