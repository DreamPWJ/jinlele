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
    Map<String, Object> getGoodListPaging(int pagenow,String categoryname,int  querytype);
}
