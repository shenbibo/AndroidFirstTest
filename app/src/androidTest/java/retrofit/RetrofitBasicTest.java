package retrofit;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.sky.slog.Slog;

import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/6/30.
 */
@RunWith(AndroidJUnit4.class)
public class RetrofitBasicTest {
    public static final String TAG = "RetrofitBasicTest";
    @Test
    public void getCityInfoTest(){
        Slog.t(TAG).i("start getCityInfo test");
        WeatherRequest.getCityInfo();
        SystemClock.sleep(5000);

        Slog.t(TAG).i("start getCityInfo2 test");
        WeatherRequest.getCityInfo2();
        SystemClock.sleep(5000);
    }
}
