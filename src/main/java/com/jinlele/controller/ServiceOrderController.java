package com.jinlele.controller;

import com.jinlele.dao.ExchangeChartMapper;
import com.jinlele.model.ServiceGood;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.service.interfaces.IServiceGoodService;
import com.jinlele.service.interfaces.IServiceOrderService;
import com.jinlele.service.interfaces.IServiceService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 服务类订单 Controller
 */

@Controller
@RequestMapping("/serviceOrder")
public class ServiceOrderController {

    @Resource
    IServiceOrderService serviceOrderService;

    @Resource
    IOrderService orderService;

    @Resource
    IServiceService serviceService;

    @Resource
    IServiceGoodService serviceGoodService;
    @Resource
    ExchangeChartMapper exchangeChartMapper;

    /**
     * 根据订单号查询service及shoporder
     */
    @ResponseBody
    @RequestMapping(value = "/getProgressInfo/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getProgressInfo(@PathVariable String orderno) {
        return serviceOrderService.getServiceProgressInfoByOrderno(orderno);
    }

    /**
     * 翻新下单完成
     * @param serviceId
     * @param type
     * @param userId
     * @param storeId
     * @param sendWay
     * @param getWay
     * @param totalprice
     * @param buyeraddresId
     * @param products
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveServiceOrder")
    public Map<String , Object> saveServiceOrder(Integer serviceId ,Integer totalnum, String type , Integer userId , Integer storeId, String sendWay , String getWay, Double totalprice , Integer buyeraddresId , String products){
        return  serviceOrderService.saveServiceOrder(serviceId ,totalnum , type , userId , storeId , sendWay , getWay , totalprice , buyeraddresId , products);
    }

    /**
     * 创建维修订单
     */
    @ResponseBody
    @RequestMapping(value = "/saveRepairOrder",method = RequestMethod.POST)
    public Map<String , Object> saveRepairOrder(@RequestBody List<Map<String,Object>> list) {
        return  serviceOrderService.saveRepairOrder(list);
    }

    /**
     * 查询服务订单定价金额或实际金额
     */
    @ResponseBody
    @RequestMapping("/selectactualpprice/{orderno}")
    public Map<String , Object> selectactualpprice(@PathVariable String orderno) throws IOException {
        Map map = new HashedMap();
        Double money = orderService.selectActualPrice(orderno);
        map.put("fixPrice" , money);
        return  map;
    }

    /**
     * 创建服务订单
     */
    @ResponseBody
    @RequestMapping(value = "/createServiceOrder" ,method = RequestMethod.POST)
    public  Map<String,Object> createServiceOrder(@RequestBody List<Map<String,Object>> list) {
        return serviceOrderService.createServiceOrder(list);
    }

    /**
     * 更新维修订单
     */
    @ResponseBody
    @RequestMapping(value = "/updateRepairOrder" ,method = RequestMethod.POST)
    public  Map<String,Object> updateRepairOrder(@RequestBody List<Map<String,Object>> list) {
        return serviceOrderService.updateRepairOrder(list);
    }

    /**
     * 添加换款商品信息
     */
    @ResponseBody
    @RequestMapping(value = "/addExchangeGood/{orderno}/{goodId}/{goodchildId}/{buynum}/{unitprice}/{money}", method = RequestMethod.GET)
    public Map<String, Object> addExchangeGood(@PathVariable String orderno,  @PathVariable int goodId , @PathVariable Integer goodchildId , @PathVariable Integer buynum , @PathVariable Double unitprice, @PathVariable Double money) {
        Map<String , Object> map = new HashedMap();
        ServiceGood serviceGood = new ServiceGood(orderno,goodId,goodchildId,buynum,unitprice);
        int n = serviceGoodService.insertSelective(serviceGood);
        map.put("n", n);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getGoodId/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getGoodId(@PathVariable String orderno) {
        Map<String , Object> map = new HashedMap();
        map.put("exchangeGood", serviceGoodService.getGoodId(orderno));
        return map;
    }

    /**
     * 创建换款订单
     */
    @ResponseBody
    @RequestMapping(value = "/createBarterOrder" ,method = RequestMethod.POST)
    public  Map<String,Object> createBarterOrder(@RequestBody List<Map<String,Object>> list) {
        return serviceOrderService.createBarterOrder(list);
    }
    /**
     * 添加到换款购物车
     */
    @ResponseBody
    @RequestMapping(value = "/addBarterCart" ,method = RequestMethod.POST)
    public  Map<String,Object> addBarterCart(@RequestBody List<Map<String,Object>> list) {
        return serviceOrderService.addBarterCart(list);
    }

    /**
     * 获取购物车计算数据
     */
    @ResponseBody
    @RequestMapping(value = "/getCalcData/{serviceId}" ,method = RequestMethod.GET)
    public  Map<String,Object> getCalcData(@PathVariable Integer serviceId) {
        return exchangeChartMapper.getCalcData(serviceId);
    }

    /**
     * 更新换款购物车信息
     */
    @ResponseBody
    @RequestMapping(value = "/updateBarterCar",method = RequestMethod.POST)
    public Map<String , Object> updateBarterCar(@RequestBody List<Map<String,Object>> list) {
        return  serviceOrderService.updateBarterCar(list);
    }

    /**
     * 删除换款购物车商品
     */
    @ResponseBody
    @RequestMapping(value = "/delBarterCar",method = RequestMethod.POST)
    public Map<String , Object> delBarterCar(@RequestBody List<Map<String,Object>> list) {
        return  serviceOrderService.deleteBarterCar(list);
    }
}
