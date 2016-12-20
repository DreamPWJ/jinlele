package com.jinlele.dao;

import com.jinlele.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User getUserInfo(String openid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}