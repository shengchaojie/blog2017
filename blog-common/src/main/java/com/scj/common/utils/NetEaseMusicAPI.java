package com.scj.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.scj.common.exception.BusinessException;
import com.scj.common.exception.StatusCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by shengchaojie on 2017/5/23.
 */
@Component
public class NetEaseMusicAPI {

    //@Resource
    //private NetEaseMusicCookieStore netEaseMusicCookieStore;

    /**
     * 安全性考虑
     *每次请求cookie可以缓存起来，一旦不是同一次请求就需要重新拿cookie
     */
    private ThreadLocal<Map<String,CookieStore>> userCookieTL =new ThreadLocal<Map<String,CookieStore>>(){
        @Override
        protected Map<String,CookieStore> initialValue() {
            return new HashMap<>();
        }
    };


    private final  String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7" +
            "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280" +
            "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932" +
            "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" +
            "3ece0462db0a22b8e7";
    private final  String nonce = "0CoJUm6Qyw8W8jud";
    private final  String pubKey = "010001";
    private final  String headers[][] = {{"Accept","*/*"},
            {"Accept-Encoding","deflate,sdch"},
            {"Accept-Language","zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4"},
            {"Connection","keep-alive"},
            {"Content-Type","application/x-www-form-urlencoded"},
            {"Host","music.163.com"},
            {"Referer","http://music.163.com/search/"},
            {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36"}
    };
    public static final String noLoginJson = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";


    public NetEaseMusicAPI() {
    }

    private   String aesEncrypt(String text, String key) {
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

    private  String rsaEncrypt(String text, String pubKey, String modulus){
        text = new StringBuilder(text).reverse().toString();
        BigInteger valueInt = hexToBigInteger(stringToHex(text));
        //BigInteger pubkey = hexToBigInteger("010001");
        //BigInteger modulus = hexToBigInteger("00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7");
        return valueInt.modPow(hexToBigInteger(pubKey), hexToBigInteger(modulus)).toString(16);
    }

    private  BigInteger hexToBigInteger(String hex) {
        return new BigInteger(hex, 16);
    }

    private  String stringToHex(String text)   {

        try {
            return DatatypeConverter.printHexBinary(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private  String createSecretKey(int i) {
        return RandomStringUtils.random(i, "0123456789abcde");
    }

    public  List<NameValuePair> encryptedRequest(String text) {
        HashMap<String,String> result =new HashMap<>();
        String secKey = createSecretKey(16);
        String encText = aesEncrypt(aesEncrypt(text, nonce), secKey);
        String encSecKey = rsaEncrypt(secKey, pubKey, modulus);

        List<NameValuePair> params =new ArrayList<>();
        params.add(new BasicNameValuePair("params",encText));
        params.add(new BasicNameValuePair("encSecKey",encSecKey));

        return params;
    }

    public  String sendPostRequest(String url,List<NameValuePair> nameValuePairs,CookieStore cookieStore){
        String response =null;
        HttpClient httpClient = HttpClients.createDefault();
        HttpClientContext httpClientContext =HttpClientContext.create();
        httpClientContext.setCookieStore(cookieStore);
        HttpPost httpPost =new HttpPost(url);
        UrlEncodedFormEntity entity =new UrlEncodedFormEntity(nameValuePairs, Charsets.UTF_8);
        httpPost.setEntity(entity);
        try {
            response =httpClient.execute(httpPost, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    return FileUtil.getStringFromInputStream(httpResponse.getEntity().getContent());
                }
            },httpClientContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private HttpClient createProxyHttpClient(){
        return HttpClientBuilder.create().setProxy(HttpHost.create("http://localhost:8888")).build();
    }

    /**
     * http://blog.csdn.net/Ciiiiiing/article/details/62434438
     * @param songId
     * @return
     */
    public String getSongMp3Url(String songId){
        String first_param = "{\"ids\":\"[" + songId + "]\",\"br\":128000,\"csrf_token\":\"\"}";
        String response = sendPostRequest("http://music.163.com/weapi/song/enhance/player/url?csrf_token=",encryptedRequest(first_param),null);
        if(!StringUtils.isEmpty(response)){
            JSONObject jsonObject= (JSONObject) JSONObject.parse(response);//data//url
            if(jsonObject!=null&&jsonObject.containsKey("data")){
                JSONArray array = (JSONArray) jsonObject.get("data");
                if(array!=null&&array.size()>0){
                    JSONObject object =array.getJSONObject(0);
                    if(object.containsKey("url")){
                        return object.get("url").toString();
                    }
                }
            }
        }
        return "";
    }

    /**
     * 登陆功能 主要是把cookie拿到,之后的请求都需要这个cookie
     * @param username
     * @param password
     */
    public void login(String username,String password){
        Pattern pattern =Pattern.compile("^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$");
        //默认邮箱 这个加密有问题
        String text ="{\"username\": \""+username+"\", \"rememberLogin\": \"true\", \"password\": \""+ EncryptUtil.encodeWithMD5(password).toLowerCase()+"\",\"csrf_token\":\"\",\"clientToken\":\"1_57RkADExG47oFCwP2WBOKb4lUsQnaWoP_ALI2GM9a14g2CyiH0t5LxLb59AO5KUSB_h+B5M7QxhyNCR3dFM9dPUg==\"}";
        String url = "https://music.163.com/weapi/login";
        if(pattern.matcher(username).matches()){//手机
            text ="{\"phone\": \""+username+"\", \"rememberLogin\": \"true\", \"password\": \""+ EncryptUtil.encodeWithMD5(password).toLowerCase()+"\"}";
            url = "https://music.163.com/weapi/login/cellphone";
        }else{
            throw new BusinessException(StatusCode.NEM_NOT_SUPPORT_EMAIL);
        }
        String response = null;
        List<NameValuePair> data =encryptedRequest(text);
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost =new HttpPost(url);
        UrlEncodedFormEntity entity =new UrlEncodedFormEntity(data, Charsets.UTF_8);
        httpPost.setEntity(entity);
        for(String[] header:headers){
            httpPost.addHeader(header[0],header[1]);
        }
        BasicCookieStore cookieStore = new BasicCookieStore();
        try {
            response = httpClient.execute(httpPost, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    //直接拿cookie拿不到全部,直接从请求头拿cookie解析
                    Header[] cookieHeaders =httpResponse.getHeaders("Set-Cookie");
                    for(Header header :cookieHeaders){
                        Integer firstEqualIndex =header.getValue().indexOf("=",0);
                        String key =header.getValue().substring(0,firstEqualIndex);
                        String value =header.getValue().substring(firstEqualIndex+1,header.getValue().indexOf(";",firstEqualIndex));
                        BasicClientCookie basicClientCookie =new BasicClientCookie(key,value);
                        basicClientCookie.setPath("/");
                        basicClientCookie.setDomain("music.163.com");
                        cookieStore.addCookie(basicClientCookie);
                    }
                    return FileUtil.getStringFromInputStream(httpResponse.getEntity().getContent());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject resultObject =JSON.parseObject(response);
        Integer code =(Integer) resultObject.get("code");
        if(Objects.equals(200,code)){

        }else if(Objects.equals(502,code)){
            throw new BusinessException(StatusCode.NEM_INVALID_PASSWORD);
        }else{
            throw new BusinessException(StatusCode.NEM_UN_KNOW_ERROR);
        }
        userCookieTL.get().put(username,cookieStore);
    }

    /**
     * 创建一个播放列表,返回这个列表的ID
     * @param username
     * @param password
     * @param playListName 播放列表名字
     * @return
     */
    public Integer createPlaylist(String username,String password,String playListName){
        CookieStore cookieStore = getUserCookieStore(username, password);
        String csrf =null;
        for(Cookie cookie :cookieStore.getCookies()){
            if("__csrf".equals(cookie.getName())){
                csrf =cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(csrf)){
            throw new BusinessException(StatusCode.NEM_NO_CSRF);
        }
        //String requestJson ="{name:\""+playListName+"\",csrf_token:\""+csrf+"\"}";
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("name",playListName);
        jsonObject.put("csrf_token",csrf);
        String result =sendPostRequest("http://music.163.com/weapi/playlist/create?csrf_token=",encryptedRequest(jsonObject.toJSONString()),cookieStore);
        JSONObject resultObject = (JSONObject)JSON.parse(result);
        Integer code = (Integer) resultObject.get("code");
        if(Objects.equals(200,code)){
            return (Integer) resultObject.get("id");
        }else{
            throw new BusinessException(StatusCode.NEM_UN_KNOW_ERROR);
        }
    }

    public void addSongToPlayList(String username,String password,List<Integer> songIds,Integer PlayListId){
        CookieStore cookieStore = getUserCookieStore(username, password);
        String csrf =null;
        for(Cookie cookie :cookieStore.getCookies()){
            if("__csrf".equals(cookie.getName())){
                csrf =cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(csrf)){
            throw new BusinessException(StatusCode.NEM_NO_CSRF);
        }
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("csrf_token","");
        jsonObject.put("op","add");
        jsonObject.put("pid",PlayListId.toString());
        jsonObject.put("trackIds", JSON.toJSONString(Lists.newArrayList(songIds)));
        //jsonObject.put("tracks","[object Object]");
        String requestJson =jsonObject.toJSONString();
        System.out.println(requestJson);
        //String requestJson ="{tracks:\"[object Object]\",pid:\"" + PlayListId + "\",trackIds:\"[" + songId + "]\",op:\"add\",csrf_token:\""+csrf+"\"}";
        String result =sendPostRequest("http://music.163.com/weapi/playlist/manipulate/tracks?csrf_token=",encryptedRequest(requestJson),cookieStore);
        JSONObject resultObject = (JSONObject)JSON.parse(result);
        Integer code = (Integer) resultObject.get("code");
        if(Objects.equals(200,code)){

        }else if(Objects.equals(502,code)){
            throw new BusinessException(StatusCode.NEM_REPEAT_SONG);
        }else{
            throw new BusinessException(StatusCode.NEM_UN_KNOW_ERROR);
        }
    }

    private CookieStore getUserCookieStore(String username, String password) {
        if(!userCookieTL.get().containsKey(username)){
            login(username,password);
        }
        CookieStore cookieStore =userCookieTL.get().get(username);
        if(cookieStore ==null){
            throw new BusinessException(StatusCode.USERNAME_PASSWORD_WRONG);
        }
        return cookieStore;
    }

    public static void main(String[] args) {
        Pattern pattern =Pattern.compile("^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$");
        System.out.println(pattern.matcher("13388611621").matches());

    }
}
