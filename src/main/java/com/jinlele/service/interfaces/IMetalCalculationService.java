package com.jinlele.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */
public interface IMetalCalculationService {
    //获取估价结果
    Map<String, Object> getEstimatePrice(String purity, Double weight);

    //获取纯度分类
    List getPurity(Integer pid);
}
