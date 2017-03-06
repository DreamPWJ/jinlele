package com.jinlele.dao;

import com.jinlele.model.DiamondParmCalculation;

public interface DiamondParmCalculationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DiamondParmCalculation record);

    int insertSelective(DiamondParmCalculation record);

    DiamondParmCalculation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DiamondParmCalculation record);

    int updateByPrimaryKey(DiamondParmCalculation record);

    Double getRate(String type,String spec, Integer dcid);
}