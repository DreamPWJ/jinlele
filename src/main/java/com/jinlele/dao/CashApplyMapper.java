package com.jinlele.dao;

import com.jinlele.model.CashApply;

import java.util.List;
import java.util.Map;

public interface CashApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CashApply record);

    int insertSelective(CashApply record);

    CashApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CashApply record);

    int updateByPrimaryKey(CashApply record);

    List<Map<String , Object>> getAllRecords(Integer userId);
}