package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.FavouriteMapper;
import com.jinlele.model.Favourite;
import com.jinlele.service.interfaces.IFavouriteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-1-3.
 */
@Service
public class FavouriteServiceImpl implements IFavouriteService{

    @Resource
    BaseMapper baseMapper;

    @Resource
    FavouriteMapper favouriteMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return favouriteMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(Favourite record) {
        return favouriteMapper.insertSelective(record);
    }

    @Override
    public List<Map<String , Object>> selectByuserIdAndGoodId(Favourite favourite) {
        return favouriteMapper.selectByuserIdAndGoodId(favourite);
    }
}
