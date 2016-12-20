package com.jinlele.controller;

import com.jinlele.service.interfaces.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pwj  on 2016/12/16 0016.
 * 用户相关Controller
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    IUserService userService;
    /**
     * 获取用户的分页方法
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/getUserPaging",method = RequestMethod.GET)
    public Map<String,Object> getUserPaging(){
        Map<String, Object> userparamMap=userService.getUserPaging();
        return userparamMap;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserInfo/{openid}",method = RequestMethod.GET)
    public Map<String,Object> getUserInfo(@PathVariable("openid") String openid){
        Map<String,Object> map=new HashMap();
        map.put("userInfo",userService.getUserInfo(openid));
        return  map;
    }

}
