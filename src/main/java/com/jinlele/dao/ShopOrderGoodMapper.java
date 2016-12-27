package com.jinlele.dao;

import com.jinlele.model.ShopOrderGood;

import java.util.List;

public interface ShopOrderGoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopOrderGood record);

    int insertSelective(ShopOrderGood record);

    ShopOrderGood selectByPrimaryKey(Integer id);
    /**
     * 根据订单号查询订单详情
     * @param orderno
     * @return
     */
    List selectOrderDetailByOrderno(String orderno);

    int updateByPrimaryKeySelective(ShopOrderGood record);

    int updateByPrimaryKey(ShopOrderGood record);
}