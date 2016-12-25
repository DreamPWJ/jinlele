package com.jinlele.service.interfaces;

import com.jinlele.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Created by pwj  on 2016/12/16 0016.
 * 用户相关的Service接口
 */
public interface IUserService {

    int insertSelective(User record);


    /**
     * 获取用户的分页方法
     */
     Map<String,Object> getUserPaging();

    User getUserInfo(@Param("openid")String openid);
}
