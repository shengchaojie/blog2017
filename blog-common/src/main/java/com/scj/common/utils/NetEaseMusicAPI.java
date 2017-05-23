package com.scj.common.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Utf8;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shengchaojie on 2017/5/23.
 */
public class NetEaseMusicAPI {
    private final static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7" +
            "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280" +
            "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932" +
            "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" +
            "3ece0462db0a22b8e7";
    private final static String nonce = "0CoJUm6Qyw8W8jud";
    private final static String pubKey = "010001";
    private final static  String headers[][] = {{"Accept","*/*"},
            {"Accept-Encoding","deflate,sdch"},
            {"Accept-Language","zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4"},
            {"Connection","keep-alive"},
            {"Content-Type","application/x-www-form-urlencoded"},
            {"Host","music.163.com"},
            {"Referer","http://music.163.com/search/"},
            {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36"}
    };
    public static final String noLoginJson = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";


    private static  String aesEncrypt(String text, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(text.getBytes());

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            //ignore
            return null;
        }
    }

    private static   String rsaEncrypt(String text, String pubKey, String modulus){
        text = new StringBuilder(text).reverse().toString();
        BigInteger valueInt = hexToBigInteger(stringToHex(text));
        //BigInteger pubkey = hexToBigInteger("010001");
        //BigInteger modulus = hexToBigInteger("00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7");
        return valueInt.modPow(hexToBigInteger(pubKey), hexToBigInteger(modulus)).toString(16);
    }

    private static BigInteger hexToBigInteger(String hex) {
        return new BigInteger(hex, 16);
    }

    private static String stringToHex(String text)   {

        try {
            return DatatypeConverter.printHexBinary(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String createSecretKey(int i) {
        return RandomStringUtils.random(i, "0123456789abcde");
    }

    public static List<NameValuePair> encryptedRequest(String text) {
        HashMap<String,String> result =new HashMap<>();
        String secKey = createSecretKey(16);
        String encText = aesEncrypt(aesEncrypt(text, nonce), secKey);
        String encSecKey = rsaEncrypt(secKey, pubKey, modulus);

        List<NameValuePair> params =new ArrayList<>();
        params.add(new BasicNameValuePair("params",encText));
        params.add(new BasicNameValuePair("encSecKey",encSecKey));

        return params;
    }

    public static String sendPostRequest(String url,List<NameValuePair> nameValuePairs){
        String response =null;
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost =new HttpPost(url);
        UrlEncodedFormEntity entity =new UrlEncodedFormEntity(nameValuePairs, Charsets.UTF_8);
        httpPost.setEntity(entity);

        try {
            response =httpClient.execute(httpPost, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    return FileUtil.getStringFromInputStream(httpResponse.getEntity().getContent());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
