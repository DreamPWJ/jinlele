package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.GoodMapper;
import com.jinlele.service.interfaces.IGoodService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
    @Resource
    BaseMapper baseMapper;
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

    @Override
    public Map<String, Object> getBarterListPaging(Double amount, Integer pagenow , String type) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", "  (SELECT min(exprice) as exprice,min(price) as price,id,good_id FROM goodchild group by good_id) as gc,good g  ");
        paramMap.put("fields", "  g.id,gc.price,gc.exprice, gc.id childId,g.title,g.bannerurl ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", 4);
        switch (type){
            case "free":
                paramMap.put("wherecase", " g.id = gc.good_id and  g.canchange = 0 and gc.exprice <=" + amount);
                break;
            case "new":
                paramMap.put("wherecase", " g.id = gc.good_id and  g.canchange = 0 and gc.exprice >" + amount);
                break;
            default:
                paramMap.put("wherecase", " g.id = gc.good_id and  g.canchange = 0 ");
                break;
        }
        paramMap.put("orderField", "  g.create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }

}
