package com.jinlele.controller;

import com.jinlele.service.interfaces.IIndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/17 0017.
 * 首页Controller
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    @Resource
    IIndexService indexService;
    /**
     * 获取首页数据展示
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getIndexInfo", method = RequestMethod.GET)
    public Map<String, Object> getIndexInfo() {
        return indexService.getIndexInfo();
    }

}
