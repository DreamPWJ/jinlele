package com.jinlele.service.interfaces;

import com.jinlele.model.AdminUser;

import java.util.Map;

/**
 * Created by pwj  on 2016/12/15 0015.
 * 测试用的service接口
 */
public interface ITestService {
    Map<String, Object> getUser();

    AdminUser getAdminUser(int id);
}
