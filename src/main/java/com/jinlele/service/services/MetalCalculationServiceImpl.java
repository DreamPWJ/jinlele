package com.jinlele.service.services;

import com.jinlele.dao.DayPriceMapper;
import com.jinlele.dao.MetalCalculationMapper;
import com.jinlele.model.DayPrice;
import com.jinlele.model.MetalCalculation;
import com.jinlele.service.interfaces.IMetalCalculationService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */
@Service
public class MetalCalculationServiceImpl implements IMetalCalculationService {
    @Resource
    DayPriceMapper dayPriceMapper;
    @Resource
    MetalCalculationMapper metalCalculationMapper;

    @Override
    public List getPurity(Integer pid) {
        return metalCalculationMapper.getPurity(pid);
    }

    @Override
    public Map<String, Object> getEstimatePrice(String purity, Double weight) {
        Map<String, Object> resultMap = new HashMap<>();
        List<DayPrice> list = dayPriceMapper.getCurrentPrice();
        Double goldPrice = 0.0;
        Double boPrice = 0.0;
        Double baPrice = 0.0;
        Double silverPrice = 0.0;
        for(int i=0;i<list.size();i++){
            if (list.get(i).getName().equals("黄金")) {
                goldPrice = list.get(i).getPrice();
            }
            if (list.get(i).getName().equals("铂金")) {
                boPrice = list.get(i).getPrice();
            }
            if (list.get(i).getName().equals("钯金")) {
                baPrice = list.get(i).getPrice();
            }
            if (list.get(i).getName().equals("白银")) {
                silverPrice = list.get(i).getPrice();
            }
        }
        String type = purity.substring(0, 3);
        MetalCalculation metalCalculation = metalCalculationMapper.selectByUQ(type, purity);
        Double price = 0.0;//相应贵金属价格
        Double depreciation=(new BigDecimal(weight*metalCalculation.getDepreciation())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//折旧费
        switch (type) {
            case "001":
            case "003":
            case "006":
                price=goldPrice*metalCalculation.getRatio()/100-metalCalculation.getAdded();
                break;
            case "002":
                price=boPrice*metalCalculation.getRatio()/100-metalCalculation.getAdded();
                break;
            case "004":
                price=baPrice*metalCalculation.getRatio()/100-metalCalculation.getAdded();
                break;
            case "005":
            case "007":
                price=silverPrice*metalCalculation.getRatio()/100-metalCalculation.getAdded();
                break;
        }
        price=(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Double totalprice = weight*price;
        Double result =totalprice-depreciation;
        //金价  重量   折旧费  结果
        resultMap.put("price",price);
        resultMap.put("weight",weight);
        resultMap.put("depreciation",depreciation);
        resultMap.put("result",result);
        return resultMap;
    }
}
