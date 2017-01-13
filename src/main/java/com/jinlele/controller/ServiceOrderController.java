package com.jinlele.controller;

import com.jinlele.service.interfaces.IServiceOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
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


}
