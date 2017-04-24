package com.jinlele.dao;

import com.jinlele.model.ShopOrder;
import com.jinlele.model.Store;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ShopOrderMapper {
    int deleteByPrimaryKey(String orderno);

    int insert(ShopOrder record);

    int insertSelective(ShopOrder record);

    ShopOrder selectByPrimaryKey(String orderno);

    int updateByPrimaryKeySelective(ShopOrder record);

    int updateByPrimaryKey(ShopOrder record);

    Map<String , Object> selectOrderInfoByOrderno(String orderno);

    Map<String , Object> findReceiptServiceByOrderno(@Param("orderno")String orderno);

    List findAllexpressCompanies();

    //根据订单号查询服务的id
    Integer selectServiceIdByOrderNo(String orderno);

    //查询下单时间  selectCreateTime
    Date selectCreateTime(String orderno);

    //获取收货证明信息
    Map<String,Object> getCertificationInfo(String orderno);

    //获取拍照邮寄图片
    List getPostbackImg(String orderno);

    //根据订单号查询用户id
    Integer getUserIdByOrderno(String orderno);

    Map<String, Object> getRechargeResult(String orderno);

    Store getStoreByOrderno(String orderno);

    int deleteOrderBySid(Integer serviceId);
}