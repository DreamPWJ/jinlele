package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.FavouriteMapper;
import com.jinlele.model.Favourite;
import com.jinlele.service.interfaces.IFavouriteService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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

    @Override
    public Map<String, Object> getFavsByUidPaging(int pagenow, int userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " favourite f left join good g on g.id = f.good_id  ");
        paramMap.put("fields", " f.id as fid , g.id as gid, g.title ,g.price,g.oldprice ,g.bannerurl ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", 6);   //SysConstants.PAGESIZE
        paramMap.put("wherecase",  "user_id = " + userId + " and f.deleteCode = '001' ");
        paramMap.put("orderField", " f.create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap) ;
    }
}
