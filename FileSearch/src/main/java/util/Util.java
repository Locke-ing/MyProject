package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 解析文件大小为中文描述
     * @param size
     * @return
     */
    public static String parseSize(long size) {
        String[] unit = {"B", "KB", "MB", "GB"};
        int idx = 0;
        while(size > 1024 && idx < unit.length-1){
            size /= 1024;
            idx++;
        }
        return size + unit[idx];
    }

    /**
     * 解析日期为中文日期描述
     * @param lastModified
     * @return
     */
    public static String parseDate(Date lastModified) {
        return new SimpleDateFormat(DATE_PATTERN).format(lastModified);
    }
}
