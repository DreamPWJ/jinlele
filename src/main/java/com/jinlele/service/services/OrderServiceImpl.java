package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 * 订单相关的服务类实现
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    BaseMapper baseMapper;

    /**
     * 商城订单列表
     */
    @Override
    public Map<String, Object> getShopListPaging(int pagenow, int userid) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " shoporder ");
        paramMap.put("fields", " * ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", " ");
        paramMap.put("orderField", " create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }

}
