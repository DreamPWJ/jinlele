package com.jinlele.util.weixinUtils.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信请求校验工具类
 */
public class ValidationUtil {



    /**
     * @param  signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param  timestamp 时间戳
     * @param nonce	随机数
     * @return
     */
    public static boolean checkSignature(String signature , String timestamp , String nonce){
//      1）将token、timestamp、nonce三个参数进行字典序排序
//      2）将三个参数字符串拼接成一个字符串进行sha1加密
//      3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        String[] array = new String[]{Parameter.token ,timestamp , nonce};
        Arrays.sort(array);
        StringBuilder buff = new StringBuilder();
        for(int i=0;i<array.length;i++){
            buff.append(array[i]);
        }
        MessageDigest md = null;
        String result = "";
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] data = md.digest(buff.toString().getBytes());
            result =  bytesToStr(data);
            System.out.println("加密后的字符串为: "+ result);
            //将字节数组转换成字符串
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result!=null?(result.equals(signature.toUpperCase())):false;
    }

    //将字节数组，转成成16进制字符串
    private static String bytesToStr(byte[] byteArray){
        String strDigest = "";
        for(int i =0;i<byteArray.length;i++){
            strDigest += bytesToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    //将字节数组，转成成16进制字符串
    private static String bytesToHexStr(byte mByte){
        char[] Digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] temp1 = new char[2];
        temp1[0] = Digit[(mByte>>>4) & 0x0F];
        temp1[1] = Digit[mByte & 0x0F];
        String str = new String(temp1);
        return  str;
    }
}
