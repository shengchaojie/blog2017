package com.scj.test.music;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by shengchaojie on 2017/5/22.
 */

public class NormalTest {

    @Test
    public void testMaxInvokeCount(){
        long count =0;
        while (true){
            try {
                Document doc = Jsoup.connect("http://music.163.com/artist?id="+(4397+count)).get();
                System.out.println(doc.body().html().length());
                System.out.println(count);
                count++;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println(count);
    }
}
