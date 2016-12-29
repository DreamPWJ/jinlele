package com.jinlele.dao;

import com.jinlele.model.GoodChild;

public interface GoodChildMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodChild record);

    int insertSelective(GoodChild record);

    GoodChild selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodChild record);

    int updateByPrimaryKey(GoodChild record);
}