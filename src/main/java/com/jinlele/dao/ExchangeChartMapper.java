package com.jinlele.dao;

import com.jinlele.model.ExchangeChart;
import com.jinlele.model.Service;

import java.util.List;
import java.util.Map;

public interface ExchangeChartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeChart record);

    int insertSelective(ExchangeChart record);

    ExchangeChart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeChart record);

    int updateByPrimaryKey(ExchangeChart record);

    int updateByServiceId(Integer serviceId);

    ExchangeChart selectByUQ(Integer serviceId,Integer goodId,Integer goodChildId);

    Map<String,Object> getCalcData(Integer serviceId);

    //获取某次换款服务中选择的换款商品个数
    int getExChartTotalnum(Integer serviceId);

    //获取某次换款服务中选择的结算商品个数
    int getExChartcheckTotalnum(Integer serviceId);

    //获取换款购物车的选中商品的总价格
    double getEcheckTotalPrice(Integer serviceId);

    //获取换款购物车中的所有商品id集合
    List<Integer> getAllGoodIds(Integer serviceId);

    //根据订单号获取换款购物车的选中商品的总价格
    double getEcheckTotalPriceByOrder(String orderno);

    //获取估价和实际定价价格
    Map<String, Object> getFixPrice(String orderno);
}