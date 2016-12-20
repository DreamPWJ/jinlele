package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.GoodCatogoryMapper;
import com.jinlele.service.interfaces.IGoodCatogoryService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * @param querytype    查询条件 综合 0  最新 1 价格从高到低 2 价格从低到高 3
     */
    @Override
    public Map<String, Object> getGoodListPaging(int pagenow, String categoryname, int querytype) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " good ");
        paramMap.put("fields", " id ,title,bannerurl,saleprice,discprice,description ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", " category_id in (SELECT id FROM goodcatogory WHERE  name='" + categoryname + "')");
        paramMap.put("orderField", " create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap) ;
    }

    /**
     * 获取一级分类下的二级分类
     * @param pid 一级分类id
     * @return  二级分类集合
     */
    public List getSecondCatogaryByPid(@Param("pid") Integer pid){
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
        paramMap.put("tableName", " good ");
        paramMap.put("fields", " id ,title,bannerurl,saleprice,discprice,description ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", "category_id = " + catogoryId);
        paramMap.put("orderField", " create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap) ;
    }

}
