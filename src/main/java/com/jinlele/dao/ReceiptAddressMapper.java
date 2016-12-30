package com.jinlele.dao;

import com.jinlele.model.ReceiptAddress;

import java.util.Map;

public interface ReceiptAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ReceiptAddress record);

    int insertSelective(ReceiptAddress record);

    ReceiptAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReceiptAddress record);

    int updateByPrimaryKey(ReceiptAddress record);

    /**
     * 根据明细查id
     */
    ReceiptAddress selectByUniqueKey(ReceiptAddress receiptAddress);
    /**
     * 获取最新地址记录
     */
    Map<String,Object> getLatestInfo(Integer userid);
}