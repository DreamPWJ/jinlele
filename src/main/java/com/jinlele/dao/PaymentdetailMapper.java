package com.jinlele.dao;

import com.jinlele.model.Paymentdetail;

public interface PaymentdetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Paymentdetail record);

    int insertSelective(Paymentdetail record);

    Paymentdetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Paymentdetail record);

    int updateByPrimaryKey(Paymentdetail record);
}