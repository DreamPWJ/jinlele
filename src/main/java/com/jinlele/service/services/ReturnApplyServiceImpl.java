package com.jinlele.service.services;

import com.jinlele.dao.ReturnApplyMapper;
import com.jinlele.dao.ShopOrderMapper;
import com.jinlele.model.ReturnApply;
import com.jinlele.model.ShopOrder;
import com.jinlele.service.interfaces.IReturnApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/19.
 */
@Service
public class ReturnApplyServiceImpl implements IReturnApplyService{
    @Resource
    ReturnApplyMapper returnApplyMapper;
    @Resource
    ShopOrderMapper shopOrderMapper;

    @Override
    public Map<String, Object> addReturnApply(ReturnApply returnApply,String type) {
        Map<String, Object> resultMap = new HashMap<>();
        List returnApplyInfo = returnApplyMapper.getReturnApplyInfo(returnApply.getOrderNo());
        switch (returnApplyInfo.size()) {
            case 0:
                //插入新数据
                returnApplyMapper.insertSelective(returnApply);
                //todo 修改订单状态
                switch (type){
                    case "006":
                        ShopOrder shopOrder=new ShopOrder();
                        shopOrder.setOrderno(returnApply.getOrderNo());
                        shopOrder.setShoporderstatuscode("008");//退款
                        shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
                        break;



                }
                resultMap.put("result", "ok");
                break;
            default:
                //不再执行数据提交
                resultMap.put("result", "cancel");
                break;
        }
        return resultMap;
    }
}
