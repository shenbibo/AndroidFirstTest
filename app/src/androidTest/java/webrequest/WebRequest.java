package webrequest;

import android.support.test.runner.AndroidJUnit4;

import com.sky.slog.Slog;

import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit.common.HttpUtils;
import retrofit.common.SimpleHttpCallback;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
@RunWith(AndroidJUnit4.class)
public class WebRequest {

    @Test
    public void runWebRequest() {
        HttpUtils.init("http://www.icbc.com.cn/ICBCDynamicSite/Charts/GoldTendencyPicture.aspx/");
        HttpUtils.get(new WebBean(), new SimpleHttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Slog.i(s);
            }

            @Override
            public void onFailure(Throwable e) {
                Slog.e(e);
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
