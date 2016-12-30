package com.jinlele.service.interfaces;

import java.io.IOException;

/**
 * Created by Administrator on 2016-12-28.
 */
public interface IServiceService {


    Integer saveService(Integer userId , Double aturalprice , String descrip ,String type ,Integer storeId,String[] mediaIds) throws IOException;
}
