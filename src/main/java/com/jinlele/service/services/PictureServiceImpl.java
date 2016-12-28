package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.PictureMapper;
import com.jinlele.model.Picture;
import com.jinlele.service.interfaces.IPictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016-12-28.
 */
@Service
public class PictureServiceImpl implements IPictureService{

    @Resource
    BaseMapper baseMapper;

    @Resource
    PictureMapper pictureMapper;


}
