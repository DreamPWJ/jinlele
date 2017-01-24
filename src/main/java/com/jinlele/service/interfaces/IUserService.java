package com.jinlele.service.interfaces;

import com.jinlele.model.User;

import java.util.Map;

/**
 * Created by pwj  on 2016/12/16 0016.
 * 用户相关的Service接口
 */
public interface IUserService {

    int insertSelective(User record);
    int updateByPrimaryKeySelective(User record);

    /**
     * 获取用户的分页方法
     */
     Map<String,Object> getUserPaging();

    User getUserInfo(String openid);

    /**
     * 创建虚拟账户
     */
    void insertSelective(String openid , Integer userId);
}
