package com.jinlele.dao;

import com.jinlele.model.Good;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Good record);

    int insertSelective(Good record);

    Good selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Good record);

    int updateByPrimaryKeyWithBLOBs(Good record);

    int updateByPrimaryKey(Good record);

    /**
     * 获取首页新品推荐
     *
     * @return
     */
    List getNewProducts();

    /**
     * 获取产品详情
     */
    Map<String, Object> getGoodDetail(@Param("gooodId") int gooodId);

    /**
     * 根据商品的id 查询所有的子商品信息
     */
    List<Map<String, Object>>  getGoodChildsByGoodId(@Param("gooodId") int gooodId);
}