package com.jinlele.service.services;

import com.jinlele.dao.ServiceGoodMapper;
import com.jinlele.model.ServiceGood;
import com.jinlele.service.interfaces.IServiceGoodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/3/12.
 */
@Service
public class ServiceGoodServiceImpl implements IServiceGoodService {
    @Resource
    ServiceGoodMapper serviceGoodMapper;

    @Override
    public int insertSelective(ServiceGood record) {
        return serviceGoodMapper.insertSelective(record);
    }
}
