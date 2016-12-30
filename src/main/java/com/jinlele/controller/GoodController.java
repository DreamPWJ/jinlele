package com.jinlele.controller;

import com.jinlele.service.interfaces.IGoodCatogoryService;
import com.jinlele.service.interfaces.IGoodService;
import com.jinlele.service.interfaces.IShoppingCartService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/19.
 * 商品相关的Controller
 */
@Controller
@RequestMapping("/good")
public class GoodController {
    @Resource
    IGoodService goodService;
    @Resource
    IGoodCatogoryService goodCatogoryService;
    @Resource
    IShoppingCartService shoppingCartService;

    /**
     * 获取一级分类
     */
    @ResponseBody
    @RequestMapping(value = "/getFirstCatogotory", method = RequestMethod.GET)
    public Map<String, Object> getFirstCatogotory() {
        return goodCatogoryService.getFirstCatogory();
    }

    /**
     * 获取产品列表
     * @param categoryname 二级分类名称
     * @param querytype  查询条件 综合 0  最新 1 价格从高到低 2 价格从低到高 3
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getGoodList/{pagenow}/{categoryname}/{querytype}", method = RequestMethod.GET)
    public Map<String, Object> getGoodList(@PathVariable int pagenow, @PathVariable String categoryname, @PathVariable int querytype) {
        return goodCatogoryService.getGoodListPaging(pagenow, categoryname, querytype);
    }

    /**
     * 获取产品详情
     * @param goodId 商品id
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getGoodDetail/{goodId}/{userId}", method = RequestMethod.GET)
    public Map<String, Object> getGoodDetail(@PathVariable int goodId,@PathVariable int userId) {
        Map<String, Object> newMap = new HashedMap();
        Map<String, Object> goodmap = goodService.getGoodDetail(goodId);   //获得商品详情信息
        int totalnum = shoppingCartService.getShopcharTotalNum(userId);  //初始页面时，获得该用户加入购车商品总数量
        List<Map<String, Object>>  goodchilds = goodService.getGoodChildsByGoodId(goodId);
        newMap.put("good" , goodmap);
        newMap.put("goodchilds" , goodchilds);
        newMap.put("totalnum" , totalnum);
        return newMap;
    }

    @ResponseBody
    @RequestMapping("/getSecondCatogaryByPid/{pid}")
    public List getSecondCatogaryByPid(@PathVariable("pid") int pid){
        return goodCatogoryService.getSecondCatogaryByPid(pid);
    }

    /**
     * 获取产品列表
     * @param pagenow     当前页码
     * @param catogoryId  二级分类id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGoodsByCidPaging/{pagenow}/{catogoryId}")
    public  Map<String, Object>  getGoodsByCidPaging(@PathVariable("pagenow") int pagenow ,@PathVariable("catogoryId") int catogoryId){
        return goodCatogoryService.getGoodsByCidPaging(pagenow ,catogoryId);
    }


    /**
     * 根据一节分类 遍历二级分类 不分页
     * @param pid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getSecondCatogByPid/{pid}")
    public List getSecondCatogByPid(@PathVariable("pid") int pid){
        return goodCatogoryService.getSecondCatogByPid(pid);
    }

}
