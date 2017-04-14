package com.jinlele.service.services;
import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.GoodCatogoryMapper;
import com.jinlele.dao.GoodMapper;
import com.jinlele.dao.ShopBannerMapper;
import com.jinlele.service.interfaces.IIndexService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.apache.commons.lang3.StringUtils;
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
    @Resource
    ShopBannerMapper shopBannerMapper;

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
        //获取所有的海报信息
        List<Map<String , Object>> banners = shopBannerMapper.listBanners();
        //新品推荐展示
        List newProductsList = goodMapper.getNewProducts();
        indexMap.put("firstCatogory", firstCatogoryList);
        indexMap.put("secondCatogory", secondCatogoryList);
        indexMap.put("newProducts", newProductsList);
        indexMap.put("banners", banners);
        return indexMap;
    }

    /**
     * 首页新品推荐分页显示
     */
    @Override
    public Map<String, Object> getNewProductsPaging(int pagenow, String searchcontent) {
        StringBuilder wherecase = new StringBuilder(" g.id = gc.gid and ishotCode ='001' and deleteCode = '001' ");
        if (!("".equals(searchcontent)||searchcontent==null)) {
            wherecase.append(" and g.title like'%" + searchcontent + "%'");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " good g,(SELECT min(price) as minprice,id cid,good_id gid FROM goodchild group by good_id) gc ");
        paramMap.put("fields", " g.id ,g.title,g.hotimgurl,g.oldprice,gc.minprice,g.description,g.shortinfo ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", 8);
        paramMap.put("wherecase", wherecase.toString());
        paramMap.put("orderField", " create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }


}
