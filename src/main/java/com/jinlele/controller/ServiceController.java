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
        return  serviceService.saveService(userId ,aturalprice ,descrip , type,storeId,mediaIds);
    }

    /**
     * 获取最新用户地址
     */
    @ResponseBody
    @RequestMapping(value = "/getLatestInfo/{userid}", method = RequestMethod.GET)
    public Map<String, Object> getLatestInfo(@PathVariable Integer userid) {
        return receiptAddressService.getLatestInfo(userid);
    }

    @ResponseBody
    @RequestMapping("/createReceiptAddressId")
    public Map<String, Object>  createReceiptAddressId(ReceiptAddress receiptAddress){
       return receiptAddressService.createReceiptAddressId(receiptAddress);
    }

    /**
     * 获取翻新的价格
     */
    @ResponseBody
    @RequestMapping("/getrefurbishPrice")
    public Map<String, Object>  getrefurbishPrice(){
        return serviceService.getrefurbishPrice();
    }

    /**
     * 获取检测的价格
     */
    @ResponseBody
    @RequestMapping("/getdetectPrice")
    public Map<String, Object>  getdetectPrice(){
        return serviceService.getdetectPrice();
    }

    /**
     * 获取检测报告
     * @param orderno
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getServiceInfo/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getServiceInfo(@PathVariable String orderno) {
        return serviceService.getServiceInfo(orderno);
    }
}
