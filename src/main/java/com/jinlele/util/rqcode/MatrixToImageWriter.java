package com.jinlele.util.rqcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jinlele.util.StringHelper;
import com.jinlele.util.qiniuUtils.QiniuParamter;
import com.jinlele.util.qiniuUtils.QiniuUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by panweiji on 2017/1/3 0003.
 * 生成二维码
 */
public final class MatrixToImageWriter {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final String PATH = "c:/qrcode/";  //默认保存到服务器的该目录，二维码图片存储路径
    public static String key_suff =  "qrcode/";//七牛上存储二维码目录位置

    private MatrixToImageWriter() {
    }

    /**
     * 生成二维码方法
     * @param type 订单类型
     * @param orderno 订单号用于给二维码图片命名
     * @return
     */
    public static String makeQRCode(String type, String orderno) throws IOException, WriterException {
        String fileName = orderno + ".jpg";//上传到七牛后保存的文件名
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map hints = new HashMap();
        //内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        String content = "http://6weiyi.com/jinleleBack/admin/order/";
        //根据类型保存图片访问路径
        switch (type) {
            case "001":
                content = content + "refurbish/detail/" + orderno;
                break;
            case "002":
                content = content + "repair/detail/" + orderno;
                break;
            case "003":
                content = content + "detect/detail/" + orderno;
                break;
            case "004":
                content = content + "recycle/detail/" + orderno;
                break;
            case "005":
                content = content + "exchange/detail/" + orderno;
                break;
            default:
                content = content + "shoporder/detail/" + orderno;
                break;
        }
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200, hints);
        //生成二维码
        File outputFile = new File(PATH, fileName);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }
        MatrixToImageWriter.writeToFile(bitMatrix, "jpg", outputFile);
        String filePath = PATH + fileName;//上传文件的路径
        //上传到七牛云
        QiniuUtil.upload(filePath, key_suff + fileName);
        //构造访问七牛上二维码图片的url
        String qrcodeurl = QiniuParamter.URL + key_suff +  fileName;
        //删除服务器上的该文件
        StringHelper.deleteFile(filePath);
        return qrcodeurl;
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }


    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    public static void main(String[] args) {
        String content = "订单号:20161226132700044476。买家:潘维吉。联系方式：18863302302";
        String orderno = "20161226132700044476";
        try {
            MatrixToImageWriter.makeQRCode(content, orderno);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
