package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.Product;
import com.jinlele.model.Service;
import com.jinlele.model.ShopOrder;
import com.jinlele.service.interfaces.IServiceOrderService;
import com.jinlele.util.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 */
@org.springframework.stereotype.Service
public class ServiceOrderServiceImpl implements IServiceOrderService{

    @Resource
    BaseMapper baseMapper;

    @Resource
    ShopOrderMapper  shopOrderMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    ServiceMapper serviceMapper;

    //生成服务类订单
    @Override
    public Map<String , Object> saveServiceOrder(Integer serviceId, String type, Integer userId, Integer storeId, String sendWay, String getWay, Double totalprice, Integer buyeraddresId, String products) {
        //生成服务订单表
        String orderno = StringHelper.getOrderNum();  //生成订单号
        ShopOrder order  = new ShopOrder(orderno , totalprice, totalprice, userId,  storeId,  type, "001", buyeraddresId);
        shopOrderMapper.insertSelective(order);
        //更新服务表
        Service service = new Service(serviceId, storeId ,orderno , sendWay , getWay);
        serviceMapper.updateByPrimaryKeySelective(service);
        //循环新增产品表
        JSONObject json = new JSONObject().fromObject(products.replaceAll("&quot;","\"")); // 首先把字符串转成 JSONObect  对象，json 对象值使用""双引号存储
        Product product = null;
        JSONArray array1 = json.getJSONArray("firstCatogoryId");
        JSONArray array2 = json.getJSONArray("secondCatogoryId");
        JSONArray array3 = json.getJSONArray("num");
        JSONArray array4 = json.getJSONArray("memo");
        for(int i=0;i<array1.size();i++){
            Integer  catogoryId =  array2.getInt(i);
            Integer num = array3.getInt(i);
            String memo = array4.getString(i);
            product = new Product(catogoryId , type , serviceId ,num , memo);
            //保存服务类商品
            productMapper.insertSelective(product);
        }
        Map<String ,Object> map = new HashMap();
        map.put("orderNo" , orderno);
        return map;
    }
}
