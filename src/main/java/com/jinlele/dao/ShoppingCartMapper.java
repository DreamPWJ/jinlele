package com.jinlele.dao;

import com.jinlele.model.ShoppingCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingCart record);

    int insertSelective(ShoppingCart record);

    ShoppingCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShoppingCart record);

    int updateByPrimaryKey(ShoppingCart record);

    //查询某个用户的购物车商品总数量
    int getShopcharTotalNum(@Param("userId") Integer userId);

    //查询某个用户，某个商品的信息
    //List getShopcharInfo(@Param("userId")Integer userId , @Param("goodId")Integer goodId);
    List getShopcharInfo(ShoppingCart record);
}