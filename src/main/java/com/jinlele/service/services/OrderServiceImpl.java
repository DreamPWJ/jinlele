package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.*;
import com.jinlele.service.interfaces.IOrderService;
import com.jinlele.service.interfaces.IReceiptAddressService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.StringHelper;
import com.jinlele.util.SysConstants;
import com.jinlele.util.rqcode.MatrixToImageWriter;
import com.jinlele.util.weixinUtils.pay.PayCommonUtil;
import org.apache.commons.collections.map.HashedMap;
import org.jdom.JDOMException;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    GoodMapper goodMapper;

    @Resource
    GoodChildMapper goodChildMapper;

    @Resource
    ReceiptAddressMapper receiptAddressMapper;

    @Resource
    IReceiptAddressService receiptAddressService;

    @Resource
    ServiceMapper serviceMapper;

    @Resource
    WalletMapper walletMapper;

    @Resource
    PaymentdetailMapper paymentdetailMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    PictureMapper pictureMapper;

    @Resource
    ServiceGoodMapper serviceGoodMapper;

    @Override
    public ShopOrder selectByPrimaryKey(String orderno) {
        return orderMapper.selectByPrimaryKey(orderno);
    }

    /**
     * 商城订单列表
     */
    @Override
    public Map<String, Object> getShopListPaging(int pagenow, int userid ,String type) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", "  shoporder as o  ");
        paramMap.put("fields", "  * ,(select dictname from vw_dictdetail where eng='ordertype' and codevalue=o.type) as ordertype,(select dictname from vw_dictdetail where eng='orderstatus' and codevalue=o.shoporderstatusCode) as orderstatus ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        if("ALL".equals(type)) {
            paramMap.put("wherecase", " deleteCode='001' and type!='007' and user_id=" + userid);
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
                order.setShoporderstatuscode("001");//设置订单状态 待付款
                order.setFreightprice(Double.valueOf(0));//运费
                order.setQrcodeUrl(MatrixToImageWriter.makeQRCode("006", orderno));//生成二维码

                //生成订单
                orderMapper.insertSelective(order);
                //添加订单_商品中间表，记录订单明细
                List<Map<String, Object>> details = (List) confirmInfo.get("detailinfo");//订单详情
                for (Map<String, Object> detailInfo : details) {
                    Integer goodId = Integer.valueOf(detailInfo.get("goodId").toString());
                    Integer cartId = Integer.valueOf(detailInfo.get("cartId").toString());
                    Integer goodchildId = Integer.valueOf(detailInfo.get("gcid").toString());
                    Double price = Double.valueOf(detailInfo.get("price").toString());
                    Integer num = Integer.valueOf(detailInfo.get("num").toString());
                    descrip = descrip + "&" + detailInfo.get("title").toString();
                    ShopOrderGood ordergood = new ShopOrderGood(orderno, goodchildId, goodId,price, num, "001");
                    //订单_商品中间表保存数据
                    shopOrderGoodMapper.insertSelective(ordergood);
                    //删除购物车中下单的数据
                    cartMapper.deleteByPrimaryKey(cartId);
                    //减少子库存数量
                    GoodChild goodChild = goodChildMapper.selectByPrimaryKey(goodchildId);
                    goodChild.setStocknumber(goodChild.getStocknumber() - num);
                    goodChildMapper.updateByPrimaryKeySelective(goodChild);
                    //减少总库存数量
                    Good good =goodMapper.selectByPrimaryKey(goodChild.getGoodId());
                    good.setStocknum(good.getStocknum()-num);
                    goodMapper.updateByPrimaryKeySelective(good);
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
                //根据订单号获得产品详情
                List products =productMapper.getServiceOrderProductsInfoByOrderno(orders.get(i).get("orderno").toString());
                orders.get(i).put("products", products);
                //根据订单号获得图片组
                List pictures = pictureMapper.getServiceOrderPicturesInfoByOrderno(orders.get(i).get("orderno").toString());
                orders.get(i).put("pictures", pictures);

//                Map<String, Object> orderDetailLists = shopOrderGoodMapper.selectServiceOrderDetailByOrderno(orders.get(i).get("orderno").toString());
//                orders.get(i).put("child", orderDetailLists);
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
        Map<String, Object> orderinfo = orderMapper.selectOrderInfoByOrderno(orderno);
        String type=orderinfo.get("type").toString();
        switch (type) {
            case "006":
                Map<String, Object> detail = new HashMap<>();
                detail.put("info", shopOrderGoodMapper.selectOrderDetailByOrderno(orderno));
                resultMap.put("orderdetail", detail);
                break;
            default:
                resultMap.put("servicedetail", serviceMapper.selectServiceDetailByOrderno(orderno).get(0));
                break;
        }
        resultMap.put("order",orderinfo);
        if(StringUtil.isBlank(orderinfo.get("receipt_address_id").toString())){
            resultMap.put("address","");
        }else {
            resultMap.put("address", receiptAddressMapper.selectByPrimaryKey(Integer.valueOf(orderinfo.get("receipt_address_id").toString())));
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderno){
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> orderinfo = orderMapper.selectOrderInfoByOrderno(orderno);
        String type=orderinfo.get("type").toString();
        switch (type){
            case "006":
                Map<String, Object> detail = new HashMap<>();
                detail.put("info", shopOrderGoodMapper.selectOrderDetailByOrderno(orderno));
                resultMap.put("orderdetail", detail);
                break;
            default:
                if("005".equals(type)){
                    resultMap.put("buyinfo",serviceGoodMapper.getBuyInfo(orderno));
                }
                resultMap.put("pictures", serviceMapper.getServicePictures(orderno));
                resultMap.put("products", serviceMapper.getServiceProducts(orderno));
                break;
        }
        resultMap.put("order",orderinfo);
        if(orderinfo.get("receipt_address_id")==null){
            resultMap.put("address","");
        }else {
            resultMap.put("address", receiptAddressMapper.selectByPrimaryKey(Integer.valueOf(orderinfo.get("receipt_address_id").toString())));
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> putOrder(String orderno ,String orderType , String payresult) {
        if(payresult.equals("ok")){
            //调用查询接口，处理业务逻辑
            ShopOrder order=orderMapper.selectByPrimaryKey(orderno);
            switch (order.getPayResult()){
                case "003"://已处理且支付成功，不再处理
                    break;
                default:
                    String randomString = PayCommonUtil.getRandomString(32);
                    SortedMap<String, Object> parameterMap = new TreeMap<>();
                    parameterMap.put("appid", PayCommonUtil.APPID);
                    parameterMap.put("mch_id", PayCommonUtil.MCH_ID);// 商户号
                    parameterMap.put("nonce_str", randomString);// 随机字符串
                    parameterMap.put("out_trade_no", orderno);// 商户订单号
                    String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
                    StringBuffer sb = new StringBuffer();

                    sb.append("<xml>");
                    sb.append("<appid>"+PayCommonUtil.APPID+"</appid>");
                    sb.append("<mch_id>"+PayCommonUtil.MCH_ID+"</mch_id>");
                    sb.append("<nonce_str>"+randomString+"</nonce_str>");
                    sb.append("<out_trade_no>"+orderno+"</out_trade_no>");
                    sb.append("<sign>"+sign+"</sign>");
                    sb.append("</xml>");

                    String backxmlstr =PayCommonUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/orderquery", "POST",sb.toString());
                    Map<String,Object> map=null;
                    try {
                        map=PayCommonUtil.doXMLParse(backxmlstr);
                    } catch (JDOMException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String tradestate=map.get("trade_state").toString();//交易状态  SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付  CLOSED—已关闭 REVOKED—已撤销（刷卡支付） USERPAYING--用户支付中 PAYERROR--支付失败(其他原因，如银行返回失败)
                    if("SUCCESS".equals(tradestate)){
                        String transaction_id=map.get("transaction_id").toString();//微信支付订单号
                        Double total_fee=Double.valueOf(Double.valueOf(map.get("total_fee").toString())/100);//订单总金额，实际支付金额
                        String time_end=map.get("time_end").toString();//支付完成时间
                        ShopOrder shopOrder=null;
                        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                        String orderStatus = "";//订单状态
                        if("006".equals(orderType)) {
                            orderStatus = "002";// 商城修改为 已付款
                        }else{
                            orderStatus = orderType + "002";//服务类订单订单状态待前缀，如 翻新支付成功 001002
                        }
                        try {
                            shopOrder=new ShopOrder(orderno,total_fee,orderStatus,"003",s.parse(time_end));
                            shopOrder.setUpdateTime(s.parse(time_end));//支付完成时间
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        orderMapper.updateByPrimaryKeySelective(shopOrder);
                    }
            }
        }
        return getOrderDetail(orderno);
    }

    @Override
    public Map<String, Object> modifyOrder(String orderno, String typeCode) {
        Map<String, Object> paramMap = new HashMap<>();
        ShopOrder shopOrder=new ShopOrder();
        switch (typeCode){
            case "001"://翻新
            case "003"://检测
                shopOrder.setShoporderstatuscode(typeCode+"010");
                break;
            case "002"://维修
            case "004"://回收
                shopOrder.setShoporderstatuscode(typeCode+"011");
                break;
            case "005"://换款
                shopOrder.setShoporderstatuscode(typeCode+"018");
                break;
            case "006":
                //增加订单明细中商品原有库存数量
                List<Map<String,Object>> detaillists = shopOrderGoodMapper.selectOrderDetailByOrderno(orderno);
                for (int i=0;i<detaillists.size();i++){
                    Integer goodchildId = Integer.valueOf(detaillists.get(i).get("gcid").toString());
                    Integer buynum = Integer.valueOf(detaillists.get(i).get("buynum").toString());
                    GoodChild goodChild=goodChildMapper.selectByPrimaryKey(goodchildId);
                    goodChild.setStocknumber(goodChild.getStocknumber()+buynum);
                    goodChildMapper.updateByPrimaryKeySelective(goodChild);
                }
                //更改商城订单状态--取消
                shopOrder.setShoporderstatuscode("006");
                break;
        }
        shopOrder.setOrderno(orderno);
        paramMap.put("resultnumber",orderMapper.updateByPrimaryKeySelective(shopOrder));
        return paramMap;
    }

    @Override
    public int updateByPrimaryKeySelective(ShopOrder record) {
        return orderMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Map<String, Object> findReceiptServiceByOrderno(String orderno) {
        return orderMapper.findReceiptServiceByOrderno(orderno);
    }

    @Override
    public  List<Map<String , Object>> findAllexpressCompanies(){
        return  orderMapper.findAllexpressCompanies();
    }

    @Override
    public Double selectActualPrice(String orderno) {
        return serviceMapper.selectActualPrice(orderno);
    }

    //根据订单号查询服务的id
    @Override
    public Integer selectServiceIdByOrderNo(String orderno){
        return orderMapper.selectServiceIdByOrderNo(orderno);
    }

    //查询下单时间  selectCreateTime
    public Date selectCreateTime(String orderno){
        return orderMapper.selectCreateTime(orderno);
    }

    @Override
    public Map<String, Object> getCertificationInfo(String orderno) {
        return orderMapper.getCertificationInfo(orderno);
    }

    @Override
    public Map<String, Object> getPostbackImg(String orderno) {
        Map<String, Object> result = new HashMap<>();
        result.put("image",orderMapper.getPostbackImg(orderno));
        return result;
    }

    @Override
    public Map<String , Object> saveRechargeOrder(Integer userId, Double rechargeMoney) {
        Map<String , Object> map = new HashMap<>();
        String orderno = StringHelper.getOrderNum();
        String type = "007";//007代表的是充值订单
        String orderstatus = "007001";//007001 代表已下单
        ShopOrder order = new ShopOrder( orderno, rechargeMoney, userId, type, orderstatus);
        int n = orderMapper.insertSelective(order);
        map.put("orderno" , orderno);
        map.put("type" , type);
        map.put("orderstatus" , orderstatus);
        map.put("price" , rechargeMoney);
        map.put("n" , n);
        return map;
    }

    //充值成功后,修改订单表，虚拟账户表，账户明细表
    @Override
    public void updateRechargetSuccess(ShopOrder order) {
        order.setUpdateTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        //根据订单号 查询得到用户用户id
        Integer userId = orderMapper.getUserIdByOrderno(order.getOrderno());
        //根据用户id 查询得到账户名称和余额
        Map<String, Object> map = walletMapper.getWalletByUserId(userId);
        Double balance = (Double) map.get("balance");
        String walletno = (String) map.get("walletno");
        balance = balance + order.getActualpayprice();
        Wallet wallet = new Wallet(walletno, balance, new Date());
        //更新账户余额
        wallet.setUpdateTime(new Date());
        walletMapper.updateByPrimaryKeySelective(wallet);//更新虚拟账户
        //新增提现充值记录明细表
        Paymentdetail paymentdetail = new Paymentdetail();
        paymentdetail.setWalletNo(walletno);
        paymentdetail.setChangemoney(order.getActualpayprice());
        paymentdetail.setBalance(balance);
        paymentdetail.setMemo("会员自己充值,来自充值订单:"+order.getOrderno());
        paymentdetailMapper.insertSelective(paymentdetail);

    }

    //查询充值结果
    public Map<String,Object> getRechargeResult(String orderno){
        return  orderMapper.getRechargeResult(orderno);
    }


    public Map<String, Object> getStoreByOrderno(String orderno){
        return  orderMapper.getStoreByOrderno(orderno);
    }

}
