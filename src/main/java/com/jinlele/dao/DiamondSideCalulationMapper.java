package com.jinlele.dao;

import com.jinlele.model.DiamondSideCalulation;

public interface DiamondSideCalulationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DiamondSideCalulation record);

    int insertSelective(DiamondSideCalulation record);

    DiamondSideCalulation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DiamondSideCalulation record);

    int updateByPrimaryKey(DiamondSideCalulation record);

    Double getDiamondSidePrice(String quality);
}