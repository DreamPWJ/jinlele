package com.jinlele.service.services;

import com.jinlele.dao.PaymentdetailMapper;
import com.jinlele.dao.WalletMapper;
import com.jinlele.model.Paymentdetail;
import com.jinlele.model.Wallet;
import com.jinlele.service.interfaces.IWalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/12.
 */
@Service
public class WalletServiceImpl implements IWalletService {
    @Resource
    WalletMapper walletMapper;
    @Resource
    PaymentdetailMapper paymentdetailMapper;
    @Override
    public void updateWallet(Integer userId,Double leftAmount,String orderno) {
        //查询账户信息
        Map<String, Object> map = walletMapper.getWalletByUserId(userId);
        Double balance = (Double) map.get("balance");
        String walletno = (String) map.get("walletno");
        //更新账户信息
        balance = balance + leftAmount;
        Wallet wallet = new Wallet(walletno, balance, new Date());
        wallet.setUpdateTime(new Date());
        walletMapper.updateByPrimaryKeySelective(wallet);//更新虚拟账户
        //新增账户明细记录
        Paymentdetail paymentdetail = new Paymentdetail();
        paymentdetail.setWalletNo(walletno);
        paymentdetail.setChangemoney(leftAmount);
        paymentdetail.setBalance(balance);
        paymentdetail.setMemo("来自换款订单:"+orderno);
        paymentdetailMapper.insertSelective(paymentdetail);
    }
}
