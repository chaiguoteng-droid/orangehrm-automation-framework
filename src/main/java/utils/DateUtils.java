package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    // 获取当前时间戳，用于生成唯一的测试数据
    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
    }
}
