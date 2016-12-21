package com.jinlele.util.qiniuUtils;

import com.qiniu.util.Auth;

/**
 * 下载文件
 */
public class Download {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = QiniuParamter.ACCESS_KEY;
    String SECRET_KEY = QiniuParamter.SECRET_KEY;
    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //构造私有空间的需要生成的下载的链接
    String URL = "http://oi2k72ijx.bkt.clouddn.com/1216";

    public static void main(String args[]) {
        new Download().download();
    }

    public void download() {
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String downloadRUL = auth.privateDownloadUrl(URL, 3600);
        System.out.println(downloadRUL);
    }
}
