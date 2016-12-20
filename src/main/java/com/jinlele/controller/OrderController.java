package com.jinlele.controller;

import com.jinlele.model.ShoppingCart;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.service.interfaces.IShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @Resource
    IOrderService orderService;

    /**
     * 分页获取购物车数据
     * @param pagenow
     * @param userid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCartList/{pagenow}/{userid}", method = RequestMethod.GET)
    public Map<String, Object> getCartList(@PathVariable int pagenow, @PathVariable  int userid) {
        return  shoppingCartService.getShoppingCartPaging(pagenow, userid);
    }

    /**
     * 添加到购物车
     * @param cart
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addtocart",method = RequestMethod.POST)
    public int AddShoppingCart(@RequestBody ShoppingCart cart) {
        return shoppingCartService.insertSelective(cart);
    }

    /**
     *商城订单列表
     */
    @ResponseBody
    @RequestMapping(value = "/getShopList/{pagenow}/{userid}", method = RequestMethod.GET)
    public Map<String, Object> getShopList(@PathVariable int pagenow, @PathVariable  int userid) {
       return orderService.getShopListPaging(pagenow, userid);
    }
}