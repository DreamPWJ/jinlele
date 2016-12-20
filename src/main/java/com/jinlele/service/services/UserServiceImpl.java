package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.service.interfaces.IUserService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pwj  on 2016/12/16 0016.
 * 用户相关的Service实现类
 */
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    BaseMapper baseMapper;

    /**
     * 获取用户的分页方法
     */
    @Override
    public Map<String, Object> getUserPaging() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " admin_user ");
        paramMap.put("fields", " * ");
        paramMap.put("pageNow", SysConstants.PAGENOW);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
/*        paramMap.put("wherecase",null);
        paramMap.put("orderField", null);*/
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }
}
