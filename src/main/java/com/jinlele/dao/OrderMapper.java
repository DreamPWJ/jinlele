package com.jinlele.dao;

import com.jinlele.model.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(String orderno);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String orderno);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}