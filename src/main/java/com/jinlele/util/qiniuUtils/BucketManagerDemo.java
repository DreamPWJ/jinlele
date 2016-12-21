package com.jinlele.util.qiniuUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

/**
 * 管理文件
 *
 * 获取文件信息
 *
 */
public class BucketManagerDemo {

    public static void main(String args[]){
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = QiniuParamter.ACCESS_KEY;
        String SECRET_KEY = QiniuParamter.SECRET_KEY;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth,c);
        //要测试的空间和key，并且这个key在你空间中存在
        String bucket = "jinlele";
        String key = "ceshi";
        try {
            //调用stat()方法获取文件的信息
            FileInfo info = bucketManager.stat(bucket, key);
            System.out.println(info.hash);
            System.out.println(info.key);
            System.out.println(info.fsize);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());

        }
    }

}
