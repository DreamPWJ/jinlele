package com.jinlele.dao;

import com.jinlele.model.ShopOrderGood;

import java.util.List;

public interface ShopOrderGoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopOrderGood record);

    int insertSelective(ShopOrderGood record);

    ShopOrderGood selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopOrderGood record);

    int updateByPrimaryKey(ShopOrderGood record);
    /**
     * 根据订单号查询订单详情
     * @param orderno
     * @return
     */
    List selectOrderDetailByOrderno(String orderno);
    //根据订单与gcid修改评论id
    int updateByOrderNoGcid(ShopOrderGood record);
}