package com.jinlele.service.interfaces;

import com.jinlele.model.ReceiptAddress;

import java.util.Map;

/**
 * Created by Administrator on 2016-12-29.
 */
public interface IReceiptAddressService {

    /**
     * 获取地址id
     */
    Map<String,Object> createReceiptAddressId(ReceiptAddress record);

    /**
     * 获取最新地址记录
     */
    Map<String,Object> getLatestInfo(Integer userid);

}
