package download.test.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.androidfirsttest.MyApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月11日 下午2:33:39
 * @version
 * @since
 */
public class NetworkUtils
{
    private static final String TAG = "NetworkUtils";
    private static volatile ConnectivityManager cm = null;
    
    /**
     * 用于请求网络数据的线程池。
     * */
    public static final ThreadPoolExecutor DATA_EXECUTOR = new ThreadPoolExecutor(
            0,  //基本大小为0
            2,  //线程池最大大小
            10, //空闲等待超时时间
            TimeUnit.SECONDS, //超时时间单位
            new LinkedBlockingQueue<Runnable>() //使用无界队列
            );
    
    /**
     * 用于下载图片的线程池
     * */
    public static final Executor IMAGE_EXECUTOR = new ThreadPoolExecutor(
            0,  //基本大小为0
            5,  //线程池最大大小
            10, //空闲等待超时时间
            TimeUnit.SECONDS, //超时时间单位
            new LinkedBlockingQueue<Runnable>() //使用无界队列
            );
    
    /**
     * 用于下载文件（apk）的线程池
     * */
    public static final ThreadPoolExecutor FILE_DOWNLOAD_EXECUTOR = new ThreadPoolExecutor(
            2,
            2, //最大允许启动的线程数，当且仅当任务队列达到上限，基本线程全在运行时
            200, 
            TimeUnit.MILLISECONDS, 
            new LinkedBlockingQueue<Runnable>(), //下载队列最多100个
            new ThreadPoolExecutor.DiscardOldestPolicy()  //若工作队列与线程池全满，则采取丢弃最旧的任务，再重新执行execute方法.
            );
    
    /**
     * 网络已连接则返回true，否则返回false
     * */
    public static boolean isNetConnected()
    {
        try
        {
            if(cm == null)
            {
                synchronized (NetworkUtils.class)
                {
                    if(cm == null)
                    {
                        cm = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
                    }
                }
            }
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isConnected())
            {
                if(netInfo.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        catch(Exception e)
        {
            Log.d(TAG, "get network status error return false", e);
        }
        return false;
    }
    

}
