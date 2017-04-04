package com.jinlele.controller;

import com.jinlele.model.Favourite;
import com.jinlele.service.interfaces.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/19.
 * 商品相关的Controller
 */
@Controller
@RequestMapping("/good")
public class GoodController {
    @Resource
    IGoodService goodService;
    @Resource
    IGoodCatogoryService goodCatogoryService;
    @Resource
    IShoppingCartService shoppingCartService;
    @Resource
    IFavouriteService favouriteService;
    @Resource
    ICommentService commentService;
    @Resource
    IGoodPictureService goodPictureService;

    /**
     * 获取一级分类
     */
    @ResponseBody
    @RequestMapping(value = "/getFirstCatogotory", method = RequestMethod.GET)
    public Map<String, Object> getFirstCatogotory() {
        return goodCatogoryService.getFirstCatogory();
    }

    /**
     * 获取产品列表
     * @param categoryname 二级分类名称
     * @param querytype  查询条件 综合 0  最新 1 价格从高到低 2 价格从低到高 3
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getGoodList/{pagenow}/{categoryname}/{querytype}/{flag}", method = RequestMethod.GET)
    public Map<String, Object> getGoodList(@PathVariable int pagenow, @PathVariable String categoryname, @PathVariable int querytype , @PathVariable int flag) {
        return goodCatogoryService.getGoodListPaging(pagenow, categoryname, querytype ,flag);
    }

    /**
     * 获取产品详情
     * @param goodId 商品id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getGoodDetail/{goodId}/{userId}", method = RequestMethod.GET)
    public Map<String, Object> getGoodDetail(@PathVariable int goodId,@PathVariable int userId) {
        Map<String, Object> newMap = new HashedMap();
        Map<String, Object> goodmap = goodService.getGoodDetail(goodId);   //获得商品详情信息
        List imgurls = goodPictureService.getGoodPicture(goodId);//获取图片集合
        int totalnum = shoppingCartService.getShopcharTotalNum(userId);  //初始页面时，获得该用户加入购车商品总数量
        List<Map<String, Object>>  goodchilds = goodService.getGoodChildsByGoodId(goodId);
        Favourite favourite = new Favourite(userId , goodId);
        List<Map<String, Object>> favourites = favouriteService.selectByuserIdAndGoodId(favourite); //查询是否收藏
        newMap.put("good" , goodmap);
        newMap.put("imgurls" , imgurls);
        newMap.put("goodchilds" , goodchilds);
        newMap.put("totalnum" , totalnum);
        newMap.put("favourites" , favourites);
        return newMap;
    }

    @ResponseBody
    @RequestMapping("/getSecondCatogaryByPid/{pid}")
    public List getSecondCatogaryByPid(@PathVariable("pid") int pid){
        return goodCatogoryService.getSecondCatogaryByPid(pid);
    }

    /**
     * 获取产品列表
     * @param pagenow     当前页码
     * @param catogoryId  二级分类id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGoodsByCidPaging/{pagenow}/{catogoryId}")
    public  Map<String, Object>  getGoodsByCidPaging(@PathVariable("pagenow") int pagenow ,@PathVariable("catogoryId") int catogoryId){
        return goodCatogoryService.getGoodsByCidPaging(pagenow ,catogoryId);
    }


    /**
     * 根据一节分类 遍历二级分类 不分页
     * @param pid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getSecondCatogByPid/{pid}")
    public List getSecondCatogByPid(@PathVariable("pid") int pid){
        return goodCatogoryService.getSecondCatogByPid(pid);
    }

    /**
     * 将商品加入收藏
     * @param favourite
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveFavourite")
    public Map<String, Object>  saveFavourite(Favourite favourite){
         favouriteService.insertSelective(favourite);  //n代表插入影响的行数 n如果等于1 则插入成功
        Map<String, Object> map = new HashedMap();
        map.put("favouriteId" , favourite.getId());
        return map;

    }

    /**
     * 根据id删除收藏
     * @param fid
     * @return
     */
    @ResponseBody
    @RequestMapping("/delFavourite/{fid}")
    public  Map<String, Object> saveFavourite(@PathVariable("fid") int fid){
        int n =  favouriteService.deleteByPrimaryKey(fid);  //n代表影响的行数 n如果等于1 则删除成功
        Map<String, Object> map = new HashedMap();
        map.put("n" , n);
        return map;
    }

    /**
     * 根据用户id查询收藏列表
     */
    @ResponseBody
    @RequestMapping("/getFavs/{pagenow}/{userId}")
    public  Map<String, Object> getFavs(@PathVariable("pagenow") int pagenow , @PathVariable("userId") int userId){
        Map<String, Object> favs =  favouriteService.getFavsByUidPaging(pagenow , userId);  //n代表影响的行数 n如果等于1 则删除成功
        return favs;
    }

    /**
     * 获取产品id查询评论总数
     */
    @ResponseBody
    @RequestMapping(value = "/getTotalNumber/{goodId}", method = RequestMethod.GET)
    public Map<String, Object> getTotalNumber(@PathVariable int goodId) {
        Map<String, Object> map = new HashedMap();
        map.put("total",commentService.getTotalNumber(Integer.valueOf(goodId)));
        return map;
    }

    /**
     * 获取产品id查询评论
     */
    @ResponseBody
    @RequestMapping(value = "/getGoodComments/{goodId}/{pagenow}", method = RequestMethod.GET)
    public Map<String, Object> getGoodComments(@PathVariable int goodId,@PathVariable int pagenow) {
        Map<String, Object> map = new HashedMap();
        map.put("comments",commentService.getCommentsPaging(pagenow,goodId));
        return map;
    }

    /**
     * 更多列表
     */
    @ResponseBody
    @RequestMapping(value = "/getBarterList/{amount}/{pagenow}/{type}" ,method = RequestMethod.GET)
    public  Map<String,Object> getBarterList(@PathVariable Double amount,@PathVariable Integer pagenow,@PathVariable String type) {
        return goodService.getBarterListPaging(amount,pagenow,type);
    }
}
