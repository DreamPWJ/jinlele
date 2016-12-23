package com.jinlele.dao;

import com.jinlele.model.ShopOrderGood;

public interface ShopOrderGoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopOrderGood record);

    int insertSelective(ShopOrderGood record);

    ShopOrderGood selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopOrderGood record);

    int updateByPrimaryKey(ShopOrderGood record);
}