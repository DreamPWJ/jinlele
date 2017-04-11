package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.EvaluateDiamond;
import com.jinlele.model.ExchangeChart;
import com.jinlele.service.interfaces.IDiamondCalculationService;
import com.jinlele.service.interfaces.IMetalCalculationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/6.
 */
@Service
public class DiamondCalculationServiceImpl implements IDiamondCalculationService {
    @Resource
    DiamondCalculationMapper diamondCalculationMapper;
    @Resource
    DiamondParmCalculationMapper diamondParmCalculationMapper;
    @Resource
    DiamondSideCalulationMapper diamondSideCalulationMapper;
    @Resource
    IMetalCalculationService metalCalculationService;
    @Resource
    EvaluateDiamondMapper evaluateDiamondMapper;
    @Resource
    ServiceMapper serviceMapper;
    @Resource
    ExchangeChartMapper exchangeChartMapper;
    @Override
    public Map<String, Object> addDiamondPrice(List<Map<String,Object>> list) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("showFormula",false);
        for (Map<String, Object> paras : list) {
            Integer goodId =Integer.valueOf(paras.get("goodId").toString());
            Integer goodChildId =Integer.valueOf(paras.get("goodChildId").toString());
            Boolean flag = Boolean.valueOf(paras.get("flag").toString());
            String src=paras.get("src")!=null&&paras.get("src").toString().length()!=0?paras.get("src").toString():"";
            Double mainWeight = 0.0;//主石重量
            if(paras.get("mainWeight")!=null&& paras.get("mainWeight").toString().length()!=0){
                mainWeight = Double.valueOf(paras.get("mainWeight").toString());//主石重量
            }
           // String weightLevel = "";
            Double result = 0.0;
//            if(mainWeight>=0.08&&mainWeight<0.13){
//                weightLevel="001";
//            }else if(mainWeight>=0.13&&mainWeight<0.18){
//                weightLevel="002";
//            }else if(mainWeight>=0.18&&mainWeight<0.23){
//                weightLevel="003";
//            }else if(mainWeight>=0.23&&mainWeight<0.3){
//                weightLevel="004";
//            }else if(mainWeight>=0.3&&mainWeight<0.4){
//                weightLevel="005";
//            }else if(mainWeight>=0.4&&mainWeight<0.5){
//                weightLevel="006";
//            }else if(mainWeight>=0.5&&mainWeight<0.6){
//                weightLevel="007";
//            }else if(mainWeight>=0.6&&mainWeight<0.7){
//                weightLevel="008";
//            }
            //根据mainWeight查询id及price
            Map<String,Object> map = diamondCalculationMapper.getMainPriceInfo(mainWeight);
            Integer dcid =Integer.valueOf(map.get("id").toString());
            Double mainPrice = Double.valueOf(map.get("price").toString());
            Double secWeight = 0.0;//副石重量
            if(paras.get("secWeight")!=null&& paras.get("secWeight").toString().length()!=0){
                secWeight = Double.valueOf(paras.get("secWeight").toString());//副石重量
            }
            String quality = paras.get("quality").toString();//副石品质
            Double secPrice=diamondSideCalulationMapper.getDiamondSidePrice(quality);//副石价格
            Double totalSecPrice = (new BigDecimal(secPrice * secWeight)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();//副石总价格

            Double totalWeight = 0.0;//副石重量
            if( paras.get("totalWeight")!=null&& paras.get("totalWeight").toString().length()!=0) {
                totalWeight = Double.valueOf(paras.get("totalWeight").toString());//副石重量
            }
            String purity = paras.get("material").toString();//镶嵌材质
            Double materialWeight = (new BigDecimal(totalWeight - (mainWeight +secWeight)*0.2)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            Map<String,Object> metalMap =  metalCalculationService.addPMPrice(purity, materialWeight,goodId,goodChildId,false);
            Double totalMetalPrice = (new BigDecimal(Double.valueOf(metalMap.get("result").toString()))).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();//材质总价格
            if (flag||"recycle".equals(src)) {
                //主石+镶嵌材质+副石
                String certificate = paras.get("certificate").toString();//证书
                String color = paras.get("color").toString();//颜色
                String cleaness = paras.get("cleaness").toString();//净度
                String florescence = paras.get("florescence").toString();//荧光
                String cut = paras.get("cut").toString();//切工
                String symmetry = paras.get("symmetry").toString();//对称
                String polish = paras.get("polish").toString();//抛光
                //根据weightLevel的id，类型及spec查询rate
                Double certificateRate = diamondParmCalculationMapper.getRate(certificate.substring(0, 3), certificate, dcid);
                Double colorRate = diamondParmCalculationMapper.getRate(color.substring(0, 3), color, dcid);
                Double cleanessRate = diamondParmCalculationMapper.getRate(cleaness.substring(0, 3), cleaness, dcid);
                Double florescenceRate = diamondParmCalculationMapper.getRate(florescence.substring(0, 3), florescence, dcid);
                Double cutRate = diamondParmCalculationMapper.getRate(cut.substring(0, 3), cut, dcid);
                Double symmetryRate = diamondParmCalculationMapper.getRate(symmetry.substring(0, 3), symmetry, dcid);
                Double polishRate = diamondParmCalculationMapper.getRate(polish.substring(0, 3), polish, dcid);
                Double totalMainPrice = (new BigDecimal(mainPrice * mainWeight * certificateRate * colorRate * cleanessRate * florescenceRate * cutRate * symmetryRate * polishRate)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();//主石总价格
                result = totalMainPrice+totalSecPrice+totalMetalPrice;
                com.jinlele.model.Service service =new com.jinlele.model.Service();
                service.setPrice(result);//估价结果
                serviceMapper.insertSelective(service);
                //添加换款购物车
                if(goodId!=0&&goodChildId!=0){
                    exchangeChartMapper.insertSelective(new ExchangeChart(service.getId(),goodId,goodChildId,1,1));
                }
                evaluateDiamondMapper.insertSelective(new EvaluateDiamond(service.getId(),metalMap.get("type").toString(),purity,certificate,color,cleaness,florescence,cut,symmetry,polish,quality,totalMetalPrice,totalMainPrice,totalSecPrice,materialWeight,mainWeight,secWeight));
                resultMap.put("mainPrice",totalMainPrice);
                resultMap.put("secPrice",totalSecPrice);
                resultMap.put("metalPrice",totalMetalPrice);
                resultMap.put("result",result);
                resultMap.put("evaluateServiceId", service.getId());//返回serviceid
            }else {
                //镶嵌材质+副石
                result = totalSecPrice+totalMetalPrice;
                com.jinlele.model.Service service =new com.jinlele.model.Service();
                service.setPrice(result);//估价结果
                serviceMapper.insertSelective(service);
                //添加换款购物车
                if(goodId!=0&&goodChildId!=0){
                    exchangeChartMapper.insertSelective(new ExchangeChart(service.getId(),goodId,goodChildId,1,1));
                }
                evaluateDiamondMapper.insertSelective(new EvaluateDiamond(service.getId(),metalMap.get("type").toString(),purity,quality,totalMetalPrice,totalSecPrice,materialWeight,mainWeight,secWeight));
                resultMap.put("mainPrice","");
                resultMap.put("secPrice",totalSecPrice);
                resultMap.put("metalPrice",totalMetalPrice);
                resultMap.put("result", result);
                resultMap.put("evaluateServiceId", service.getId());//返回serviceid
            }
        }
        return resultMap;
    }
}
