package com.jinlele.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/6.
 */
public interface IDiamondCalculationService {
    //获取钻石估价结果
    Map<String, Object> getDiamondPrice(List<Map<String,Object>> map);
}
