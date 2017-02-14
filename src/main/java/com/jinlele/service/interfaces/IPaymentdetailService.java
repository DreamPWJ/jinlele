package com.jinlele.service.interfaces;

import java.util.Map;

/**
 * Created by Administrator on 2017-2-14.
 */
public interface IPaymentdetailService {

    /**
     *账户提现充值列表
     */
    Map<String, Object> getPayDetailListPaging(int pagenow, int userid);
}
