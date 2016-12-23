package com.jinlele.dao;

import com.jinlele.model.ShopOrder;

public interface ShopOrderMapper {
    int deleteByPrimaryKey(String orderno);

    int insert(ShopOrder record);

    int insertSelective(ShopOrder record);

    ShopOrder selectByPrimaryKey(String orderno);

    int updateByPrimaryKeySelective(ShopOrder record);

    int updateByPrimaryKey(ShopOrder record);
}