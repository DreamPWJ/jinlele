package com.jinlele.dao;

import com.jinlele.model.ShopOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ShopOrderMapper {
    int deleteByPrimaryKey(String orderno);

    int insert(ShopOrder record);

    int insertSelective(ShopOrder record);

    ShopOrder selectByPrimaryKey(String orderno);

    int updateByPrimaryKeySelective(ShopOrder record);

    int updateByPrimaryKey(ShopOrder record);

    Map<String , Object> selectOrderInfoByOrderno(String orderno);

    Map<String , Object> findReceiptServiceByOrderno(@Param("orderno")String orderno);

    List findAllexpressCompanies();
}