package com.jinlele.dao;

import com.jinlele.model.DayPrice;

import java.util.List;

public interface DayPriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DayPrice record);

    int insertSelective(DayPrice record);

    DayPrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DayPrice record);

    int updateByPrimaryKey(DayPrice record);

    List<DayPrice> getCurrentPrice();
}