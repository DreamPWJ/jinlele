package com.jinlele.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 服务订单接口
 */
public interface IServiceOrderService {

    /**
     * 根据订单号查询service及shoporder
     */
    Map<String, Object> getServiceProgressInfoByOrderno(String orderno);

    Map<String, Object> saveServiceOrder(Integer serviceId, Integer totalnum, String type, Integer userId, Integer storeId, String sendWay, String getWay, Double totalprice, Integer buyeraddresId, String products);

    /**
     * 创建维修订单
     */
    Map<String, Object> saveRepairOrder(List<Map<String, Object>> list);

    /**
     * 更新换款购物车信息
     */
    Map<String, Object> updateBarterCar(List<Map<String, Object>> list);

    /**
     * 删除换款购物车商品
     */
    Map<String, Object> deleteBarterCar(List<Map<String, Object>> list);

    Map<String, Object> deleteBarterCarts(List<Integer> list);

    /**
     * 更新维修订单
     */
    Map<String, Object> updateRepairOrder(List<Map<String, Object>> list);

    /**
     * 创建服务订单
     */
    Map<String, Object> createServiceOrder(List<Map<String, Object>> list);

    /**
     * 添加换款购物车
     */
    Map<String, Object> addBarterCart(List<Map<String, Object>> list);

    /**
     * 创建换款订单
     */
    Map<String, Object> createBarterOrder(List<Map<String, Object>> list);
    /**
     * 更新换款订单
     */
    int updateExchangeOrder(Integer userId, Double leftAmount, String orderno,Integer goodId, Integer goodchildId, Integer num,Double price);


    public int updateExchange(Integer userId, Double leftAmount, String orderno);

}
