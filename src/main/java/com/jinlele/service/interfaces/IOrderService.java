package com.jinlele.service.interfaces;

import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 * 订单相关的服务类
 */
public interface IOrderService {
    /**
     *商城订单列表
     */
    Map<String, Object> getShopListPaging(int pagenow, int userid);
}
