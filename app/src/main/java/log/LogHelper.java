package log;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
final class LogHelper {
    /**
     * 2018-02-26 16:53:25.123
     */
    static final String DATA_FORMAT = "YYYY-MM-dd HH:mm:ss:SSS";

    static final char SPRIT = '/';

    static final char LEFE_BRACKET = '(';

    static final char RIGHT_BRACKET = ')';

    static final char SPACE = ' ';

    /**
     * 获取堆栈，使用系统自带的获取堆栈的方法，效率更高
     */
    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    /**
     * 获取当前时间的格式化字符串
     */
    public static String formatCurTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DATA_FORMAT, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 获取日志级别对应的字符
     */
    public static String getPriorityString(int priority) {
        switch (priority) {
            case Logger.VERBOSE:
                return "V";
            case Logger.DEBUG:
                return "D";
            case Logger.INFO:
                return "I";
            case Logger.WARN:
                return "W";
            case Logger.ERROR:
                return "E";
            case Logger.ASSERT:
                return "A";
            default:
                return "unknown";

        }
    }

    /**
     * 设计日志的头部
     * */
    public static StringBuilder getLogTitle(int priority, String tag){
        StringBuilder sb = new StringBuilder(72);
        sb.append()
    }
}
