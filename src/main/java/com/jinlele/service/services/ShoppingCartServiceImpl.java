package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.ShoppingCartMapper;
import com.jinlele.model.ShoppingCart;
import com.jinlele.service.interfaces.IShoppingCartService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twislyn on 2016/12/20.
 */
@Service
public class ShoppingCartServiceImpl implements IShoppingCartService {

    @Resource
    BaseMapper baseMapper;

    @Resource
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public Map<String, Object> getShoppingCartPaging(int pagenow, int userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", " (\n" +
                "select s.goodchild_id as gcid,g.title,g.saleprice, s.id as cartId ,s.create_time,\n" +
                "s.user_id as userId, s.num ,g.id as goodId  ,\n" +
                "s.deleteCode as sdelCode , g.deleteCode as gdelCode\n" +
                "from  shoppingcart s \n" +
                "LEFT JOIN good g on s.good_id = g.id order by s.id\n" +
                ")as m\n" +
                "left join goodchild  c on m.gcid = c.id ");
        paramMap.put("fields", " m.* ,c.imgurl,c.color,c.stocknumber  ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", 100);
        paramMap.put("wherecase", " m.sdelCode='001' AND m.gdelCode='001' AND m.userId= "+userId);
        paramMap.put("orderField", " m.create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap) ;
    }

    @Override
    public int insertSelective(ShoppingCart record) {
        return shoppingCartMapper.insertSelective(record);
    }

    @Override
    public int getShopcharTotalNum(@Param("userId") Integer userId) {
        return shoppingCartMapper.getShopcharTotalNum(userId);
    }

    @Override
    public List getShopcharInfo(ShoppingCart record) {
        return shoppingCartMapper.getShopcharInfo(record);
    }

    //添加购物车
    public int addShoppingCart(ShoppingCart cart){
        List<Map<String, Object>> list =  shoppingCartMapper.getShopcharInfo(cart);
        if(list.size() == 0){
            shoppingCartMapper.insertSelective(cart);  //没有数据就添加数据
            return cart.getNum();
        }
        if(list.size() == 1){   //有数据就去更新
            Map<String , Object> map = list.get(0);
            cart.setId((Integer) map.get("id"));
            Integer num =  (Integer) map.get("num") + cart.getNum();
            cart.setNum(num);
            shoppingCartMapper.updateByPrimaryKeySelective(cart);
            return  num;
        }
        return 0;
    }

    public int updateByPrimaryKeySelective(ShoppingCart record){
        return shoppingCartMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ShoppingCart record){
        return shoppingCartMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByUserIdGcid(int userid, int gcid) {
        return shoppingCartMapper.deleteByUserIdGcid(userid,gcid);
    }


}
