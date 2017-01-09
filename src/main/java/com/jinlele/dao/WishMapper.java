package com.jinlele.dao;

import com.jinlele.model.Wish;

public interface WishMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Wish record);

    int insertSelective(Wish record);

    Wish selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Wish record);

    int updateByPrimaryKey(Wish record);
}