package com.scj.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengchaojie on 2017/8/18.
 */
public class ImageUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private static final String JPEG_MAGIC_NUMBER = "FFD8";

    private static final String PNG_MAGIC_NUMBER = "89504E470D0A1A0A";

    private static final String GIF_MAGIC_NUMBER1 = "474946383761";

    private static final String GIF_MAGIC_NUMBER2 = "474946383961";

    private static final String BMP_MAGIC_NUMBER = "424D";

    private static final Map<String,String> magicNumberMap =new HashMap<>();

    private static final Integer minByteSize = 8;

    static{
        magicNumberMap.put(JPEG_MAGIC_NUMBER,"jpg");
        magicNumberMap.put(PNG_MAGIC_NUMBER,"png");
        magicNumberMap.put(GIF_MAGIC_NUMBER1,"gif");
        magicNumberMap.put(GIF_MAGIC_NUMBER2,"gif");
        magicNumberMap.put(BMP_MAGIC_NUMBER,"bmp");
    }

    /**
     * 检查输入的流是否为图片流,
     * @param bytes
     * @return 如果是图片,返回图片的后缀,如果不是图片,返回null
     */
    public static String isImage(byte[] bytes){
        if(bytes.length< minByteSize){
            logger.info("检查的流文件太小");
            return null;
        }
        String hexString = ByteUtil.byteArrayToHexString(bytes);
        if(StringUtils.isEmpty(hexString)){
            return null;
        }
        for (Map.Entry<String,String> entry :magicNumberMap.entrySet()){
            String magicNumber =entry.getKey();
            if(hexString.startsWith(magicNumber)){
                return entry.getValue();
            }
        }
        return null;
    }

    /*public static void main(String[] args) {
        String testString = "4749463837615464654643132134564634354354";
        InputStream is =new ByteArrayInputStream(ByteUtil.hexStringToByteArray(testString));
        System.out.println(isImage(is));
    }*/
}
