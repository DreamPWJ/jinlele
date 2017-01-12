package com.jinlele.service.interfaces;

import com.jinlele.model.ShopOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 * 订单相关的服务类
 */
public interface IOrderService {

    ShopOrder selectByPrimaryKey(String orderno);
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

    //订单详情 升级版
    Map<String, Object> getOrderDetail(String orderno);

    /**
     * 根据订单号及jsapi支付结果处理并查询订单详情
     */
    Map<String,Object> putOrder(String orderno,String orderType ,String payresult);
    /**
     *取消订单
     */
    Map<String, Object> modifyOrder(String orderno);

    /**
     *更新订单
     */
    int updateByPrimaryKeySelective(ShopOrder record);

    //服务翻新详情页面 根据订单号查询买买方物流单号 买卖方收货地址信息
    Map<String , Object> findReceiptServiceByOrderno(String orderno);

    //查询所有的快递
    List<Map<String , Object>> findAllexpressCompanies();


}
