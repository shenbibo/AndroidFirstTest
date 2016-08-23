package download.test.bean;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Future;

import android.os.Environment;
import common.tools.StringUtlis;
import download.test.utils.ImageHttpUtils;
import download.test.viewbeans.BaseBean;

/**
 * 下载文件的数据
 * 
 * @author sWX284798
 * @date 2015年11月24日 上午9:42:17
 * @version
 * @since
 */
public class DownloadFileInfo extends BaseBean
{
    
    public static final String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "DownloadFileCache";
    
    public interface DownloadStatus
    {
        int DOWNLOADING = 0;
        
        int DOWNLOADED = 1;
        
        int DOWNLOAD_FAILED = 2;
        
        int DOWNLOAD_WAIT = 3;
        
        int DOWNLOAD_CANCEL = 4;
        
        /**
         * 初始化状态。
         * */
        int DOWNLOAD = 5;
        
        int DOWMLOAD_PAUSE = 6;
        
        /**
         * 准备下载，此时下载线程已经启动。
         * */
        int DOWNLOAD_READY = 7;
        
        /**
         * 抛出异常导致的下载失败
         * */
        int DOWNLOAD_EXCEPTION = 8;
        
    }
    
    /**
     * 中断下载的原因
     * */
    public interface InterruptReason
    {
        
        /**
         * 默认
         * */
        int DEFAULT = 0;
        /**
         * 用户暂停*/
        int USER_PAUSED = 1;
        /**
         * 用户取消*/
        int USER_CANCEL = 2;
        /**
         * 文件长度不合法*/
        int FILE_LENGTH_INVALID = 3;
        /**
         * 网络改变
         * */
        int NETWORK_CHANGED = 4;
        /**
         * 文件校验不匹配
         * */
        int FIEL_CHECK_INVALID = 5;
        /**
         * 下载完成
         * */
        int DOWNLOAD_END = 6;
        /**
         * 下载所需空间不足
         * */
        int SPACE_NOT_ENCOUGH = 7;
        /**
         * 下载写入过程中的空间不足
         * */
        int WRITE_SPACE_NOT_ENCOUGH = 8;
        /**
         * 差分合成失败
         * */
        int PATH_ERROR = 9;
        
    }
    
    public String url;
    
    /**
     * 将url使用md5转换的16进制表示的字符串
     * */
    public String urlMD5HexStr;
    
    /**
     * 已经下载的文件的字节大小
     * */
    protected long alreadyDownloadCounts;
    
    /**
     * 下载进度。
     * */
    public int downloadProgress = 0;
    
    /**
     * 下载状态。
     * */
    public int downloadStatus = DownloadStatus.DOWNLOAD;
    
    public String appName;
    
    
    /**
     * 是否中断任务
     * */
    protected boolean interrupt;
    
    protected int interruptReason = InterruptReason.DEFAULT; 
    
    public String appSize;
    
    /**
     * 本地保存的文件的名称
     * */
    protected String localFileName;
    
    /**
     * 下载过程中用于缓存
     * */
    protected String tempLocalFileName;
    
    /**
     * 下载速度，字节单位
     * */
    protected int downloadSpeed;
    
    /**
     * 要执行任务的返回对象
     * */
    protected Future<?> future;
    
    /**
     * 文件大小
     * */
    protected long fileSize;
    
    /**
     * 
     * */
    
    
    public static final String covertIntegerSpeedToString(int speed)
    {
        if(speed < 0)
        {
            throw new RuntimeException("speed < 0, speed = " + speed);
        }
        String speedUnit = null;
        //double speedD = speed * 1.0;
        double speedEnd = 0.0;
        if((speed / 1024) < 1)
        {
            speedEnd = speed;
            speedUnit = "B/s";
        }
        else if((speed / (1024 * 1024)) < 1)
        {
            speedEnd = speed * 1.0 / 1024;
            speedUnit = "KB/s";
        }
        else
        {
            speedEnd = speed * 1.0 / (1024 * 1024);
            speedUnit = "MB/s";
        }
        return new BigDecimal(speedEnd).setScale(2, RoundingMode.HALF_UP).doubleValue() + speedUnit;
    }
    
    
    /**
     * 
     */
    public DownloadFileInfo(String url)
    {
        this.url = url;
        urlMD5HexStr = StringUtlis.genMD5HexStr(url);
        localFileName = genLocalFileName(url);
        tempLocalFileName = localFileName + ".bak";
    }
    
    /**
     * 
     */
    public DownloadFileInfo(String appName, String size, String url)
    {
        this(url);
        this.appName = appName;
        this.appSize = size;
        
    }
    
    /**
     * 
     * */
    public void setTempDownloadFile(String fileName)
    {
        tempLocalFileName = fileName;
    }
    
    public String getTempDownloadFile()
    {
        return DownloadFileInfo.LOCAL_FILE_PATH + File.separator + tempLocalFileName;
    }
    
    public synchronized boolean isInterrupt()
    {
        return interrupt;
    }
    
    public synchronized void setInterrupt(boolean isInterrupt)
    {
        interrupt = isInterrupt;
    }
    
    public synchronized void setAlreadyDownloadCount(long counts)
    {
        alreadyDownloadCounts = counts;
    }
    
    public synchronized long getAlreadyDownloadCount()
    {
        return alreadyDownloadCounts;
    }
    
    public synchronized void setInterruptByReason(boolean isInterrupt, int interruptReason)
    {
        interrupt = isInterrupt;
        this.interruptReason = interruptReason;
    }
    
    public synchronized void setStatus(int status)
    {
        downloadStatus = status;
    }
    
    public synchronized int getStatus()
    {
        return downloadStatus;
    }
    
    public String getLocalFile()
    {
        return DownloadFileInfo.LOCAL_FILE_PATH + File.separator + localFileName;
    }
    
    public synchronized int getProgress()
    {
        return downloadProgress;
    }
    
    public synchronized void setProgress(int progress)
    {
        downloadProgress = progress;
    }
    
    public synchronized int getDownloadSpeed()
    {
        return downloadSpeed;
    }
    
    public synchronized void setDownloadSpeed(int speed)
    {
        downloadSpeed = speed;
    }
    
    public synchronized void setFileSize(long fileSize)
    {
        this.fileSize = fileSize;
    }
    
    public synchronized long getFileSize()
    {
        return fileSize;
    }
    
    public synchronized void setFuture(Future<?> future)
    {
        this.future = future;
    }
    
    public synchronized Future<?> getFuture()
    {
        return future;
    }
    
    public synchronized int getInterruptReason()
    {
        return interruptReason;
    }
    
    /**
     * 根据URL截取app的名称，截取com.xx...apk这一字符子串，若不存在这一子串，则直接返回url的MD5字符串 + “.apk”。
     * */
    private String genLocalFileName(String url)
    {
        String retStr = null;
        int startPos = url.indexOf("com");
        int endPos = url.indexOf("?");
        if(startPos != -1 && endPos != -1 && endPos > startPos)
        {
            retStr = url.substring(startPos, endPos);
        }
        else
        {
            retStr = ImageHttpUtils.genImageFileName(url) + ".apk";
        }
        return retStr;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        
        return "downloadFile = " + appName;
    }
}
