package com.jinlele.service.services;

import com.jinlele.dao.ReceiptAddressMapper;
import com.jinlele.model.ReceiptAddress;
import com.jinlele.service.interfaces.IReceiptAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-29.
 */
@Service
public class ReceiptAddressServiceImpl  implements IReceiptAddressService{

    @Resource
    ReceiptAddressMapper receiptAddressMapper;


    @Override
    public Map<String, Object> createReceiptAddressId(ReceiptAddress record) {
        Map<String,Object> resultMap=new HashMap<>();
        //根据条件查记录,有记录，返回查询的id；无记录，返回新增记录id
        ReceiptAddress receiptAddress =receiptAddressMapper.selectByUniqueKey(record);
        if (receiptAddress!=null){
            resultMap.put("receiptAddressId",receiptAddress.getId());
            return  resultMap;
        }
        receiptAddressMapper.insertSelective(record);
        resultMap.put("receiptAddressId",record.getId());
        return resultMap;
    }

    @Override
    public Map<String, Object> getLatestInfo(Integer userid) {
        return receiptAddressMapper.getLatestInfo(userid);
    }
}
