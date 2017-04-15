package com.jinlele.controller;

import com.jinlele.model.ReceiptAddress;
import com.jinlele.service.interfaces.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    @Resource
    IDayPriceService dayPriceService;

    @Resource
    IMetalCalculationService metalCalculationService;

    @Resource
    IDiamondCalculationService diamondCalculationService;

   //②后台处理:拿到mediaId去后台上传图片传到服务器本地路径
   // 然后将本地图片上传到七牛并返回七牛图片url,在后台保存数据到翻新服务表 ，照片表 ，翻新服务_照片中间表
    @RequestMapping("/saveService")
    @ResponseBody
    public Map<String , Object> saveService(Integer userId , Double totalprice ,String type , String[] mediaIds) throws IOException {
        return serviceService.saveService(userId, totalprice, type, mediaIds);
    }

    @RequestMapping("/updateService")
    @ResponseBody
    public Map<String , Object> updateService(Integer userId , Integer serviceId ,String type , String[] mediaIds) throws IOException {
        return serviceService.updateService(userId, serviceId, type, mediaIds);
    }

    /**
     * 获取类别元素
     */
    @ResponseBody
     @RequestMapping(value = "/getSelectedItems/{typename}", method = RequestMethod.GET)
     public Map<String, Object> getSelectedItems(@PathVariable String typename) {
        return serviceService.getDictInfo(typename);
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

    /**
     * 获取当日金价
     */
    @ResponseBody
    @RequestMapping("/getCurrentPrice")
    public Map<String, Object>  getCurrentPrice(){
        return dayPriceService.getCurrentPrice();
    }

    @ResponseBody
    @RequestMapping(value = "/getSubSet/{category}/{pid}", method = RequestMethod.GET)
    public Map<String, Object>  getSubSet(@PathVariable String category,@PathVariable Integer pid){
        HashMap<String,Object> result= new HashMap<>();
        result.put("result",metalCalculationService.getSubSet(category,pid));
        return result;
    }

    @ResponseBody
    @RequestMapping("/getMaterial")
    public Map<String, Object>  getMaterial(){
        HashMap<String,Object> result= new HashMap<>();
        result.put("result",metalCalculationService.getMaterial());
        return result;
    }

    @ResponseBody
    @RequestMapping("/getQuality")
    public Map<String, Object>  getQuality(){
        HashMap<String,Object> result= new HashMap<>();
        result.put("result",metalCalculationService.getQuality());
        return result;
    }

    /**
     * 钻石估价
     */
    @ResponseBody
    @RequestMapping(value = "/getDiamondPrice" ,method = RequestMethod.POST)
    public  Map<String,Object> getDiamondPrice(@RequestBody List<Map<String,Object>> list) {
        return diamondCalculationService.addDiamondPrice(list);
    }

    /**
     * 贵金属估价
     */
    @ResponseBody
    @RequestMapping(value = "/getPMPrice/{weight}/{purity}/{goodId}/{goodChildId}" ,method = RequestMethod.GET)
    public  Map<String,Object> getPMPrice(@PathVariable Double weight,@PathVariable  String purity,@PathVariable  Integer goodId,@PathVariable  Integer goodChildId) {
        return metalCalculationService.addPMPrice(purity, weight,goodId,goodChildId,true);
    }

    //获取某次换款服务中选择的换款商品个数
    @ResponseBody
    @RequestMapping(value = "/getShopcharTotalNum/{serviceId}" ,method = RequestMethod.GET)
    public  Map<String,Object> getShopcharTotalNum(@PathVariable  Integer serviceId) {
        HashMap<String,Object> result= new HashMap<>();
        int n = serviceService.getExChartTotalnum(serviceId);
        result.put("totalnum",n);
        return result;
    }

}
