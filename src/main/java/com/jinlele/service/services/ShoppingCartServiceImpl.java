package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.ShoppingCartMapper;
import com.jinlele.model.ShoppingCart;
import com.jinlele.service.interfaces.IShoppingCartService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 */
@Service
public class ShoppingCartServiceImpl implements IShoppingCartService {

    @Resource
    BaseMapper baseMapper;

    @Resource
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public Map<String, Object> getShoppingCartPaging(int pagenow, int userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " shoppingcart s INNER JOIN good  g ON s.good_id=g.id ");
        paramMap.put("fields", " g.id,g.bannerurl,g.saleprice,s.num,g.title ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", " s.deleteCode='001' AND g.deleteCode='001' AND s.user_id=1 ");
        paramMap.put("orderField", " s.create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap) ;
    }

    @Override
    public int insertSelective(ShoppingCart record) {
        return shoppingCartMapper.insertSelective(record);
    }
}