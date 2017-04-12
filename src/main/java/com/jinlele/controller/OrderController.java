package com.jinlele.controller;

import com.jinlele.model.ReturnApply;
import com.jinlele.model.ShopOrder;
import com.jinlele.model.ShoppingCart;
import com.jinlele.service.interfaces.*;
import com.jinlele.util.KdniaoTrackQueryAPI;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20 0020.
 * 订单相关的Controller
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    IShoppingCartService shoppingCartService;
    @Resource
    IOrderService orderService;

    @Resource
    ICommentService commentService;

    @Resource
    IPaymentdetailService paymentdetailService;

    @Resource
    IServiceOrderService serviceOrderService;

    @Resource
    IReturnApplyService returnApplyService;

    /**
     * 分页获取购物车数据
     *
     * @param pagenow
     * @param userid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCartList/{pagenow}/{userid}", method = RequestMethod.GET)
    public Map<String, Object> getCartList(@PathVariable int pagenow, @PathVariable  int userid) {
        return  shoppingCartService.getShoppingCartPaging(pagenow, userid);
    }

    @ResponseBody
    @RequestMapping(value = "/getBarterCartInfo/{pagenow}/{serviceid}", method = RequestMethod.GET)
    public Map<String, Object> getBarterCartInfo(@PathVariable int pagenow, @PathVariable  int serviceid) {
        return  shoppingCartService.getBarterCartPaging(pagenow, serviceid);
    }
    /**
     * 获取用户加入购车商品总数量
     */
    @ResponseBody
    @RequestMapping(value = "/getCartTotalNum/{userId}")
    public Map<String, Object> getCartTotalNum(@PathVariable int userId) {
        Map<String, Object> resultMap = new HashedMap();
        int totalnum = shoppingCartService.getShopcharTotalNum(userId);
        resultMap.put("totalnum" , totalnum);
        return resultMap;
    }

    /**
     * 更新或添加购物车
     * @param cart  购物车
     * @return   购物车商品数量
     */
    @ResponseBody
    @RequestMapping(value = "/addtocart",method = RequestMethod.GET)
    public int AddShoppingCart(ShoppingCart cart) {
        //处理业务逻辑 ，有记录就更新数量，没有就插入
        //查询是否有该用户和商品的 购物车(001)数据
        return shoppingCartService.addShoppingCart(cart);
    }

    /**
     * 删除购物车商品
     */
    @ResponseBody
    @RequestMapping(value = "/deleteShoppingCart/{userid}/{gcIdStr}",method = RequestMethod.GET)
    public int deleteShoppingCart(@PathVariable int userid,@PathVariable String gcIdStr) {
        String[] gcIds=gcIdStr.split("-");
        int count =0;
        for (int i=0;i<gcIds.length;i++){
            count+=shoppingCartService.deleteByUserIdGcid(userid,Integer.valueOf(gcIds[i]));
        }
        if(count==gcIds.length){
            return 1;
        }
        return 0;
    }

    /**
     * 商城订单列表
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderListDetail/{pagenow}/{userid}/{type}", method = RequestMethod.GET)
    public Map<String, Object> getOrderListDetail(@PathVariable int pagenow, @PathVariable int userid , @PathVariable String type) {
        return orderService.getOrderListDetail(orderService.getShopListPaging(pagenow, userid , type) , type);
    }

    /**
     * 根据订单号查询订单及其详情
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderDetailInfo/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getOrderDetailInfo(@PathVariable String orderno) {
        return orderService.getOrderDetailByOrderno(orderno);
    }

    /**
     * 根据服务类订单号查询订单及其详情
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderDetail/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getOrderDetai(@PathVariable String orderno) {
        return orderService.getOrderDetail(orderno);
    }

    /**
     * 根据订单号处理并查询订单及其详情
     */
    @ResponseBody
    @RequestMapping(value = "/putOrder/{orderno}/{orderType}/{payresult}", method = RequestMethod.GET)
    public Map<String, Object> putOrder(@PathVariable String orderno,@PathVariable String orderType,@PathVariable String payresult) {
        return orderService.putOrder(orderno,orderType ,payresult);
    }

    /**
     * 更改订单状态
     */
    @ResponseBody
    @RequestMapping(value = "/cancelOrder/{orderno}/{typeCode}", method = RequestMethod.GET)
    public Map<String, Object> modifyOrder(@PathVariable String orderno,@PathVariable String typeCode) {
        return orderService.modifyOrder(orderno,typeCode);
    }

    /**
     * 修改服务订单状态 为已付款
     */
    @ResponseBody
    @RequestMapping(value = "/updateOrder" ,method = RequestMethod.GET)
    public  Map<String, Object> updateOrder(ShopOrder order){
        int n =  orderService.updateByPrimaryKeySelective(order);
        Map<String ,Object> map = new HashedMap();
        map.put("n" ,n);
        return map;
    }


    /**
     * 创建订单
     */
    @ResponseBody
    @RequestMapping(value = "/createOrder" ,method = RequestMethod.POST)
    public  Map<String,Object> createOrder(@RequestBody List<Map<String,Object>> list) {
        return orderService.createOrder(list);
    }

    /**
     * 创建评论
     */
    @ResponseBody
    @RequestMapping(value = "/createComment" ,method = RequestMethod.POST)
    public  Map<String,Object> createComment(@RequestBody List<Map<String,Object>> list) {
        Map<String, Object> map = new HashMap<>();
        map.put("row", commentService.createComment(list));
        return map;
    }

    /**
     * 服务类收货地址页面数据加载
     * @param orderno
     * @return
     */
    @ResponseBody
    @RequestMapping("/findReceiptServiceByOrderno/{orderno}")
    public Map<String, Object> findReceiptServiceByOrderno(@PathVariable("orderno") String orderno) throws Exception {
        Map<String , Object> map = new HashedMap();
        List<Map<String, Object>> express = orderService.findAllexpressCompanies();
        Map<String , Object> order  = orderService.findReceiptServiceByOrderno(orderno);
        if(order.get("userlogisticsno")!=null && order.get("userlogisticsnoComp")!=null){
            JSONObject buyer = KdniaoTrackQueryAPI.getOrderTracesByJson((String)order.get("userlogisticsnoComp") , (String)order.get("userlogisticsno"));
            map.put("userLogistc" , buyer);
        }
        if(order.get("logisticsno")!=null && order.get("logisticsnoComp")!=null){
            JSONObject seller = KdniaoTrackQueryAPI.getOrderTracesByJson((String)order.get("logisticsnoComp") , (String)order.get("logisticsno"));
            map.put("storeLogistc" , seller);
        }
        map.put("express" ,express);
        map.put("order" ,order);
        return map;
    }

    /**
     * 更新订单 如 买家发货信息等
     */
    @ResponseBody
    @RequestMapping("/update")
    public Map<String, Object> update(ShopOrder order) {
        Map<String , Object> map = new HashedMap();
        int n = orderService.updateByPrimaryKeySelective(order);
        map.put("n" ,n);
        return map;
    }

//    /**
//     *  查询物流信息
//     */
//    @ResponseBody
//    @RequestMapping("/getOrderTracesByJson/{}")
//    public Map<String, Object> getOrderTracesByJson(ShopOrder order) {
//        Map<String , Object> map = new HashedMap();
//        int n = KdniaoTrackQueryAPI.getOrderTracesByJson(order);
//        map.put("n" ,n);
//        return map;
//    }


    @ResponseBody
    @RequestMapping(value = "/getCertificationInfo/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getCertificationInfo(@PathVariable String orderno) {
        return orderService.getCertificationInfo(orderno);
    }

    @ResponseBody
    @RequestMapping(value = "/getPostbackImg/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getPostbackImg(@PathVariable String orderno) {
        return orderService.getPostbackImg(orderno);
    }

    /**
     * 生成充值订单
     */
    @ResponseBody
    @RequestMapping(value = "/saveRechargeOrder/{userId}/{rechargeMoney}")
    public Map<String , Object>  saveRechargeOrder(@PathVariable Integer userId , @PathVariable Double rechargeMoney){
        return orderService.saveRechargeOrder(userId , rechargeMoney);

    }

    /**
     * 查询充值结果
     */
    @ResponseBody
    @RequestMapping(value = "/getRechargeResult/{orderno}")
    public Map<String , Object>  getRechargeResult(@PathVariable String orderno){
        return orderService.getRechargeResult(orderno);
    }

    /**
     * 查询账户充值提现明细
     */
    @ResponseBody
    @RequestMapping(value = "/getwalletDetail/{pagenow}/{userId}")
    public Map<String , Object>  getwalletDetail(@PathVariable int pagenow, @PathVariable Integer userId){
        return paymentdetailService.getPayDetailListPaging(pagenow, userId);
    }

    /**
     * 更新换款订单支付信息
     */
    @ResponseBody
    @RequestMapping(value = "/updateExchangeOrder/{orderno}/{userId}/{goodId}/{goodchildId}/{buynum}/{unitprice}/{money}", method = RequestMethod.GET)
    public Map<String, Object> updateExchangeOrder(@PathVariable String orderno, @PathVariable int userId, @PathVariable int goodId , @PathVariable Integer goodchildId , @PathVariable Integer buynum , @PathVariable Double unitprice, @PathVariable Double money) {
        Map<String , Object> map = new HashedMap();
        int n = serviceOrderService.updateExchangeOrder(userId,money,orderno,goodId,goodchildId,buynum,unitprice);
        map.put("n" ,n);
        return map;
    }

    /**
     * 申请退货
     */
    @ResponseBody
    @RequestMapping(value = "/addReturnApply/{type}/{orderno}/{userId}/{reasonCode}/{memo}", method = RequestMethod.GET)
    public Map<String, Object> addReturnApply(@PathVariable String type, @PathVariable String orderno, @PathVariable int userId, @PathVariable String reasonCode , @PathVariable String memo) {
        ReturnApply returnApply=new ReturnApply(orderno,userId,reasonCode,memo);
        return returnApplyService.addReturnApply(returnApply,type);
    }
}
