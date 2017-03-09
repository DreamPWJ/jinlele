package com.jinlele.service.services;

import com.jinlele.dao.DiamondCalculationMapper;
import com.jinlele.dao.DiamondParmCalculationMapper;
import com.jinlele.dao.DiamondSideCalulationMapper;
import com.jinlele.model.DiamondCalculation;
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
    @Override
    public Map<String, Object> getDiamondPrice(List<Map<String,Object>> list) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("showFormula",false);
        for (Map<String, Object> paras : list) {
            Boolean flag = Boolean.valueOf(paras.get("flag").toString());
            String src=paras.get("src").toString();
            Double mainWeight = Double.valueOf((null== paras.get("mainWeight")|| paras.get("mainWeight").toString().length()==0)? "0" : paras.get("mainWeight").toString());//主石重量
            String weightLevel = "";
            Double result = 0.0;
            if(mainWeight>=0.08&&mainWeight<0.13){
                weightLevel="001";
            }else if(mainWeight>=0.13&&mainWeight<0.18){
                weightLevel="002";
            }else if(mainWeight>=0.18&&mainWeight<0.23){
                weightLevel="003";
            }else if(mainWeight>=0.23&&mainWeight<0.3){
                weightLevel="004";
            }else if(mainWeight>=0.3&&mainWeight<0.4){
                weightLevel="005";
            }else if(mainWeight>=0.3&&mainWeight<0.4){
            }else if(mainWeight>=0.4&&mainWeight<0.5){
                weightLevel="006";
            }else if(mainWeight>=0.5&&mainWeight<0.6){
                weightLevel="007";
            }else if(mainWeight>=0.6&&mainWeight<0.7){
                weightLevel="008";
            }
            //根据weightLevel查询id及price
            Map<String,Object> map = diamondCalculationMapper.getMainPriceInfo(weightLevel);
            Integer dcid =Integer.valueOf(map.get("id").toString());
            Double mainPrice = Double.valueOf(map.get("price").toString());
            Double secWeight = Double.valueOf((null== paras.get("secWeight")|| paras.get("secWeight").toString().length()==0)? "0" : paras.get("secWeight").toString());//副石重量
            String quality = paras.get("quality").toString();//副石品质
            Double secPrice=diamondSideCalulationMapper.getDiamondSidePrice(quality);//副石价格
            Double totalSecPrice = (new BigDecimal(secPrice * secWeight)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();//副石总价格

            Double totalWeight = Double.valueOf((null== paras.get("totalWeight")|| paras.get("totalWeight").toString().length()==0)? "0" : paras.get("totalWeight").toString());//副石重量
            String material = paras.get("material").toString();//镶嵌材质
            Double materialWeight = (new BigDecimal(totalWeight - (mainWeight +secWeight)*0.2)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            Map<String,Object> metalMap =  metalCalculationService.getPMPrice(material, materialWeight);
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
                resultMap.put("result",result);
            }else {
                //镶嵌材质+副石
                result = totalSecPrice+totalMetalPrice;
                resultMap.put("result", result);
            }
        }
        return resultMap;
    }
}
