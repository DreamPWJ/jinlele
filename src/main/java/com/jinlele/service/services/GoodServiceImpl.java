package com.jinlele.service.services;

import com.jinlele.dao.GoodMapper;
import com.jinlele.service.interfaces.IGoodService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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

    /**
     * 根据商品的id 查询所有的子商品信息
     */
    @Override
    public List<Map<String, Object>> getGoodChildsByGoodId(@Param("gooodId") int gooodId) {
        return goodMapper.getGoodChildsByGoodId(gooodId);
    }

}
