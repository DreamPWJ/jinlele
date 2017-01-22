package com.jinlele.service.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 服务订单接口
 */
public interface IServiceOrderService {

    public Map<String , Object> saveServiceOrder(Integer serviceId ,Integer totalnum, String type , Integer userId, Integer storeId, String sendWay , String getWay, Double totalprice , Integer buyeraddresId , String products);

    public Map<String , Object> saveRepairOrder(Integer userId ,Integer totalnum,String products , String descrip , String type , Integer storeId , String[] mediaIds) throws IOException;

    public Map<String , Object> updateRepair(String orderno ,String type, Integer userId, Integer storeId, String sendWay, String getWay, Double totalprice, Integer buyeraddresId);
    /**
     * 创建服务订单
     */
    Map<String, Object> createServiceOrder(List<Map<String,Object>> list);
}
