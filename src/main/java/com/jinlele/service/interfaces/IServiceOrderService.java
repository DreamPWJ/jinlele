package com.jinlele.service.interfaces;

import java.util.Map;

/**
 * 服务订单接口
 */
public interface IServiceOrderService {

    public Map<String , Object> saveServiceOrder(Integer serviceId , String type , Integer userId, Integer storeId, String sendWay , String getWay, Double totalprice , Integer buyeraddresId , String products);

}
