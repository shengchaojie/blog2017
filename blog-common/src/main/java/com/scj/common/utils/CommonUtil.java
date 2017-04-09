package com.scj.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shengcj on 2016/12/14.
 */
public class CommonUtil {
    public static String generateUID(String username, String password)
    {
        Calendar calendar =Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,2);
        Date expireTime =calendar.getTime();

        return generateUID(username,password,expireTime, "scjzuiniubi");
    }

    public static String generateUID(String username, String password, Date datetime, String salt) {
        long milliseconds =datetime.getTime();
        int hash = generateUIDHash(username,password,String.valueOf(milliseconds),salt);

        return username + "|" + milliseconds + "|" + hash;
    }

    public static int generateUIDHash(String username, String password, String datetime, String salt) {
        return (username + datetime + password.substring(0, 3) + salt).hashCode();
    }
}
