package com.scj.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/3/12 0012.
 */
public class FileUtil {
    public static String getStringFromInputStream(InputStream inputStream){
        ByteArrayOutputStream bao = null;
        try {
            bao = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = inputStream.read(buffer)) != -1) {
                bao.write(buffer, 0, size);
            }
            return bao.toString();
        } catch (IOException ex) {
            return null;
        } finally {
            if (bao != null) {
                try {
                    bao.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
