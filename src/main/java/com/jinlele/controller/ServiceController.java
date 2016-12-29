package com.jinlele.controller;

import com.jinlele.model.ReceiptAddress;
import com.jinlele.service.interfaces.IReceiptAddressService;
import com.jinlele.service.interfaces.IServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Map<String , Object> saveService(Integer userId , Double price , String descrip , String type , Integer storeId , String[] mediaIds) throws IOException {
        serviceService.saveService(userId ,price ,descrip , type,storeId,mediaIds);
        Map<String , Object> map = new HashMap();
        map.put("status" , "ok");
        return map;
    }


    @RequestMapping("/saveAddress")
    @ResponseBody
    public void  saveAddress(ReceiptAddress receiptAddress){
        receiptAddressService.insertSelective(receiptAddress);
    }


}
