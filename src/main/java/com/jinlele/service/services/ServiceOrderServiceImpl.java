package com.jinlele.service.services;

import com.google.zxing.WriterException;
import com.jinlele.dao.*;
import com.jinlele.model.*;
import com.jinlele.service.interfaces.IReceiptAddressService;
import com.jinlele.service.interfaces.IServiceOrderService;
import com.jinlele.service.interfaces.IWalletService;
import com.jinlele.util.StringHelper;
import com.jinlele.util.qiniuUtils.QiniuParamter;
import com.jinlele.util.qiniuUtils.QiniuUtil;
import com.jinlele.util.rqcode.MatrixToImageWriter;
import com.jinlele.util.weixinUtils.util.AdvancedUtil;
import com.sun.tools.internal.ws.wsdl.document.jaxws.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.Exception;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 */
@org.springframework.stereotype.Service
public class ServiceOrderServiceImpl implements IServiceOrderService{

    public static String key_suff =  "service/";
    public static String savePath =  "c:/download";  //默认保存到服务器的该目录
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
    @Resource
    IReceiptAddressService receiptAddressService;
    @Resource
    IWalletService walletService;
    @Resource
    ServiceGoodMapper serviceGoodMapper;

    //生成服务类订单
    @Override
    public Map<String , Object> saveServiceOrder(Integer serviceId, Integer totalnum,String type, Integer userId, Integer storeId, String sendWay, String getWay, Double totalprice, Integer buyeraddresId, String products) {
        //生成服务订单表
        String orderno = StringHelper.getOrderNum();  //生成订单号
        Date orderTime = new Date();
        String shoporderstatus = type + "001";
        ShopOrder order  = new ShopOrder(orderno ,totalnum, totalprice, 0.0, userId,  storeId,  type, shoporderstatus, buyeraddresId , orderTime);
        try {
            order.setQrcodeUrl(MatrixToImageWriter.makeQRCode(type, orderno));//生成二维码
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

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
     * 创建维修订单
     */
    @Override
    public Map<String, Object> saveRepairOrder(List<Map<String, Object>> list) {
        Map<String, Object> resultMap = new HashedMap();
        //循环下载媒体文件 上传到七牛 并返回 七牛的连接
        String filePath = null;
        String key = null;
        String imgurl = null;
        Picture picture = null;
        //订单号生成
        String orderno = StringHelper.getOrderNum();
        //一条总数据
        for (Map<String, Object> repairInfo : list) {
            Integer userId = Integer.valueOf(repairInfo.get("userId").toString());//用户id
            String type = repairInfo.get("type").toString();//业务类型type
            Integer totalnum = Integer.valueOf(repairInfo.get("totalnum").toString());//总数量
            List<String> mediaIds = (List)repairInfo.get("mediaIds");//媒体id数组
            List<Map<String, Object>> products = (List) repairInfo.get("products");//产品信息
            //生成维修服务订单表
            Date orderTime = new Date();
            String shoporderstatus = type + "001";
            ShopOrder order  = new ShopOrder(orderno ,totalnum,  userId, type, shoporderstatus , orderTime);
            try {
                order.setQrcodeUrl(MatrixToImageWriter.makeQRCode(type, orderno));//生成二维码
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriterException e) {
                e.printStackTrace();
            }
            shopOrderMapper.insertSelective(order);
            //保存服务表
            Service service = new Service(userId ,orderno);
            serviceMapper.insertSelective(service);
            ServicePicture servicePicture = null;
            for(int i=0,len=mediaIds.size();i<len;i++){
                filePath = AdvancedUtil.getMedia(mediaIds.get(i) , savePath);
                key = key_suff + mediaIds.get(i);
                try {
                    QiniuUtil.upload(filePath,key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            Product product = null;
            for (Map<String, Object> detailInfo : products) {
                Integer catogoryId = Integer.valueOf(detailInfo.get("secondCatogoryId").toString());
                String repairItemValue = detailInfo.get("repairItemValue").toString();
                Integer num = Integer.valueOf(detailInfo.get("num").toString());
                String memo = detailInfo.get("memo").toString();

                product = new Product(catogoryId , type , service.getId() ,num , memo);
                product.setRepairitem(repairItemValue);//维修项目类型
                //保存服务类商品
                productMapper.insertSelective(product);
            }
            resultMap.put("orderno" , orderno);
            resultMap.put("serviceId" , service.getId());
        }
        return resultMap;
    }


    @Override
    public Map<String, Object> updateRepairOrder(List<Map<String, Object>> list) {
        Map<String, Object> resultMap = new HashedMap();
        //一条总数据
        for (Map<String, Object> confirmInfo : list) {
            Integer userId = Integer.valueOf(confirmInfo.get("userId").toString());//用户id
            String type = confirmInfo.get("type").toString();//业务类型type
            Integer storeId = Integer.valueOf(confirmInfo.get("storeId").toString());//门店id
            String sendWay = confirmInfo.get("sendWay").toString();//送货方式
            String getWay = confirmInfo.get("getWay").toString();//取货方式
            Double totalprice = Double.valueOf(confirmInfo.get("totalprice").toString());//总金额
            String orderno = confirmInfo.get("orderno").toString();//订单号
            List<Map<String, Object>> addressinfo = (List) confirmInfo.get("addressinfo");//地址信息
            //获取地址id
            ReceiptAddress address = null;
            for (Map<String, Object> item : addressinfo) {
                address = new ReceiptAddress(item.get("userName").toString(), item.get("postalCode").toString(), item.get("provinceName").toString(), item.get("cityName").toString(), item.get("countryName").toString(), item.get("detailInfo").toString(), item.get("nationalCode").toString(), item.get("telNumber").toString(), userId);
            }
            Map<String, Object> result = receiptAddressService.createReceiptAddressId(address);
            //更新订单信息
            ShopOrder order = new  ShopOrder(orderno,type,userId,storeId,totalprice,Integer.valueOf(result.get("receiptAddressId").toString()));
            shopOrderMapper.updateByPrimaryKeySelective(order);
            Integer serviceId =  shopOrderMapper.selectServiceIdByOrderNo(orderno);
            Service service = new Service(serviceId ,totalprice ,totalprice ,userId , storeId , sendWay ,getWay ,new Date());
            serviceMapper.updateByPrimaryKeySelective(service);
            Date orderTime = shopOrderMapper.selectCreateTime(orderno);
            resultMap.put("orderTime" , orderTime);
            resultMap.put("orderNo" , orderno);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> createServiceOrder(List<Map<String, Object>> list) {
        Map<String, Object> resultMap = new HashedMap();
        //订单号生成
        String orderno = StringHelper.getOrderNum();
        //一条总数据
        for (Map<String, Object> confirmInfo : list) {
            Integer userId = Integer.valueOf(confirmInfo.get("userId").toString());//用户id
            String type = confirmInfo.get("type").toString();//业务类型type
            Integer storeId = Integer.valueOf(confirmInfo.get("storeId").toString());//门店id
            String sendWay = confirmInfo.get("sendWay").toString();//送货方式
            String getWay = confirmInfo.get("getWay").toString();//取货方式
            Integer totalnum = Integer.valueOf(confirmInfo.get("totalnum").toString());//总数量
            Double totalprice = Double.valueOf(confirmInfo.get("totalprice").toString());//总金额
            Integer serviceId = Integer.valueOf(confirmInfo.get("serviceId").toString());//服务id
            List<Map<String, Object>> addressinfo = (List) confirmInfo.get("addressinfo");//地址信息
            //获取地址id
            ReceiptAddress address = null;
            for (Map<String, Object> item : addressinfo) {
                address = new ReceiptAddress(item.get("userName").toString(), item.get("postalCode").toString(), item.get("provinceName").toString(), item.get("cityName").toString(), item.get("countryName").toString(), item.get("detailInfo").toString(), item.get("nationalCode").toString(), item.get("telNumber").toString(), userId);
            }
            Map<String, Object> result = receiptAddressService.createReceiptAddressId(address);
            Date orderTime = new Date();
            try {
                //保存订单
                String shoporderstatus = type + "001";
                ShopOrder order  = new ShopOrder(orderno ,totalnum, totalprice, totalprice, userId,  storeId,  type, shoporderstatus, Integer.valueOf(result.get("receiptAddressId").toString()) , orderTime);
                order.setQrcodeUrl(MatrixToImageWriter.makeQRCode(type, orderno));//生成二维码

                //生成订单
                shopOrderMapper.insertSelective(order);
                //更新服务表
                Service service = new Service(serviceId, storeId ,orderno , sendWay , getWay);
                serviceMapper.updateByPrimaryKeySelective(service);
                //添加订单_商品中间表，记录订单明细
                List<Map<String, Object>> products = (List) confirmInfo.get("products");//产品集合
                Product product = null;
                for (Map<String, Object> detailInfo : products) {
                    Integer catogoryId = Integer.valueOf(detailInfo.get("secondCatogoryId").toString());
                    Integer num = Integer.valueOf(detailInfo.get("num").toString());
                    String memo = detailInfo.get("memo").toString();

                    product = new Product(catogoryId , type , serviceId ,num , memo);
                    //保存服务类商品
                    productMapper.insertSelective(product);
                }
                resultMap.put("errmsg", "ok");

            } catch (Exception e) {
                resultMap.put("errmsg", "error");
            }
            resultMap.put("orderNo" , orderno);
            resultMap.put("totalprice" , totalprice);
            resultMap.put("orderTime" , orderTime);
        }
        return resultMap;
    }

    @Override
    public int updateExchangeOrder(Integer userId, Double leftAmount, String orderno,Integer goodId, Integer goodchildId, Integer num,Double price){
        try {
            //保存换购商品信息
            ServiceGood serviceGood = new ServiceGood(orderno,goodId,goodchildId,num,price,leftAmount );
            serviceGoodMapper.insertSelective(serviceGood);
            //更新订单状态
            ShopOrder order = shopOrderMapper.selectByPrimaryKey(orderno);
            order.setShoporderstatuscode("005009");//已付款
            shopOrderMapper.updateByPrimaryKeySelective(order);
            //更新账户
            walletService.updateWallet(userId, leftAmount, orderno);
            return  1;
        } catch (Exception e) {
            return 0;
        }
    }


}
