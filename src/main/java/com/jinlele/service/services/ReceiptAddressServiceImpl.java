package com.jinlele.service.services;

import com.jinlele.dao.ReceiptAddressMapper;
import com.jinlele.model.ReceiptAddress;
import com.jinlele.service.interfaces.IReceiptAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016-12-29.
 */
@Service
public class ReceiptAddressServiceImpl  implements IReceiptAddressService{

    @Resource
    ReceiptAddressMapper receiptAddressMapper;


    @Override
    public int insertSelective(ReceiptAddress record) {
        return receiptAddressMapper.insertSelective(record);
    }
}
