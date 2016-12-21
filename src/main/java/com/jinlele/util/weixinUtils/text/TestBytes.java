package com.jinlele.util.weixinUtils.text;

import java.io.UnsupportedEncodingException;

/**
 *
 */
public class TestBytes {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String source = "微信";
        int length = source.getBytes().length;
        System.out.println(length);

        System.out.println("============");
        System.out.println(source.getBytes("ISO8859-1").length);
        System.out.println(source.getBytes("GB2312").length);
        System.out.println(source.getBytes("GBK").length);
        System.out.println(source.getBytes("UTF-8").length);
    }
}
