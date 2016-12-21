package com.jinlele.service.interfaces;

import com.jinlele.model.ShoppingCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 */
public interface IShoppingCartService {
    /**
     * 根据用户id分页查找购物车数据
     * @param pagenow
     * @param userId
     * @return
     */
    Map<String, Object> getShoppingCartPaging(int pagenow, int userId);

    //插入数据到购物车
    int insertSelective(ShoppingCart record);

    //查询某个用户的购物车商品总数量
    int getShopcharTotalNum(@Param("userId") Integer userId);


    //查询某个用户，某个商品的信息
    //List getShopcharInfo(@Param("userId")Integer userId , @Param("goodId")Integer goodId);
    List getShopcharInfo(ShoppingCart record);

    //添加购物车
    public int addShoppingCart(ShoppingCart cart);

    int updateByPrimaryKeySelective(ShoppingCart record);

    int updateByPrimaryKey(ShoppingCart record);
}
