package com.jinlele.dao;

import com.jinlele.model.ShopOrderGood;

import java.util.List;
import java.util.Map;

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


    /**
     * 根据订单号 查询服务类订单详情
     * @param orderno
     * @return
     */
    Map<String ,Object> selectServiceOrderDetailByOrderno(String orderno);

    int updateByPrimaryKeySelective(ShopOrderGood record);

    int updateByPrimaryKey(ShopOrderGood record);
    //根据订单与gcid修改评论id
    int updateByOrderNoGcid(ShopOrderGood record);
}