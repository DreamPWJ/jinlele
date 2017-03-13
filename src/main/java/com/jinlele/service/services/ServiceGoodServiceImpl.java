package com.jinlele.service.services;

import com.jinlele.dao.ServiceGoodMapper;
import com.jinlele.dao.ShopOrderMapper;
import com.jinlele.model.ServiceGood;
import com.jinlele.model.ShopOrder;
import com.jinlele.service.interfaces.IServiceGoodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/12.
 */
@Service
public class ServiceGoodServiceImpl implements IServiceGoodService {
    @Resource
    ServiceGoodMapper serviceGoodMapper;
    @Resource
    ShopOrderMapper shopOrderMapper;

    @Override
    public int insertSelective(ServiceGood record) {
        try{
            ShopOrder order = new ShopOrder();
            order.setShoporderstatuscode("005007");//待付款
            order.setOrderno(record.getOrderno());
            shopOrderMapper.updateByPrimaryKeySelective(order);
            return serviceGoodMapper.insertSelective(record);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public Map<String, Object> getGoodId(String orderno) {
        return serviceGoodMapper.getGoodId(orderno);
    }
}
