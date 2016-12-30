package com.jinlele.controller;

import com.jinlele.model.ReceiptAddress;
import com.jinlele.service.interfaces.IReceiptAddressService;
import com.jinlele.service.interfaces.IServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 5大服务类Controller
 */
@Controller
@RequestMapping("/service")
public class ServiceController {

    @Resource
    IServiceService serviceService;

    @Resource
    IReceiptAddressService receiptAddressService;

   //②后台处理:拿到mediaId去后台上传图片传到服务器本地路径
   // 然后将本地图片上传到七牛并返回七牛图片url,在后台保存数据到翻新服务表 ，照片表 ，翻新服务_照片中间表
    @RequestMapping("/saveService")
    @ResponseBody
    public Map<String , Object> saveService(Integer userId , Double aturalprice , String descrip , String type , Integer storeId , String[] mediaIds) throws IOException {
        int serviceId = serviceService.saveService(userId ,aturalprice ,descrip , type,storeId,mediaIds);
        Map<String , Object> map = new HashMap();
        map.put("serviceId" , serviceId);
        return map;
    }

    /**
     * 获取最新用户地址
     */
    @ResponseBody
    @RequestMapping(value = "/getLatestInfo/{userid}", method = RequestMethod.GET)
    public Map<String, Object> getLatestInfo(@PathVariable Integer userid) {
        return receiptAddressService.getLatestInfo(userid);
    }


    @RequestMapping("/createReceiptAddressId")
    @ResponseBody
    public Map<String, Object>  createReceiptAddressId(ReceiptAddress receiptAddress){
       return receiptAddressService.createReceiptAddressId(receiptAddress);
    }


}
