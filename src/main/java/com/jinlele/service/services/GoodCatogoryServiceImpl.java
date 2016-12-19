package com.jinlele.service.services;

import com.jinlele.dao.GoodCatogoryMapper;
import com.jinlele.service.interfaces.IGoodCatogoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/19.
 */
@Service
public class GoodCatogoryServiceImpl implements IGoodCatogoryService {
    @Resource
    GoodCatogoryMapper goodCatogoryMapper;
    @Override
    public Map<String, Object> getFirstCatogory() {
        Map<String, Object> catogories= new HashMap();
        //一级分类
        List firstList=goodCatogoryMapper.getFirstCatogory();
        catogories.put("firstList",firstList);
        return catogories;
    }
}
