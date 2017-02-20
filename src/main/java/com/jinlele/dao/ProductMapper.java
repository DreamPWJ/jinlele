package com.jinlele.dao;

import com.jinlele.model.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 根据订单号获取服务类订单中产品组信息
     */
    List getServiceOrderProductsInfoByOrderno(String orderno);
}