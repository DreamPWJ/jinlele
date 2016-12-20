package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.GoodCatogoryMapper;
import com.jinlele.dao.GoodMapper;
import com.jinlele.service.interfaces.IIndexService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/17 0017.
 * 首页服务层实现类
 */
@Service
public class IndexServiceImpl implements IIndexService {
    @Resource
    BaseMapper baseMapper;
    @Resource
    GoodCatogoryMapper goodCatogoryMapper;
    @Resource
    GoodMapper goodMapper;

    /**
     * 获取首页数据展示
     */
    @Override
    public Map<String, Object> getIndexInfo() {
        Map<String, Object> indexMap = new HashMap();
        //首页一级分类获取
        List firstCatogoryList = goodCatogoryMapper.getFirstCatogory();
        //首页二级分类获取
        List secondCatogoryList = goodCatogoryMapper.getSecondCatogory();
        //新品推荐展示
        List newProductsList = goodMapper.getNewProducts();
        indexMap.put("firstCatogory", firstCatogoryList);
        indexMap.put("secondCatogory", secondCatogoryList);
        indexMap.put("newProducts", newProductsList);
        return indexMap;
    }

    /**
     * 首页新品推荐分页显示
     */

    @Override
    public Map<String, Object> getNewProductsPaging(int pagenow) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " good ");
        paramMap.put("fields", " id ,title,bannerurl,saleprice,discprice,description ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", " ishotCode ='001' ");
        paramMap.put("orderField", " create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }


}
