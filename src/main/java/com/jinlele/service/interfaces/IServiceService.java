package com.jinlele.service.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-28.
 */
public interface IServiceService {


    Map<String , Object> saveService(Integer userId , Double totalprice ,String type , String[] mediaIds) throws IOException;

    Map<String , Object> updateService(Integer userId ,Integer serviceId , String type , String[] mediaIds) throws IOException;

    Map<String , Object> getrefurbishPrice();

    Map<String , Object> getdetectPrice();

    //根据订单号查询服务信息
    Map<String , Object> getServiceInfo(String orderno);

    //获得服务信息订单所有图片
   List<Map<String , Object>> getReportImages(String orderno, String orderType);

    //根据类型查询字典信息
    Map<String , Object> getDictInfo(String typename);
    //获取某次换款服务中选择的换款商品个数
    int getExChartTotalnum(Integer serviceId);

    //获取某次换款服务中选择的结算商品个数
    int getExChartcheckTotalnum(Integer serviceId);


    //获取换款购物车的选中商品的总价格
    double getEcheckTotalPrice(Integer serviceId);

    Map<String,Object> getCalcData(Integer serviceId);

    List<Integer> getAllGoodIds(Integer serviceId);

    String getStatusByOrderno(String orderno);



}
