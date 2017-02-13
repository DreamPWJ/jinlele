package com.jinlele.service.interfaces;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-28.
 */
public interface IServiceService {


    Map<String , Object> saveService(Integer userId , Double aturalprice ,String type , String[] mediaIds) throws IOException;

    Map<String , Object> getrefurbishPrice();

    Map<String , Object> getdetectPrice();

    //根据订单号查询服务信息
    Map<String , Object> getServiceInfo(String orderno);

    //根据类型查询字典信息
    Map<String , Object> getDictInfo(String typename);
}
