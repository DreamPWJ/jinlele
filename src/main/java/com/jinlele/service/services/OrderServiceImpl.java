package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.ShopOrderGoodMapper;
import com.jinlele.dao.ShopOrderMapper;
import com.jinlele.dao.ShoppingCartMapper;
import com.jinlele.model.ShopOrder;
import com.jinlele.model.ShopOrderGood;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.StringHelper;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 * 订单相关的服务类实现
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    BaseMapper baseMapper;

    @Resource
    ShopOrderMapper orderMapper;

    @Resource
    ShoppingCartMapper cartMapper;

    @Resource
    ShopOrderGoodMapper shopOrderGoodMapper;

    /**
     * 商城订单列表
     */
    @Override
    public Map<String, Object> getShopListPaging(int pagenow, int userid) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " shoporder ");
        paramMap.put("fields", " * ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", " ");
        paramMap.put("orderField", " create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }

    public void saveOrder(Double totalprice,Integer totalnum ,Integer userId,Integer storeId,Integer[] goodchildIds){
        //订单号生成
        String orderno = StringHelper.getOrderNum();
        ShopOrder order = new ShopOrder(orderno ,totalprice ,totalnum ,userId ,storeId ,1,"001");
        order.setShoporderstatuscode("001");//设置订单状态 未付款
        //生成订单
        orderMapper.insertSelective(order);
        //订单_商品中间表数据添加
        for (int i =0 ,len= goodchildIds.length;i<len;i++){
            Integer goodchildId = goodchildIds[i];
            //根据选中的商品子表查询购物车中的id , goodid
            Map map = cartMapper.findCartIdAndGoodId(goodchildId);
            Integer cartId = (Integer) map.get("id");
            Integer goodId = (Integer) map.get("goodId");
            ShopOrderGood ordergood = new ShopOrderGood(orderno ,goodchildId ,goodId ,"001");
            //订单_商品中间表保存数据
            shopOrderGoodMapper.insertSelective(ordergood);
            //删除购物车中下单的数据
            cartMapper.deleteByPrimaryKey(cartId);
        }
    }

}
