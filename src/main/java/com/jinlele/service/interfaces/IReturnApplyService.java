package com.jinlele.service.interfaces;

import com.jinlele.model.ReturnApply;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/19.
 */
public interface IReturnApplyService {
    Map<String,Object> addReturnApply(ReturnApply returnApply,String type);
}
