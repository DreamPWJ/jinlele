package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.*;
import com.jinlele.service.interfaces.ICommentService;
import com.jinlele.service.interfaces.IPictureService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.SysConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/2.
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Resource
    BaseMapper baseMapper;

    @Resource
    IPictureService pictureService;

    @Resource
    PictureMapper pictureMapper;

    @Resource
    CommentMapper commentMapper;

    @Resource
    CommentPictureMapper commentPictureMapper;

    @Resource
    ShopOrderGoodMapper shopOrderGoodMapper;

    @Resource
    ShopOrderMapper shopOrderMapper;

    @Resource
    ServiceMapper serviceMapper;

    @Override
    public int createComment(List<Map<String,Object>> list) {
        try {
            //读取整体信息，一条总记录
            for (Map<String, Object> allCommentInfo : list) {
                String orderno = allCommentInfo.get("orderno").toString();
                Integer userId = Integer.valueOf(allCommentInfo.get("userId").toString());
                Integer descriplevel = Integer.valueOf(allCommentInfo.get("descriplevel").toString());
                String type = allCommentInfo.get("type").toString();//业务类型
                //读取评论内容信息，多条
                List<Map<String, Object>> itemsinfo = (List) allCommentInfo.get("itemsinfo");
                for (Map<String, Object> itemsinfoMap : itemsinfo) {
                    Integer gcid =0;
                    if(type.length()==0) {
                        gcid = Integer.valueOf(itemsinfoMap.get("gcid").toString());//商品子id
                    }
                    String content = itemsinfoMap.get("content").toString();//针对商品子id的评论内容
                    List mediaIds = (List) itemsinfoMap.get("mediaIds");//针对商品子id的媒体文件集合
                    //添加评论
                    Comment comment = new Comment(userId, content);
                    commentMapper.insertSelective(comment);
                    //转换media数组
                    String[] strings = new String[mediaIds.size()];
                    for (int i = 0; i < mediaIds.size(); i++) {
                        strings[i] = mediaIds.get(i).toString();
                    }
                    if (strings.length != 0) {
                        // 上传媒体文件
                        List<String> urls = pictureService.saveURL(strings);
                        for (int j = 0; j < urls.size(); j++) {
                            //添加图片
                            Picture pic = new Picture(urls.get(j), userId);
                            pictureMapper.insertSelective(pic);
                            //添加评论图片中间表
                            CommentPicture cp = new CommentPicture(comment.getId(), pic.getId());
                            commentPictureMapper.insertSelective(cp);
                        }
                    }
                    if (type.length()==0) {
                        //修改订单商品中间表，添加评论id
                        ShopOrderGood shopOrderGood = new ShopOrderGood();
                        shopOrderGood.setCommentId(comment.getId());
                        shopOrderGood.setGoodchildId(gcid);
                        shopOrderGood.setShoporderNo(orderno);
                        shopOrderGoodMapper.updateByOrderNoGcid(shopOrderGood);
                    }else{
                        //修改service表，添加评论id
                        com.jinlele.model.Service service= new com.jinlele.model.Service();
                        service.setOrderNo(orderno);
                        service.setCommentId(comment.getId());
                        serviceMapper.updateByOrdernoSelective(service);
                    }
                    //修改订单，增加描述等级
                    ShopOrder shopOrder = new ShopOrder();
                    shopOrder.setDescriplevel(descriplevel);
                    //todo 状态未完成
                    //region 更新为已评价状态
                    switch (type) {
                        case "001"://产品翻新
                            shopOrder.setShoporderstatuscode("001009");
                            break;
                        case "002"://产品维修
                            shopOrder.setShoporderstatuscode("002010");
                            break;
                        case "003"://产品检测
                            shopOrder.setShoporderstatuscode("003009");
                            break;
                        case "004"://产品回收
                            shopOrder.setShoporderstatuscode("004008");
                            break;
                        case "005"://产品换款
                            shopOrder.setShoporderstatuscode("005014");
                            break;
                        default://默认商城
                            shopOrder.setShoporderstatuscode("005");
                            break;
                    }
                    //endregion
                    shopOrder.setOrderno(orderno);
                    shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    @Override
    public Map<String, Object> getCommentsPaging(int pagenow, int goodid) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", "  comment c\n" +
                "join shoporder_good og on og.comment_id=c.id\n" +
                "join user u on u.id=c.user_id  ");
        paramMap.put("fields", "  c.id,c.content,c.create_time,u.nickname,u.headimgurl  ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", SysConstants.PAGESIZE);
        paramMap.put("wherecase", " c.deleteCode='001' and og.good_id="+goodid);
        paramMap.put("orderField", "  c.create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }

    @Override
    public int getTotalNumber(Integer goodid) {
        return commentMapper.getTotalNumber(goodid).size();
    }
}
