package com.jinlele.controller;

import com.jinlele.service.interfaces.IGoodCatogoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/19.
 */
@Controller
@RequestMapping("/good")
public class GoodController {
@Resource
    IGoodCatogoryService goodCatogoryService;

    @ResponseBody
    @RequestMapping(value = "/getFirstCatogotory", method = RequestMethod.GET)
    public Map<String, Object> getFirstCatogotory() {
        return goodCatogoryService.getFirstCatogory();
    }
}
