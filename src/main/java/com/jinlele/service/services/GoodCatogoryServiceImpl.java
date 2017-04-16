package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.GoodCatogoryMapper;
import com.jinlele.service.interfaces.IGoodCatogoryService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/19.
 */
@Service
public class GoodCatogoryServiceImpl implements IGoodCatogoryService {
    @Resource
    GoodCatogoryMapper goodCatogoryMapper;

    @Resource
    BaseMapper baseMapper;

    @Override
    public Map<String, Object> getFirstCatogory() {
        Map<String, Object> catogories = new HashMap();
        //一级分类
        List firstList = goodCatogoryMapper.getFirstCatogory();
        catogories.put("firstList", firstList);
        return catogories;
    }

    /**
     * 获取产品列表
     *
     * @param categoryname 二级分类名称
     * @param querytype    查询条件 综合 0  最新 1  价格2
     *                     flag  : 1 降序 0：升序
     */
    @Override
    public Map<String, Object> getGoodListPaging(int pagenow, String categoryname, int querytype , int flag) {
        try {
            categoryname =  java.net.URLDecoder.decode(categoryname,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String orderKey = "";
        if(querytype == 2){
            orderKey = " price ";
        }else {
            orderKey = " create_time ";
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " good g ,g_category gc,(SELECT min(price) as minprice,good_id  FROM goodchild group by good_id) gcc ");
        paramMap.put("fields", " distinct g.id,g.title,g.bannerurl,g.price,g.oldprice,g.description,gcc.minprice ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize",6);   //每次加载显示6个
        paramMap.put("wherecase", " gc.good_id=g.id and gcc.good_id = g.id and  gc.category_id in (SELECT id FROM goodcatogory WHERE  name='" + categoryname + "')  and g.deleteCode = '001' ");
        paramMap.put("orderField",  orderKey );
        paramMap.put("orderFlag", flag);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap) ;
    }




    /**
     * 获取一级分类下的二级分类
     * @param pid 一级分类id
     * @return  二级分类集合
     */
    public List getSecondCatogaryByPid( Integer pid){
        return  goodCatogoryMapper.getSecondCatogaryByPid(pid);
    }

    /**
     *  获取产品列表
     * @param pagenow   当前页
     * @param catogoryId 二级分类id
     * @return
     */
    public Map<String, Object> getGoodsByCidPaging(int pagenow, int catogoryId){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " good g join g_category gc on g.id=gc.good_id ");
        paramMap.put("fields", " g.id,g.title,g.hotimgurl,g.price,g.oldprice,g.description ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", 500);
        paramMap.put("wherecase", "gc.category_id = " + catogoryId + " and g.deleteCode = '001' ");
        paramMap.put("orderField", " create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap) ;
    }

    /**
     * 不分页
     * 获取一级分类下的二级分类
     * @param pid 一级分类id
     * @return  二级分类集合
     */
    public List getSecondCatogByPid(Integer pid){
        return  goodCatogoryMapper.getSecondCatogByPid(pid);
    }

}
