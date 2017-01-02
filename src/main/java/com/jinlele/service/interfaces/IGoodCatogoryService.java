package com.jinlele.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/19.
 */
public interface IGoodCatogoryService {
    /**
     *   获取首页一级分类
     * @return
     */
    Map<String, Object> getFirstCatogory();
    /**
     * 获取产品列表
     * @param categoryname 二级分类名称
     * @param querytype  查询条件 综合 0  最新 1 价格从高到低 2 价格从低到高 3
     */
    Map<String, Object> getGoodListPaging(int pagenow,String categoryname,int querytype ,int flag);

    /**
     * 获取一级分类下的二级分类
     * @param pid 一级分类id
     * @return  二级分类集合
     */
    List getSecondCatogaryByPid(Integer pid);


    /**
     *  获取产品列表
     * @param pagenow   当前页
     * @param catogoryId 二级分类id
     * @return
     */
    Map<String, Object> getGoodsByCidPaging(int pagenow, int catogoryId);


    /**
     * 不分页的
     * 获取一级分类下的二级分类
     * @param pid 一级分类id
     * @return  二级分类集合
     */
    List getSecondCatogByPid(Integer pid);

}
