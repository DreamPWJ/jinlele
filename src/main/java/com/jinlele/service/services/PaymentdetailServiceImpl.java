package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.PaymentdetailMapper;
import com.jinlele.service.interfaces.IPaymentdetailService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-2-14.
 */
@Service
public class PaymentdetailServiceImpl  implements IPaymentdetailService{

    @Resource
    BaseMapper baseMapper;

    @Resource
    PaymentdetailMapper paymentdetailMapper;

    /**
     * 账户提现充值列表
     */
    @Override
    public Map<String, Object> getPayDetailListPaging(int pagenow, int userid) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", "  paymentdetail  ");
        paramMap.put("fields", "  changeMoney, balance, create_time as createTime ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", " wallet_no = (select walletno from wallet where user_id = "+ userid +") and deleteCode = '001' ");
        paramMap.put("orderField", "  create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }



}
