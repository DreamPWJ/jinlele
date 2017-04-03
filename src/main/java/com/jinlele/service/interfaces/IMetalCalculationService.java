package com.jinlele.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */
public interface IMetalCalculationService {
    //获取估价结果
    Map<String, Object> addPMPrice(String purity, Double weight,Boolean flag);

    //查询子集
    List getSubSet(String category, Integer pid);

    //获取材质
    List getMaterial();
    //获取品质
    List getQuality();
}
