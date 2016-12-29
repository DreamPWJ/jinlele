package com.jinlele.controller;

import com.jinlele.model.ShoppingCart;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.service.interfaces.IShoppingCartService;
import net.sf.json.JSONArray;
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
     *
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
     * 删除购物车商品
     */
    @ResponseBody
    @RequestMapping(value = "/deleteShoppingCart/{userid}/{gcIdStr}",method = RequestMethod.GET)
    public int deleteShoppingCart(@PathVariable int userid,@PathVariable String gcIdStr) {
        String[] gcIds=gcIdStr.split("-");
        int count =0;
        for (int i=0;i<gcIds.length;i++){
            count+=shoppingCartService.deleteByUserIdGcid(userid,Integer.valueOf(gcIds[i]));
        }
        if(count==gcIds.length){
            return 1;
        }
        return 0;
    }

    /**
     * 商城订单列表
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderListDetail/{pagenow}/{userid}", method = RequestMethod.GET)
    public Map<String, Object> getOrderListDetail(@PathVariable int pagenow, @PathVariable int userid) {
        return orderService.getOrderListDetail(orderService.getShopListPaging(pagenow, userid));
    }

    /**
     * 根据订单号查询订单及其详情
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderDetailInfo/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> getOrderDetailInfo(@PathVariable String orderno) {
        return orderService.selectOrderDetailByOrderno(orderno);
    }

    /**
     * 更改订单状态
     */
    @ResponseBody
    @RequestMapping(value = "/updateOrderStatus/{orderno}", method = RequestMethod.GET)
    public Map<String, Object> updateOrderStatus(@PathVariable String orderno) {
        return orderService.updateOrderStatusByOrderno(orderno);
    }

    /**
     * 生成订单
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrder" ,method = RequestMethod.GET)
    public  Map<String, Object> saveOrder(Double totalprice,Integer totalnum ,Integer userId,Integer storeId,String chars){
        JSONArray json = new JSONArray().fromObject(chars.replaceAll("&quot;","\"")); // 首先把字符串转成 JSONArray  对象，json 对象值使用""双引号存储
        return orderService.saveOrder(totalprice,totalnum,userId,storeId ,json);
    }

}
