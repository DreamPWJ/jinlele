package com.jinlele.controller;

import com.jinlele.service.interfaces.IShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20 0020.
 * 订单相关的Controller
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    IShoppingCartService shoppingCartService;

    /**
     * 分页获取购物车数据
     * @param pagenow
     * @param userid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getGoodList/{pagenow}/{userid}", method = RequestMethod.GET)
    public Map<String, Object> getGoodList(@PathVariable int pagenow, @PathVariable  int userid) {
        return shoppingCartService.getShoppingCartPaging(pagenow, userid);
    }
}
