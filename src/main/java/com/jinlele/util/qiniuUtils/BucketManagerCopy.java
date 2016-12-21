package com.jinlele.util.qiniuUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

/**
 * 复制单个文件
 *
 */
public class BucketManagerCopy {

    public static void main(String args[]) {
        //设置需要操作的账号的AK和SK
        String ACCESS_KEY = QiniuParamter.ACCESS_KEY;
        String SECRET_KEY = QiniuParamter.SECRET_KEY;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        //要测试的空间和key，并且这个key在你空间中存在
        String bucket = QiniuParamter.BUCKETNAME;
        String key = "ceshi/1.png";
        //将文件从文件key 复制到文件key2。 可以在不同bucket复制
        String key2 = "shop/1.png";
        try {
            //调用copy方法移动文件
            bucketManager.copy(bucket, key, bucket, key2);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}
