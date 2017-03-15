package com.jinlele.dao;

import com.jinlele.model.ServiceGood;

import java.util.List;
import java.util.Map;

public interface ServiceGoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ServiceGood record);

    int insertSelective(ServiceGood record);

    ServiceGood selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ServiceGood record);

    int updateByPrimaryKey(ServiceGood record);

    Map<String,Object> getGoodId(String orderno);

    List getBuyInfo(String orderno);
}