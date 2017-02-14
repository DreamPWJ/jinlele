package com.jinlele.controller;

import com.jinlele.service.interfaces.IStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 门店Controller
 */
@Controller
@RequestMapping("/store")
public class StoreController {

    @Resource
    IStoreService storeService;

    @ResponseBody
    @RequestMapping("/findAllStores/{latitude}/{longitude}")
    public List<Map<String , Object>> findAllStores(@PathVariable String latitude,@PathVariable String longitude){
          return  storeService.findAllStores(latitude,longitude);
    }
}
