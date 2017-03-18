package com.jinlele.dao;

import com.jinlele.model.ReturnApply;

import java.util.List;

public interface ReturnApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ReturnApply record);

    int insertSelective(ReturnApply record);

    ReturnApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReturnApply record);

    int updateByPrimaryKey(ReturnApply record);

    List getReturnApplyInfo(String orderno);
}