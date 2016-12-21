package com.jinlele.util.qiniuUtils;


import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

/**
 *  移动单个文件
 *
 *  测试成功
 */

public class BucketManagerYiDong {

    public static void main(String args[]) {
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = QiniuParamter.ACCESS_KEY;
        String SECRET_KEY = QiniuParamter.SECRET_KEY;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        System.out.println(auth);

        Zone z = Zone.zone2();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth,c);
        //要测试的空间和key，并且这个key在你空间中存在
        String bucket = QiniuParamter.BUCKETNAME;
        String key = "my-java2.png";
        //将文件从文件key移动到文件key2, 可以在不同bucket移动，同空间移动相当于重命名
        String key2 = "ceshi";
        try {
            //调用move方法移动文件
            bucketManager.move(bucket, key, bucket, key2);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());

        }
    }
}
