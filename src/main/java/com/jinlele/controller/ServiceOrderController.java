package com.jinlele.controller;
import com.jinlele.service.interfaces.IServiceOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 服务类订单 Controller
 */

@Controller
@RequestMapping("/serviceOrder")
public class ServiceOrderController {

    @Resource
    IServiceOrderService serviceOrderService;


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
    @RequestMapping("/saveServiceOrder")
    @ResponseBody
    public Map<String , Object> saveServiceOrder(Integer serviceId ,Integer totalnum, String type , Integer userId , Integer storeId, String sendWay , String getWay, Double totalprice , Integer buyeraddresId , String products){
         return  serviceOrderService.saveServiceOrder(serviceId ,totalnum , type , userId , storeId , sendWay , getWay , totalprice , buyeraddresId , products);
    }



}
