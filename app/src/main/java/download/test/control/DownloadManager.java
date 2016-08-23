package download.test.control;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.example.androidfirsttest.MyApplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import download.test.bean.DownloadFileInfo;
import download.test.bean.DownloadFileInfo.DownloadStatus;
import download.test.bean.DownloadFileInfo.InterruptReason;
import download.test.utils.BroadcastConstants.DownloadAirConstants;
import download.test.utils.NetworkUtils;

/**
 * 
 * 下载管理类，单例模式
 * @author sWX284798
 * @date 2015年11月28日 上午11:04:33
 * @version
 * @since
 */
public class DownloadManager
{

    private static final String TAG = "DownloadManager";
    
    private volatile static DownloadManager manager;
    
    private List<DownloadFileInfo> taskList = new LinkedList<DownloadFileInfo>();
    
    /**
     * 执行下载任务的线程池
     * */
    private ThreadPoolExecutor executor = NetworkUtils.FILE_DOWNLOAD_EXECUTOR;
    
    private DownloadManager()
    {}
    
    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(android.os.Message msg) 
        {
            int status = msg.what;
            switch(status)
            {
                case DownloadStatus.DOWNLOADING:
                    
                    Log.d(TAG, "downloadStatus = downloading" );
                    //刷新下载进度
                    sendBroadcast(status);
                    break;
                     
                default:
                    sendBroadcast(status);
                    break;
            }
        };
    };
    
    
   private void sendBroadcast(int downloadStatus)
   {
       Intent i = new Intent();
       String action;
       if(downloadStatus == DownloadStatus.DOWNLOADING)
       {
           action = DownloadAirConstants.PROGRESS_ACTION;
       }
       else
       {
           action = DownloadAirConstants.STATUS_ACTION;
       }
       i.setAction(action);
       MyApplication.getInstance().sendBroadcast(i);
       
   }
    
    /**
     * 获取下载管理的的单例
     * */
    public static DownloadManager getInstance()
    {
        if(manager == null)
        {
            synchronized(DownloadManager.class)
            {
                if(manager == null)
                {
                    manager = new DownloadManager();
                }
            }
        }
        return manager;
    }
    
    /**
     * 开启新的下载任务
     * */
    public boolean startNewDownloadTask(DownloadFileInfo info)
    {
        if(info.getInterruptReason() != InterruptReason.DOWNLOAD_END)
        {
            info.setInterrupt(false);
            info.setStatus(DownloadStatus.DOWNLOAD_WAIT);
            addTask(info);
            handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
            Future<?> future = executor.submit(new DownloadRunnable(info, handler));
            info.setFuture(future);

        }
        else
        {
            Toast.makeText(MyApplication.getInstance(), 
                    info.appName + " is download finished", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    
    public boolean addTask(DownloadFileInfo info)
    {
        synchronized(taskList)
        {
            if(!isTaskRepeat(info))
            {
                taskList.add(info);
                return true;
            }
        }
        return false;
    }
    
    public boolean removeTask(DownloadFileInfo info)
    {
        synchronized (taskList)
        {
            return taskList.remove(info);
        }
    }
    
    public void clearTaskList()
    {
        synchronized(taskList)
        {
            taskList.clear();
        }
    }
    
    /**
     * 全部下载
     * */
    public void startAllTask(List<DownloadFileInfo> infoList)
    {
        for (DownloadFileInfo info : infoList)
        {
            //当认为的状态不为下载完成、下载中、等待下载、准备下载时，才作为新任务启动
            if(info.getStatus() != DownloadStatus.DOWNLOADED &&
                    info.getStatus() != DownloadStatus.DOWNLOADING &&
                    info.getStatus() != DownloadStatus.DOWNLOAD_READY &&
                    info.getStatus() != DownloadStatus.DOWNLOAD_WAIT)
            {
                startNewDownloadTask(info);
            }
        }
    }
    
    /**
     * 取消全部下载任务
     * */
    public void cancelAllTask(List<DownloadFileInfo> infoList)
    {
        for (DownloadFileInfo info : infoList)
        {
            //当认为的状态为下载中、等待下载、准备下载、暂停下载才取消下载
            if(info.getStatus() == DownloadStatus.DOWNLOADING ||
                    info.getStatus() == DownloadStatus.DOWNLOAD_READY ||
                    info.getStatus() == DownloadStatus.DOWNLOAD_WAIT ||
                    info.getStatus() == DownloadStatus.DOWMLOAD_PAUSE)
            {
                //startNewDownloadTask(info);
                cancelDownloadTask(info);
            }
        }
    }
    
    /**
     * 恢复所有暂停下载的任务
     * */
    public void resumeAllPauseTask(List<DownloadFileInfo> infoList)
    {
        for (DownloadFileInfo info : infoList)
        {
            if(info.getStatus() == DownloadStatus.DOWMLOAD_PAUSE)
            {
                resumeDownloadTask(info);
            }
        }
    }
    
    /**
     * 暂停所有正在下载的任务
     * */
    public void pauseAllDownloadingTask(List<DownloadFileInfo> infoList)
    {
        for (DownloadFileInfo info : infoList)
        {
            //当认为的状态为下载中、等待下载、准备下载、暂停下载才停止下载
            if(info.getStatus() == DownloadStatus.DOWNLOADING ||
                    info.getStatus() == DownloadStatus.DOWNLOAD_READY ||
                    info.getStatus() == DownloadStatus.DOWNLOAD_WAIT)
            {
                //startNewDownloadTask(info);
                pauseDownloadTask(info);
            }
        }
    }
    
    /**
     * 判断新加入的任务是否已经存在。
     * */
    private boolean isTaskRepeat(DownloadFileInfo info)
    {
        //synchronized(taskList)
        {
            for (DownloadFileInfo task : taskList)
            {
                if(task.urlMD5HexStr.equalsIgnoreCase(info.urlMD5HexStr))
                {
                    return true;
                }                
            }
        }
        return false;
    }
    
    /**
     * 取消下载
     * */
    public boolean cancelDownloadTask(DownloadFileInfo info)
    {
        boolean isNeedSendMessage = false;
        if(info.getStatus() == DownloadStatus.DOWNLOAD_WAIT || 
                info.getStatus() == DownloadStatus.DOWMLOAD_PAUSE)
        {
            if(info.getFuture() != null)
            {
                info.getFuture().cancel(true);
                info.setFuture(null);
            }
            removeTask(info);
            isNeedSendMessage = true;
        }
        info.setInterruptByReason(true, InterruptReason.USER_CANCEL);
        info.setStatus(DownloadStatus.DOWNLOAD_CANCEL);
        if(isNeedSendMessage)
        {
            info.setProgress(0);
            info.setAlreadyDownloadCount(0);
            File f = new File(info.getTempDownloadFile());
            if(f.exists())
            {
                f.delete();
            }
            handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
        }
        return false;
    }
    
    /**
     * 暂停下载任务
     * */
    public boolean pauseDownloadTask(DownloadFileInfo info)
    {
        boolean isNeedSendMessage = false;
        if(info.getStatus() == DownloadStatus.DOWNLOAD_WAIT)
        {
            if(info.getFuture() != null)
            {
                info.getFuture().cancel(true);
                info.setFuture(null);
            }
            //removeTask(info);
            isNeedSendMessage = true;
        }
        info.setInterruptByReason(true, InterruptReason.USER_PAUSED);
        info.setStatus(DownloadStatus.DOWMLOAD_PAUSE);
        if(isNeedSendMessage)
        {
            handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
        }
        return false;
    }
    
    /**
     * 继续下载
     * */
    public boolean resumeDownloadTask(DownloadFileInfo info)
    {
        if(info.getInterruptReason() != InterruptReason.DOWNLOAD_END ||
                info.getStatus() != DownloadStatus.DOWNLOADED)
        {
            
            info.setInterrupt(false);
            info.setStatus(DownloadStatus.DOWNLOAD_WAIT);
            addTask(info);
            handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
            Future<?> future = executor.submit(new DownloadRunnable(info, handler));
            info.setFuture(future);

        }
        else
        {
            Toast.makeText(MyApplication.getInstance(), 
                    info.appName + " is download finished", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    
}
