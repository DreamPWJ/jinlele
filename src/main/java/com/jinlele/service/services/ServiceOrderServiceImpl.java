package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.*;
import com.jinlele.service.interfaces.IServiceOrderService;
import com.jinlele.util.StringHelper;
import com.jinlele.util.qiniuUtils.QiniuParamter;
import com.jinlele.util.qiniuUtils.QiniuUtil;
import com.jinlele.util.weixinUtils.util.AdvancedUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
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
    PictureMapper pictureMapper;
    @Resource
    ProductMapper productMapper;
    @Resource
    ServiceMapper serviceMapper;
    @Resource
    ServicePictureMapper servicePictureMapper;

    public static String key_suff =  "service/";
    public static String savePath =  "c:/download";  //默认保存到服务器的该目录

    //生成服务类订单
    @Override
    public Map<String , Object> saveServiceOrder(Integer serviceId, Integer totalnum,String type, Integer userId, Integer storeId, String sendWay, String getWay, Double totalprice, Integer buyeraddresId, String products) {
        //生成服务订单表
        String orderno = StringHelper.getOrderNum();  //生成订单号
        Date orderTime = new Date();
        String shoporderstatus = type + "001";
        ShopOrder order  = new ShopOrder(orderno ,totalnum, totalprice, totalprice, userId,  storeId,  type, shoporderstatus, buyeraddresId , orderTime);
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
        map.put("totalprice" , totalprice);
        map.put("orderTime" , orderTime);
        return map;
    }


    /**
     * 维修订单 : 生成订单表 服务表 产品表  图片表  服务_图片中间表
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
    @Override
    public Map<String, Object> saveRepairOrder(Integer userId,Integer totalnum, String products, String descrip, String type, Integer storeId, String[] mediaIds) throws IOException {
        //循环下载媒体文件 上传到七牛 并返回 七牛的连接
        String filePath = null;
        String key = null;
        String imgurl = null;
        Picture picture = null;
        //生成维修服务订单表
        String orderno = StringHelper.getOrderNum();  //生成订单号
        Date orderTime = new Date();
        String shoporderstatus = type + "001";
        ShopOrder order  = new ShopOrder(orderno ,totalnum,  userId,  storeId, type, shoporderstatus , orderTime);
        shopOrderMapper.insertSelective(order);
        //保存服务表
        Service service = new Service(userId ,orderno , descrip , storeId); //暂时设定门店为1，以后会动态获取
        serviceMapper.insertSelective(service);
        Map<String , Object> map = new HashedMap();
        ServicePicture servicePicture = null;
        for(int i=0,len=mediaIds.length;i<len;i++){
            filePath = AdvancedUtil.getMedia(mediaIds[i] , savePath);
            key = key_suff + mediaIds[i];
            QiniuUtil.upload(filePath,key);
            //拼接七牛的路径
            imgurl = QiniuParamter.URL + key;
            //保存图片表
            picture = new Picture(imgurl , userId);
            pictureMapper.insertSelective(picture);
            //插入中间表
            servicePicture = new ServicePicture(service.getId(), picture.getId() , type);
            servicePictureMapper.insertSelective(servicePicture);
            //删除服务器上的该文件
            StringHelper.deleteFile(filePath);
        }
        //保存订单表 保存产品表
        //循环新增产品表
        // JSONObject json = JSONObject.fromObject(products.replaceAll("&quot;","\"")); // 首先把字符串转成 JSONObect  对象，json 对象值使用""双引号存储
        JSONObject json = JSONObject.fromObject(products.replaceAll("&quot;","\"")); // 首先把字符串转成 JSONObect  对象，json 对象值使用""双引号存储
        Product product = null;
        JSONArray array1 = json.getJSONArray("firstCatogoryId");
        JSONArray array2 = json.getJSONArray("secondCatogoryId");
        JSONArray array3 = json.getJSONArray("num");
        JSONArray array4 = json.getJSONArray("memo");
        for(int i=0;i<array1.size();i++){
            Integer  catogoryId =  array2.getInt(i);
            Integer num = array3.getInt(i);
            String memo = array4.getString(i);
            product = new Product(catogoryId , type , service.getId() ,num , memo);
            //保存服务类商品
            productMapper.insertSelective(product);
        }
        map.put("orderno" , orderno);
        map.put("serviceId" , service.getId());
        return map;
    }
}
