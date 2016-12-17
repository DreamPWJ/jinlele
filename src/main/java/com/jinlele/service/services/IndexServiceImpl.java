package com.jinlele.service.services;

import com.jinlele.dao.GoodCatogoryMapper;
import com.jinlele.service.interfaces.IIndexService;
import com.sun.tools.jdi.LinkedHashMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/17 0017.
 * 首页服务层实现类
 */
@Service
public class IndexServiceImpl implements IIndexService
{
    @Resource
    GoodCatogoryMapper goodCatogoryMapper;
    /**
     * 获取首页数据展示
     */
    @Override
    public Map<String, Object> getIndexInfo() {
        Map<String, Object> indexMap= new LinkedHashMap();
        List firstCatogoryList=goodCatogoryMapper.getFirstCatogory();
        indexMap.put("firstCatogoryList",firstCatogoryList);
        return indexMap;
    }
}
