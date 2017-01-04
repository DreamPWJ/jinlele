package com.jinlele.service.interfaces;


import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/2.
 */
public interface ICommentService {
    //添加评论
    int createComment(List<Map<String,Object>> list);
    //根据商品id分页查询评论
    Map<String,Object> getCommentsPaging(int pagenow, int goodid);
    //获取评论总数
    int getTotalNumber(Integer goodid);
}
