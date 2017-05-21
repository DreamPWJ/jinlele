package com.jinlele.controller;

import com.jinlele.service.interfaces.IIndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/getIndexInfo/{userId}", method = RequestMethod.GET)
    public Map<String, Object> getIndexInfo(@PathVariable Integer userId) {
        Map<String, Object> indexmap=indexService.getIndexInfo(userId);
//        indexmap.put("openId",WeiXinController.openIds);
//        indexmap.put("userId",WeiXinController.userIds);

        return indexmap;
    }

    /**
     * 首页新品推荐分页显示
     *
     * @param pagenow 当前页
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getNewProducts/{pagenow}", method = RequestMethod.GET)
    public Map<String, Object> getNewProducts(@PathVariable int pagenow,@RequestParam(value = "searchcontent",required = false) String searchcontent) {
        return indexService.getNewProductsPaging(pagenow,searchcontent);
    }
}
