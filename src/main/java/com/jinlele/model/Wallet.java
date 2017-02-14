package com.jinlele.model;

import java.util.Date;

public class Wallet {
    private String walletno;

    private String accountcode;

    private Double balance;

    private Integer userId;

    private String deletecode;

    private Date createTime;

    private Date updateTime;

    public Wallet() {
    }

    public Wallet(String walletno, Integer userId) {
        this.walletno = walletno;
        this.userId = userId;
    }

    public Wallet(String walletno, Double balance, Date updateTime) {
        this.walletno = walletno;
        this.balance = balance;
        this.updateTime = updateTime;
    }

    public String getWalletno() {
        return walletno;
    }

    public void setWalletno(String walletno) {
        this.walletno = walletno == null ? null : walletno.trim();
    }

    public String getAccountcode() {
        return accountcode;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode == null ? null : accountcode.trim();
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDeletecode() {
        return deletecode;
    }

    public void setDeletecode(String deletecode) {
        this.deletecode = deletecode == null ? null : deletecode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}