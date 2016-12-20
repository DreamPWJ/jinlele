package com.jinlele.controller;

import com.jinlele.service.interfaces.IGoodCatogoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/19.
 * 商品相关的Controller
 */
@Controller
@RequestMapping("/good")
public class GoodController {
    @Resource
    IGoodCatogoryService goodCatogoryService;

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
    public  Map<String, Object> getGoodList(@PathVariable int  pagenow,@PathVariable String  categoryname,@PathVariable int  querytype) {
        return goodCatogoryService.getGoodListPaging(pagenow, categoryname, querytype);
    }
}
