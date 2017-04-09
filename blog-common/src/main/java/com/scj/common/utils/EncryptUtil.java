package com.scj.common.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/7/11.
 */
public class EncryptUtil {
    /**
     * MD5加密 BASE64
     */
    public static String encodeWithMD5(String text)
    {
        final char[] chars =
                new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        try {
            byte[] bytes =text.getBytes();

            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);

            byte[] encodeBytes =messageDigest.digest();

            char[] resultChars =new char[encodeBytes.length*2];

            int k =0;
            for(int i=0;i<encodeBytes.length;i++)
            {
                //0xf =1111
                //第一句取高4位 第二句就是取第四位
                resultChars[k++] =chars[encodeBytes[i]>>>4&0xf];
                resultChars[k++] =chars[encodeBytes[i]&0xf];
            }
            return new String(resultChars);

        }catch (Exception ex)
        {
            return text;
        }
    }

    /**
     * base64的原理是 3*8->4*（00+6）
     * 3个字节转换4个字节 8不足末尾补0 3不足 补=
     * @param text
     * @return
     */
    public static String encodeWithBase64(String text)
    {
        byte[] source =text.getBytes();

        BASE64Encoder encoder =new BASE64Encoder();
        return encoder.encode(source);
    }

    public static String decodeWithBase64(String text)
    {
        BASE64Decoder decoder =new BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(text));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String EncodePassword(String password)
    {
        return encodeWithMD5(StringUtils.reverse(encodeWithMD5(password)));
    }

    /*public static void main(String[] args) {
        String text ="0123";
        System.out.println(encodeWithMD5(text));
        System.out.println(encodeWithMD5(text));

        System.out.println(0xf ==15);

        String textEncodedByBase64 =encodeWithBase64(text);
        System.out.println(textEncodedByBase64);
        System.out.println(decodeWithBase64(textEncodedByBase64));

    }*/
}
