package com.jinlele.service.services;

import com.jinlele.dao.AdminUserMapper;
import com.jinlele.dao.BaseMapper;
import com.jinlele.model.AdminUser;
import com.jinlele.service.interfaces.ITestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * created by pwj  on 2016/12/15 0015.
 *  测试用service
 */
@Service
public class TestServiceImpl implements ITestService {
    @Resource
    BaseMapper baseMapper;
    @Resource
    AdminUserMapper adminUserMapper;

    @Override
    public Map<String, Object> getUser() {
        Map<String,Object> paramMap=new HashMap<String, Object>();
        paramMap.put("tableName", "admin_user");
        paramMap.put("fields","*");
        paramMap.put("pageNow", 1);
        paramMap.put("pageSize", 10);
        this.baseMapper.getPaging(paramMap);
        return paramMap;
    }

    @Override
    public AdminUser getAdminUser(int id) {
        AdminUser adminUser=adminUserMapper.selectByPrimaryKey(id);
        return adminUser;
    }
}
