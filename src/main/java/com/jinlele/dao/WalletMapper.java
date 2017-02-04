package com.jinlele.dao;

import com.jinlele.model.Wallet;

public interface WalletMapper {
    int deleteByPrimaryKey(String walletno);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(String walletno);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);

    String selectWalletByUserId(Integer userId);

    Double selectWalletBalanceByUserId(Integer userId);


}