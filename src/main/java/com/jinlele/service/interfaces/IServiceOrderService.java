package com.jinlele.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 服务订单接口
 */
public interface IServiceOrderService {

    Map<String, Object> saveServiceOrder(Integer serviceId, Integer totalnum, String type, Integer userId, Integer storeId, String sendWay, String getWay, Double totalprice, Integer buyeraddresId, String products);

    /**
     * 创建维修订单
     */
    Map<String, Object> saveRepairOrder(List<Map<String, Object>> list);

    /**
     * 更新维修订单
     */
    Map<String, Object> updateRepairOrder(List<Map<String, Object>> list);

    /**
     * 创建服务订单
     */
    Map<String, Object> createServiceOrder(List<Map<String, Object>> list);

    /**
     * 更新换款订单
     */
    int updateExchangeOrder(Integer userId, Double leftAmount, String orderno,Integer goodId, Integer goodchildId, Integer num,Double price);


}
