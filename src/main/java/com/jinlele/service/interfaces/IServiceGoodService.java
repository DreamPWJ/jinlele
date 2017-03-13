package com.jinlele.service.interfaces;

import com.jinlele.model.ServiceGood;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/12.
 */
public interface IServiceGoodService {
    int insertSelective(ServiceGood record);

    Map<String,Object> getGoodId(String orderno);
}
