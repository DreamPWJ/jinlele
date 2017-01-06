package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.GoodChild;
import com.jinlele.model.ReceiptAddress;
import com.jinlele.model.ShopOrder;
import com.jinlele.model.ShopOrderGood;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.service.interfaces.IReceiptAddressService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.StringHelper;
import com.jinlele.util.SysConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Resource
    GoodChildMapper goodChildMapper;

    @Resource
    ReceiptAddressMapper receiptAddressMapper;

    @Resource
    IReceiptAddressService receiptAddressService;

    /**
     * 商城订单列表
     */
    @Override
    public Map<String, Object> getShopListPaging(int pagenow, int userid ,String type) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", "  shoporder  ");
        paramMap.put("fields", "  * ,case type when '001' then '翻新' when '002' then '维修' when '003' then '检测' when '004' then '回收' when '005' then '换款' when '006' then '商城' end as ordertype ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        if("ALL".equals(type)) {
            paramMap.put("wherecase", " deleteCode='001'  and user_id=" + userid);
        }else{
            paramMap.put("wherecase", " deleteCode='001' and  type = " + type + " and user_id="+userid);
        }
        paramMap.put("orderField", "  create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }

    @Override
    public Map<String, Object> createOrder(List<Map<String, Object>> list) {
        Map<String, Object> resultMap = new HashedMap();
        //订单号生成
        String orderno = StringHelper.getOrderNum();
        //一条总数据
        for (Map<String, Object> confirmInfo : list) {
            Integer userId = Integer.valueOf(confirmInfo.get("userId").toString());//用户id
            Integer storeId = Integer.valueOf(confirmInfo.get("storeId").toString());//门店id
            Integer totalnum = Integer.valueOf(confirmInfo.get("totalnum").toString());//总数量
            Double totalprice = Double.valueOf(confirmInfo.get("totalprice").toString());//总金额
            List<Map<String, Object>> addressinfo = (List) confirmInfo.get("addressinfo");//地址信息
            //获取地址id
            ReceiptAddress address = null;
            for (Map<String, Object> item : addressinfo) {
                address = new ReceiptAddress(item.get("userName").toString(), item.get("postalCode").toString(), item.get("provinceName").toString(), item.get("cityName").toString(), item.get("countryName").toString(), item.get("detailInfo").toString(), item.get("nationalCode").toString(), item.get("telNumber").toString(), userId);
            }
            Map<String, Object> result = receiptAddressService.createReceiptAddressId(address);
            String descrip = "";//订单描述
            try {
                //保存订单
                ShopOrder order = new ShopOrder(orderno, totalprice, totalnum, userId, storeId, Integer.valueOf(result.get("receiptAddressId").toString()), "001");
                order.setType("006");//订单类型
                order.setShoporderstatuscode("006002");//设置订单状态 未付款
                order.setFreightprice(Double.valueOf(0));//运费
                //生成订单
                orderMapper.insertSelective(order);
                //添加订单_商品中间表，记录订单明细
                List<Map<String, Object>> details = (List) confirmInfo.get("detailinfo");//订单详情
                for (Map<String, Object> detailInfo : details) {
                    Integer goodId = Integer.valueOf(detailInfo.get("goodId").toString());
                    Integer cartId = Integer.valueOf(detailInfo.get("cartId").toString());
                    Integer goodchildId = Integer.valueOf(detailInfo.get("gcid").toString());
                    Integer num = Integer.valueOf(detailInfo.get("num").toString());
                    descrip = descrip + "&" + detailInfo.get("title").toString();
                    ShopOrderGood ordergood = new ShopOrderGood(orderno, goodchildId, goodId, num, "001");
                    //订单_商品中间表保存数据
                    shopOrderGoodMapper.insertSelective(ordergood);
                    //删除购物车中下单的数据
                    cartMapper.deleteByPrimaryKey(cartId);
                    //减少库存数量
                    GoodChild goodChild = goodChildMapper.selectByPrimaryKey(goodchildId);
                    goodChild.setStocknumber(goodChild.getStocknumber() - num);
                    goodChildMapper.updateByPrimaryKeySelective(goodChild);
                }
                resultMap.put("errmsg", "ok");

            } catch (Exception e) {
                resultMap.put("errmsg", "error");
            }
            resultMap.put("orderno", orderno);
            resultMap.put("totalprice", totalprice);
            resultMap.put("descrip", descrip);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getOrderListDetail(Map map , String type) {
        List<Map<String, Object>> orderLists = new ArrayList<>();
        List<Map<String, Object>> orders = (ArrayList) map.get("pagingList");
        String ordernowType = null;
        //商城的情况
        for (int i = 0; i < orders.size(); i++) {
            ordernowType = (String) orders.get(i).get("type");
            //根据订单得到订单详情  商城
            if("006".equals(ordernowType) ) {
                List<Map<String, Object>> orderDetailLists = shopOrderGoodMapper.selectOrderDetailByOrderno(orders.get(i).get("orderno").toString());
                orders.get(i).put("child", orderDetailLists);
            }else{
                //根据订单得到订单详情  服务类订单
                Map<String, Object> orderDetailLists = shopOrderGoodMapper.selectServiceOrderDetailByOrderno(orders.get(i).get("orderno").toString());
                orders.get(i).put("child", orderDetailLists);
            }
            orderLists.add(orders.get(i));
        }
        //服务类的情况
        map.put("pagingList", orderLists);
        return map;
    }

    @Override
    public Map<String, Object> getOrderDetailByOrderno(String orderno) {
        Map<String, Object> resultMap = new HashMap<>();
        ShopOrder shopOrder = orderMapper.selectByPrimaryKey(orderno);
        Map<String, Object> detail = new HashMap<>();
        detail.put("info",shopOrderGoodMapper.selectOrderDetailByOrderno(orderno));
        resultMap.put("order",shopOrder);
        resultMap.put("address", receiptAddressMapper.selectByPrimaryKey(shopOrder.getReceiptAddressId()));
        resultMap.put("orderdetail",detail);
        return resultMap;
    }

    @Override
    public Map<String, Object> modifyOrder(String orderno) {
        Map<String, Object> paramMap = new HashMap<>();
        //增加订单明细中商品原有库存数量
        List<Map<String,Object>> detaillists = shopOrderGoodMapper.selectOrderDetailByOrderno(orderno);
        for (int i=0;i<detaillists.size();i++){
            Integer goodchildId = Integer.valueOf(detaillists.get(i).get("gcid").toString());
            Integer buynum = Integer.valueOf(detaillists.get(i).get("buynum").toString());
            GoodChild goodChild=goodChildMapper.selectByPrimaryKey(goodchildId);
            goodChild.setStocknumber(goodChild.getStocknumber()+buynum);
            goodChildMapper.updateByPrimaryKeySelective(goodChild);
        }
        //更改订单状态--取消
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setShoporderstatuscode("009");
        shopOrder.setOrderno(orderno);
        paramMap.put("resultnumber",orderMapper.updateByPrimaryKeySelective(shopOrder));
        return paramMap;
    }

    @Override
    public int updateByPrimaryKeySelective(ShopOrder record) {
        return orderMapper.updateByPrimaryKeySelective(record);
    }
}
