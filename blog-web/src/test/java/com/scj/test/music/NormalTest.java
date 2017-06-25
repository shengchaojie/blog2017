package com.scj.test.music;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * Created by shengchaojie on 2017/5/22.
 */

public class NormalTest {

    @Test
    public void testMaxInvokeCount(){
        long count =0;
        while (true){
            try {
                Document doc = Jsoup.connect("http://music.163.com/artist?id="+(4397+count))
                        .proxy("119.4.13.85",808)
                        .get();
                //System.out.println(doc.body().html().length());
                System.out.println(count);
                count++;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println(count);
    }

    @Test
    public void testDateTimeFormat(){
        //http://blog.csdn.net/cml_blog/article/details/48787593
        //Date date = DateTime.parse("1988.04.10",DateTimeFormat.forPattern("yyyy.MM.dd").withZoneUTC()).toDate();
        //System.out.println(date);

        DateTimeFormatter dtf =DateTimeFormat.forPattern("yyyy.MM.dd");
        LocalTime localTime= dtf.parseLocalTime("1988.04.10");
        System.out.println(localTime);
    }
}
