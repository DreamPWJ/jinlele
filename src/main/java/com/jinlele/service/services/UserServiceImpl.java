package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.CashApplyMapper;
import com.jinlele.dao.UserMapper;
import com.jinlele.dao.WalletMapper;
import com.jinlele.model.CashApply;
import com.jinlele.model.User;
import com.jinlele.model.Wallet;
import com.jinlele.service.interfaces.IUserService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.StringHelper;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pwj  on 2016/12/16 0016.
 * 用户相关的Service实现类
 */
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    BaseMapper baseMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    WalletMapper walletMapper;

    @Resource
    CashApplyMapper cashApplyMapper;


    public int insertSelective(User record){
        return   userMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 获取用户的分页方法
     */
    @Override
    public Map<String, Object> getUserPaging() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " admin_user ");
        paramMap.put("fields", " * ");
        paramMap.put("pageNow", SysConstants.PAGENOW);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
/*      paramMap.put("wherecase",null);
        paramMap.put("orderField", null);*/
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }

    @Override
    public User getUserInfo(String openid) {
        return userMapper.getUserInfo(openid);
    }
    @Override
    public String findWalletAccount(Integer userId){
        return  walletMapper.selectWalletByUserId(userId);
    }
    @Override
    public Double selectWalletBalanceByUserId(Integer userId){
        return  walletMapper.selectWalletBalanceByUserId(userId);
    }

    @Override
    public int saveCashApply(Integer userId, Double applyMoney) {
        return cashApplyMapper.insertSelective(new CashApply(applyMoney , userId ,"001"));
    }

    @Override
    public List<Map<String, Object>> getAllRecords(Integer userId) {
        return cashApplyMapper.getAllRecords(userId);
    }

    @Override
    public void insertWallet(String openid , Integer userId) {
        String substrOpendid = openid.substring(openid.length()-4 , openid.length());
        String walletno = "JLL" + StringHelper.getOrderNum() + substrOpendid;
        Wallet wallet = new Wallet(walletno , userId);
        //规则商城虚拟账户的规则 JLL + '订单号规则' + opendid后四位
        //"JLL"+ StringHelper.getOrderNum() +
        walletMapper.insertSelective(wallet);
        System.out.println("创建成功");
    }


}
