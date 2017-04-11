package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.DayPrice;
import com.jinlele.model.EvaluateMetal;
import com.jinlele.model.ExchangeChart;
import com.jinlele.model.MetalCalculation;
import com.jinlele.service.interfaces.IMetalCalculationService;
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
    @Resource
    ServiceMapper serviceMapper;
    @Resource
    EvaluateMetalMapper evaluateMetalMapper;
    @Resource
    ExchangeChartMapper exchangeChartMapper;

    @Override
    public List getSubSet(String category, Integer pid) {
        return metalCalculationMapper.getSubSet(category, pid);
    }

    @Override
    public List getMaterial() {
        return metalCalculationMapper.getMaterial();
    }

    @Override
    public List getQuality() {
        return metalCalculationMapper.getQuality();
    }

    @Override
    public Map<String, Object> addPMPrice(String purity, Double weight, Integer goodId, Integer goodChildId,Boolean flag) {
        Map<String, Object> resultMap = new HashMap<>();
        List<DayPrice> list = dayPriceMapper.getCurrentPrice();
        Double goldPrice = 0.0;
        Double boPrice = 0.0;
        Double baPrice = 0.0;
        Double silverPrice = 0.0;
        for (int i = 0; i < list.size(); i++) {
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
        Double depreciation = (new BigDecimal(weight * metalCalculation.getDepreciation())).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();//折旧费
        switch (type) {
            case "001":
            case "003":
            case "006":
                price = goldPrice * metalCalculation.getRatio() / 100 - metalCalculation.getAdded();
                break;
            case "002":
                price = boPrice * metalCalculation.getRatio() / 100 - metalCalculation.getAdded();
                break;
            case "004":
                price = baPrice * metalCalculation.getRatio() / 100 - metalCalculation.getAdded();
                break;
            case "005":
            case "007":
                price = silverPrice * metalCalculation.getRatio() / 100 - metalCalculation.getAdded();
                break;
        }
        price = (new BigDecimal(price)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        Double totalprice = (new BigDecimal(weight * price)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        Double result = totalprice - depreciation;

        //flag：为true，则添加记录
        if(flag) {
            com.jinlele.model.Service service = new com.jinlele.model.Service();
            service.setPrice(result);//估价结果
            serviceMapper.insertSelective(service);
            evaluateMetalMapper.insertSelective(new EvaluateMetal(service.getId(), type, purity, weight, totalprice));
            resultMap.put("evaluateServiceId", service.getId());//返回serviceid
            //添加换款购物车
            if(goodId!=0&&goodChildId!=0){
                exchangeChartMapper.insertSelective(new ExchangeChart(service.getId(),goodId,goodChildId,1,1));
            }
        }
        //金价  重量   折旧费  结果
        resultMap.put("showFormula",true);
        resultMap.put("type", type);
        resultMap.put("price", price);
        resultMap.put("weight", weight);
        resultMap.put("depreciation", depreciation);
        resultMap.put("result", result);
        return resultMap;
    }
}
