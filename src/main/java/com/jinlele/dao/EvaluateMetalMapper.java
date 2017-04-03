package com.jinlele.dao;

import com.jinlele.model.EvaluateMetal;

public interface EvaluateMetalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EvaluateMetal record);

    int insertSelective(EvaluateMetal record);

    EvaluateMetal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EvaluateMetal record);

    int updateByPrimaryKey(EvaluateMetal record);
}