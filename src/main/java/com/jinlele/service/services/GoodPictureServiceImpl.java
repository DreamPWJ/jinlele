package com.jinlele.service.services;

import com.jinlele.dao.GoodPictureMapper;
import com.jinlele.service.interfaces.IGoodPictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */
@Service
public class GoodPictureServiceImpl implements IGoodPictureService {
    @Resource
    GoodPictureMapper goodPictureMapper;
    @Override
    public List getGoodPicture(int goodId) {
        return goodPictureMapper.getGoodPicture(goodId);
    }
}
