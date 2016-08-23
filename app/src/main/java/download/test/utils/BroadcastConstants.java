package download.test.utils;

import com.example.androidfirsttest.MyApplication;

/**
 * 广播常量
 * 
 * @author sWX284798
 * @date 2015年11月30日 下午7:02:05
 * @version
 * @since
 */
public final class BroadcastConstants
{

    public interface DownloadAirConstants
    {
        /**
         * 下载进度通知
         * */
        String PROGRESS_ACTION = MyApplication.getInstance().getPackageName() + "_downloadProgressAction";
        
        /**
         * 下载状态，数据发生改变
         * */
        String STATUS_ACTION = MyApplication.getInstance().getPackageName() + "_downloadStatusChangedAction";
    }
}
