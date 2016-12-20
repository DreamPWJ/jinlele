package com.jinlele.service.interfaces;

import com.jinlele.model.ShoppingCart;

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

    //添加购物车
    int insertSelective(ShoppingCart record);
}