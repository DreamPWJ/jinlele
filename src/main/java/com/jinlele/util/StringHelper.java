package com.jinlele.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 常用工具类
 */
public class StringHelper {

    /**
     * 获取订单流水号  时间（到毫秒）+三位随机数
     *
     * @return
     */
    public static String getOrderNum() {
        try {
            Thread.sleep(1L);//处理并发问题 延迟1毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmsssss");
        String num = Integer.toString((int) ((999 - 100) * Math.random() + 100));//取得一个100-999的3位随机数字字符串
        String orderNum = format.format(date) + num;
        return orderNum;
    }

    /**
     * 根据 删除文件 和 相应文件夹
     * @param
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(getOrderNum());
    }
}
