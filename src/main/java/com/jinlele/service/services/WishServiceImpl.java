package com.jinlele.service.services;

import com.jinlele.dao.WishMapper;
import com.jinlele.model.Wish;
import com.jinlele.service.interfaces.IWishService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/1/9.
 */
@Service
public class WishServiceImpl implements IWishService {
    @Resource
    WishMapper wishMapper;
    @Override
    public int insertSelective(Wish record) {
        return wishMapper.insertSelective(record);
    }
}
