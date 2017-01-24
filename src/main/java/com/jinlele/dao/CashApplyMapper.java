package com.jinlele.dao;

import com.jinlele.model.CashApply;

public interface CashApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CashApply record);

    int insertSelective(CashApply record);

    CashApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CashApply record);

    int updateByPrimaryKey(CashApply record);
}