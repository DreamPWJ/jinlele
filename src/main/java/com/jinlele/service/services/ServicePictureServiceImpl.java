package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.ServicePictureMapper;
import com.jinlele.model.ServicePicture;
import com.jinlele.service.interfaces.IServicePictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016-12-28.
 */
@Service
public class ServicePictureServiceImpl implements IServicePictureService {

    @Resource
    BaseMapper baseMapper;

    @Resource
    ServicePictureMapper servicePictureMapper;


}
