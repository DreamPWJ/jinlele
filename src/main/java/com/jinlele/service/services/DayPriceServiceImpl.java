package com.jinlele.service.services;

import com.jinlele.dao.DayPriceMapper;
import com.jinlele.service.interfaces.IDayPriceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/4.
 */
@Service
public class DayPriceServiceImpl implements IDayPriceService {
    @Resource
    DayPriceMapper dayPriceMapper;
    @Override
    public Map<String,Object> getCurrentPrice() {
        Map<String,Object> map=new HashMap<>();
        map.put("dayprice",dayPriceMapper.getCurrentPrice());
        return map;
    }
}
