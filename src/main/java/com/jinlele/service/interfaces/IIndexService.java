package com.jinlele.service.interfaces;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/17 0017.
 * 首页服务层接口
 */
public interface IIndexService {
    /**
     * 获取首页数据展示
     */
     Map<String, Object> getIndexInfo(Integer userId) ;

    /**
     * 首页新品推荐分页显示
     */
    Map<String, Object> getNewProductsPaging(int pagenow,String searchcontent) ;
}
