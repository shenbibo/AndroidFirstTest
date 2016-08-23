package datetest;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * 测试将给定的毫秒数转化对应的日期。
 * 
 * @author sWX284798
 * @date 2015年9月10日 上午11:22:58
 * @version
 * @since
 */
public class DateTest
{
    private static final String TAG = "DateTest";
    public static void timeTest(long ms)
    {
        if(ms < 0)
        {
            Log.e(TAG, "ms < 0, ms = " + ms);
        }
        Date date = new Date(ms);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = sdf.format(date);
        Log.d(TAG, "time = " + time);
    }
}
