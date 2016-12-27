package com.jinlele.service.interfaces;

import net.sf.json.JSONArray;

import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 * 订单相关的服务类
 */
public interface IOrderService {
    /**
     *商城订单列表
     */
    Map<String, Object> getShopListPaging(int pagenow, int userid);
    /**
     *生成订单
     */
    void saveOrder(Double totalprice,Integer totalnum ,Integer userId,Integer storeId ,JSONArray json);

    /**
     * 自定义订单数据详情
     * @param map
     * @return
     */
    Map<String,Object> getOrderListDetail(Map<String,Object> map);

    /**
     * 根据订单号查询订单详情
     * @param orderno
     * @return
     */
    Map<String, Object> selectOrderDetailByOrderno(String orderno);

    /**
     *
     * @param orderno
     * @return
     */
    Map<String, Object> updateOrderStatusByOrderno(String orderno);
}
