package com.scj.common.utils;

/**
 * Created by shengchaojie on 2017/8/18.
 */
public class ByteUtil {

    public static String byteToHexString(byte aByte){
        return Integer.toHexString(aByte&0xFF).toUpperCase();
    }

    public static String byteArrayToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte b :bytes){
            String hexString = Integer.toHexString(b&0xff).toUpperCase();
            if(hexString.length()<2){
                sb.append("0");
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String hexString){
        if(hexString.length()%2 !=0){
            throw new IllegalArgumentException("hexString必须为偶数");
        }
        hexString =hexString.toUpperCase();
        int byteLength =hexString.length()/2;
        byte [] result =new byte[byteLength];
        for(int i =0;i<byteLength;i++){
            byte b = (byte) ("0123456789ABCDEF".indexOf(hexString.charAt(i*2))<<4 | "0123456789ABCDEF".indexOf(hexString.charAt(i*2+1)));
            result[i] =b;
        }
        return result;
    }

    public static void main(String[] args) {
        byte[] bytes =hexStringToByteArray("aBCDEFCCDE");
        System.out.println(byteArrayToHexString(bytes));
    }
}
