package com.jinlele.service.interfaces;

import com.jinlele.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by pwj  on 2016/12/16 0016.
 * 用户相关的Service接口
 */
public interface IUserService {

    int insertSelective(User record);
    int updateByPrimaryKeySelective(User record);

    /**
     * 获取用户的分页方法
     */
     Map<String,Object> getUserPaging();

    User getUserInfo(String openid);

    String findWalletAccount(Integer userId);

    /**
     * 创建虚拟账户
     */
    void insertWallet(String openid , Integer userId);

    /**
     * 查询账户余额
     * @param userId
     * @return
     */
    Double selectWalletBalanceByUserId(Integer userId);

    /**
     * 保存申请提现的记录
     */
    int saveCashApply(Integer userId , Double applyMoney);

    /**
     * 得到所有的提现记录
     */
    List<Map<String , Object>> getAllRecords(Integer userId);

}
