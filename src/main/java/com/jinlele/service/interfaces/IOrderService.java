package com.jinlele.service.interfaces;

import com.jinlele.model.ShopOrder;
import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 * 订单相关的服务类
 */
public interface IOrderService {
    /**
     *商城订单列表
     */
    Map<String, Object> getShopListPaging(int pagenow, int userid , String type);

    /**
     * 创建订单
     */
    Map<String, Object> createOrder(List<Map<String,Object>> list);
    /**
     * 自定义订单数据详情
     */
    Map<String,Object> getOrderListDetail(Map<String,Object> map , String type);

    /**
     * 根据订单号查询订单详情
     */
    Map<String, Object> getOrderDetailByOrderno(String orderno);



    /**
     *取消订单
     */
    Map<String, Object> modifyOrder(String orderno);

    /**
     *更新订单
     */
    int updateByPrimaryKeySelective(ShopOrder record);


}
