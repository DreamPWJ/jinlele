package com.jinlele.service.services;

import com.jinlele.dao.GoodMapper;
import com.jinlele.service.interfaces.IGoodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20 0020.
 * 商品相关的服务类
 */
@Service
public class GoodServiceImpl implements IGoodService {
    @Resource
    GoodMapper goodMapper;
    /**
     * 获取产品详情
     * @param goodId 商品id
     * @return
     */
    @Override
    public Map<String, Object> getGoodDetail(int goodId) {

        return goodMapper.getGoodDetail(goodId);
    }
}
