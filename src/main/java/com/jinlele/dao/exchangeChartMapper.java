package com.jinlele.dao;

import com.jinlele.model.exchangeChart;

public interface exchangeChartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(exchangeChart record);

    int insertSelective(exchangeChart record);

    exchangeChart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(exchangeChart record);

    int updateByPrimaryKey(exchangeChart record);
}