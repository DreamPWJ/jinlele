package com.jinlele.controller;

import com.jinlele.model.ShoppingCart;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.service.interfaces.IShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 更新或添加购物车
     * @param cart  购物车
     * @return   购物车商品数量
     */
    @ResponseBody
    @RequestMapping(value = "/addtocart",method = RequestMethod.GET)
    public int AddShoppingCart(ShoppingCart cart) {
        //处理业务逻辑 ，有记录就更新数量，没有就插入
        //查询是否有该用户和商品的 购物车(001)数据
       return shoppingCartService.addShoppingCart(cart);
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
