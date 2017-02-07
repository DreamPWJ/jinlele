package com.jinlele.dao;

import com.jinlele.model.ShopBanner;

import java.util.List;
import java.util.Map;

public interface ShopBannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopBanner record);

    int insertSelective(ShopBanner record);

    ShopBanner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopBanner record);

    int updateByPrimaryKey(ShopBanner record);

    List<Map<String , Object>> listBanners();
}