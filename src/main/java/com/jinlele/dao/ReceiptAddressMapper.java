package com.jinlele.dao;

import com.jinlele.model.ReceiptAddress;

public interface ReceiptAddressMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ReceiptAddress record);

    int insertSelective(ReceiptAddress record);

    ReceiptAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReceiptAddress record);

    int updateByPrimaryKey(ReceiptAddress record);
}