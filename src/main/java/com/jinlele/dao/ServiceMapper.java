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

    Map<String , Object> getrefurbishPrice();

    Map<String , Object> getdetectPrice();

    List selectServiceDetailByOrderno(String orderno);
}