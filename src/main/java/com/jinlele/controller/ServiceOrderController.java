package com.jinlele.controller;

import com.jinlele.service.interfaces.IOrderService;
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
     * 维修订单生成
     * @param userId
     * @param totalnum
     * @param products
     * @param descrip
     * @param type
     * @param storeId
     * @param mediaIds
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/saveRepairOrder")
    public Map<String , Object> saveRepairOrder(Integer userId ,Integer totalnum,String products , String descrip , String type , Integer storeId , String[] mediaIds) throws IOException {
        return  serviceOrderService.saveRepairOrder(userId ,totalnum,products , descrip , type , storeId , mediaIds);
    }

    /**
     * 查询维修订单是否已经定价
     */
    @ResponseBody
    @RequestMapping("/selectactualpprice/{orderno}")
    public Map<String , Object> selectactualpprice(@PathVariable String orderno) throws IOException {
        Map map = new HashedMap();
        Double money = orderService.selectactualpprice(orderno);
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
     * 更新维修订单和维修服务信息
     */
    @ResponseBody
    @RequestMapping(value = "/updateServiceOrder" ,method = RequestMethod.POST)
    public  Map<String,Object> updateServiceOrder(@RequestBody List<Map<String,Object>> list) {
        return serviceOrderService.createServiceOrder(list);
    }


}
