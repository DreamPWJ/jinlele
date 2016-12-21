package com.jinlele.util.qiniuUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

/**
 * 批量操作文件
 *
 */
public class BatchDemo {
    public static void main(String args[]){
        //设置需要操作的账号的AK和SK
        String ACCESS_KEY = QiniuParamter.ACCESS_KEY;
        String SECRET_KEY = QiniuParamter.SECRET_KEY;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth,c);

        //创建Batch类型的operations对象
        BucketManager.Batch operations = new BucketManager.Batch();

        //第一组源空间名、原文件名，目的空间名、目的文件名
        String bucketFrom1 = "jinlele";
        String keyFrom1 = "05.png";
        String bucketTo1 = "jinlele";
        String keyTo1 = "ceshi/04.png";

        //第二组源空间名、原文件名，目的空间名、目的文件名
        String bucketFrom2 = "jinlele";
        String keyFrom2 = "ceshi/05.png";
        String bucketTo2 = "jinlele";
        String keyTo2 = "shop/05.png";


        try {
            //调用批量操作的batch方法
            Response res = bucketManager.batch(operations.move(bucketFrom1, keyFrom1, bucketTo1, keyTo1)
                    .move(bucketFrom2, keyFrom2, bucketTo2, keyTo2));

            System.out.println(res.toString());

        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}
