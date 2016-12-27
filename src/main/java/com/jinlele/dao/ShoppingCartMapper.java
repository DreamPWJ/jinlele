package com.jinlele.dao;

import com.jinlele.model.ShoppingCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    Map findCartIdAndGoodId(@Param("goodchildId") Integer goodchildId);

    //根据用户id商品子id删除
    int deleteByUserIdGcid(int userid, int gcid);
}