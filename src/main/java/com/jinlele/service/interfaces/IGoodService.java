package com.jinlele.service.interfaces;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20 0020.
 * 商品相关的服务接口
 */
public interface IGoodService {
    /**
     * 获取产品详情
     * @param goodId 商品id
     * @return
     */
    Map<String, Object> getGoodDetail(int goodId);

    /**
     * 根据商品的id 查询所有的子商品信息
     */
    List<Map<String, Object>> getGoodChildsByGoodId(@Param("gooodId") int gooodId);

    Map<String,Object> getExchangeGoodPaging(int pagenow);
}
