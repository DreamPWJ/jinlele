package com.jinlele.dao;

import com.jinlele.model.Service;

import java.util.List;
import java.util.Map;

public interface ServiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Service record);

    int insertSelective(Service record);

    Service selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Service record);

    int updateByPrimaryKey(Service record);

    int updateByOrdernoSelective(Service record);

    Map<String , Object> getrefurbishPrice();

    Map<String , Object> getdetectPrice();

    List selectServiceDetailByOrderno(String orderno);

    //获得服务信息订单所有图片
    List getServicePictures(String orderno,String orderType);

    //服务订单产品信息详情
    Map<String , Object> getServiceProducts(String orderno);

    //根据订单号查询服务信息
    Map<String , Object> getServiceInfo(String orderno);
    //根据类型查询字典信息
    List getDictInfo(String typename);

    //查询服务订单定价金额或实际付款价格
    Double selectActualPrice(String orderno);

    Map<String,Object> getServiceProgressInfoByOrderno(String orderno);

    String getStatusByOrderno(String orderno);

    String getOrderById(Integer id);

}