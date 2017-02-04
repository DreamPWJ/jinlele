package com.jinlele.controller;

import com.jinlele.model.User;
import com.jinlele.model.Wish;
import com.jinlele.service.interfaces.IUserService;
import com.jinlele.service.interfaces.IWishService;
import com.jinlele.util.sms.sendSMS;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Resource
    IWishService wishService;
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

    /**
     * 获取用户信息
     * @param openid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserInfo/{openid}",method = RequestMethod.GET)
    public Map<String,Object> getUserInfo(@PathVariable("openid") String openid){
        Map<String,Object> map=new HashMap();
        map.put("userInfo",userService.getUserInfo(openid));
        return  map;
    }

    /**
     * 获得用户id的方法
     */
    @ResponseBody
    @RequestMapping("/getUserInfo")
    public Map<String,Object>  getUserInfo(HttpServletRequest request){
        Integer id = (Integer) request.getSession().getAttribute("jinlele_id");
        User userInfo = (User) request.getSession().getAttribute("userInfo");
        Map<String ,Object> map = new HashedMap();
        map.put("userId" , id);
        map.put("userInfo" , userInfo);
        return map;

    }

    /**
     * 提交用户建议反馈
     */
    @ResponseBody
    @RequestMapping(value = "/saveWish/{suggest}/{userId}" ,method = RequestMethod.GET)
    public Map<String, Object> saveWish(@PathVariable String suggest, @PathVariable int userId) {
        Map<String , Object> map = new HashedMap();
        int n = wishService.insertSelective(new Wish(suggest,userId));
        map.put("n" ,n);
        return map;
    }

    /**
     * 获取验证码
     */
    @ResponseBody
    @RequestMapping(value = "/getCheckcode/{phonenumber}" ,method = RequestMethod.GET)
    public Map<String, Object> getCheckcode(@PathVariable String phonenumber) {
        Map<String , Object> map = new HashedMap();
        try {
            String s =sendSMS.sendSimple(sendSMS.randomCode(), phonenumber);
            map.put("result" ,s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 绑定手机号码
     */
    @ResponseBody
    @RequestMapping(value = "/addPhoneNumber/{phoneNumber}/{userId}" ,method = RequestMethod.GET)
    public Map<String, Object> addPhoneNumber(@PathVariable String phoneNumber, @PathVariable int userId) {
        Map<String , Object> map = new HashedMap();
        User user =new User();
        user.setId(userId);
        user.setPhone(phoneNumber);
        int result = userService.updateByPrimaryKeySelective(user);
        map.put("result" ,result);
        return map;
    }

    /**
     * 获得账户的余额
     */
    @ResponseBody
    @RequestMapping(value = "/selectWalletBalanceByUserId/{userId}" ,method = RequestMethod.GET)
    public Map<String , Object> selectWalletBalanceByUserId(@PathVariable Integer userId){
          Double balance =  userService.selectWalletBalanceByUserId(userId);
          Map map = new HashedMap();
          map.put("balance" , balance);
          return map;
    }
}
