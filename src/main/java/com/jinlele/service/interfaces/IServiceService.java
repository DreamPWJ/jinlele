package com.jinlele.service.interfaces;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-28.
 */
public interface IServiceService {


    Map<String , Object> saveService(Integer userId , Double aturalprice , String descrip , String type , Integer storeId, String[] mediaIds) throws IOException;

    Map<String , Object> getrefurbishPrice();

    Map<String , Object> getdetectPrice();

}
