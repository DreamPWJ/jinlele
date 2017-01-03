package com.jinlele.dao;

import com.jinlele.model.Favourite;

import java.util.List;

public interface FavouriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Favourite record);

    int insertSelective(Favourite record);

    Favourite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Favourite record);

    int updateByPrimaryKey(Favourite record);

    List selectByuserIdAndGoodId(Favourite favourite);
}