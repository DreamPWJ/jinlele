package com.jinlele.dao;

import com.jinlele.model.ExchangeChart;

import java.util.Map;

public interface ExchangeChartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeChart record);

    int insertSelective(ExchangeChart record);

    ExchangeChart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeChart record);

    int updateByPrimaryKey(ExchangeChart record);

    ExchangeChart selectByUQ(Integer serviceId,Integer goodId,Integer goodChildId);

    Map<String,Object> getCalcData(Integer serviceId);
}