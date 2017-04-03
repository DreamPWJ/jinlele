package com.jinlele.dao;

import com.jinlele.model.EvaluateDiamond;

public interface EvaluateDiamondMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EvaluateDiamond record);

    int insertSelective(EvaluateDiamond record);

    EvaluateDiamond selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EvaluateDiamond record);

    int updateByPrimaryKey(EvaluateDiamond record);
}