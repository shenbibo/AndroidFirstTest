package download.test.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import download.test.NetworkTask;
import download.test.bean.DownloadFileInfo;
import download.test.bean.DownloadFileInfo.DownloadStatus;
import download.test.bean.DownloadFileInfo.InterruptReason;
import download.test.utils.HttpUtils;
import download.test.utils.NetworkUtils;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月27日 下午3:54:37
 * @version
 * @since
 */
public class DownloadRunnable implements Runnable
{
    private static final String TAG = DownloadRunnable.class.getSimpleName();

    private DownloadFileInfo info;
    
    private Handler handler;
    
    /**
     * 下载的起始位置，因为可以暂停.
     * */
    private long startPos = 0;
    
    /**
     * 
     */
    public DownloadRunnable(DownloadFileInfo info, Handler handler)
    {
        this.info = info;
        this.handler = handler;
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        Log.d(TAG, "task is start");
        if(NetworkUtils.isNetConnected())
        {
//            try
//            {
                //Log.d(TAG, "task is start");
                info.setStatus(DownloadStatus.DOWNLOAD_READY);
                handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
                doGet();
//            }
        }
    }

    @SuppressWarnings("deprecation")
    private void setRequestParam(HttpGet get)
    {
        //HttpParams params = request.getParams();
        if(startPos != 0)
        {
            //doGet.addHeader("Range", "bytes=" + startPos + "-");
            get.addHeader("Range", "bytes=" + startPos + "-");
        }
        
    }
    
    private void outHeader(String tag, Header[] header)
    {
        StringBuffer buf = new StringBuffer(500);
        buf.append("DownloadTask " + tag + " header, downloadFile::" + info.appName + "{");
        if (header != null) 
        {
            for (int i = 0; i < header.length; i++) 
            {
                buf.append("\n\t" + header[i].getName() + ":"+ header[i].getValue());
            }
        }
        buf.append("\n}");
        Log.d(TAG, buf.toString());
    }
    
    /**
     * 次方法返回一个数组大小为0的byte数组。
     * */
    @SuppressWarnings("deprecation")
    private void doGet() 
    {
        
        HttpClient client = null;
        HttpGet get = null;
        HttpResponse response = null;
        BufferedInputStream bis = null;
        RandomAccessFile accessFile = null;        
        HttpEntity entity = null;
        InputStream is = null;
        long responseFileLength = 0;
        File f = null;
        boolean isError = false;
        startPos = info.getAlreadyDownloadCount();
        try
        {
            //1、获取网络请求实例，设置请求参数、方法。
            client = HttpUtils.getHttpClient(NetworkTask.USER_AGENT);

            //如果临时文件存在，且位置与starPos不一致，删除文件,或者已经下载完成了，则删除重下
            f = genTempDownloadFileObj(info);
            if(f.exists() && (/*f.length() != startPos || */(info.getFileSize() != 0 && startPos == info.getFileSize())))
            {
//                throw new RuntimeException("startPos != tempFile.length, startPos = " + startPos 
//                        + ", f.length = " + f.length());
                
                if(f.delete())
                {
                    Log.d(TAG, "file is exist deleted");
                    startPos = 0;
                    info.setAlreadyDownloadCount(0);
                }
                

            }
            
            get = new HttpGet(info.url);
            Log.d(TAG, "startPos = " + startPos);
            setRequestParam(get);
            outHeader("request", get.getAllHeaders());  
            //2、执行下载请求
            response = client.execute(get);
            
            outHeader("response", response.getAllHeaders());
            int rtnCode = response.getStatusLine().getStatusCode();
            //如果为200或者206(返回部分数据，用于断点下载)则表示请求数据成功。
            if(rtnCode == HttpStatus.SC_OK || rtnCode == HttpStatus.SC_PARTIAL_CONTENT)
            {
                Log.d(TAG, "return is ok");
                //3、获取实体、内容的长度、文件的总长度
                entity = response.getEntity();
                
                //保存到外部存储
                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                {
                    
                    if(entity != null)
                    {
                        //计算设置下载的文件的长度
                        long fileLength = entity.getContentLength() + startPos;
                        responseFileLength = getFileLengthFromResponse(response);
                        Log.d(TAG, "response is ok, contentLength = " + entity.getContentLength() 
                                + ", totalLength = " + responseFileLength);
                        if(responseFileLength == 0)
                        {
                            responseFileLength = fileLength;
                        }
                        if(info.getFileSize() == 0)
                        {
                            info.setFileSize(responseFileLength);
                        }
                        
                        
                        is = entity.getContent();
                        if(is != null)
                        {
                            
                            //4、创建输入输出流
                            bis = new BufferedInputStream(is);
                            //f = genTempDownloadFileObj(info);
//                            if(f.exists() && f.length() != startPos)
//                            {
//                                throw new RuntimeException("startPos != tempFile.length, startPos = " + startPos 
//                                        + ", f.length = " + f.length());
//
//                            }
                            accessFile = new RandomAccessFile(f, "rw");
                            if(startPos != 0)
                            {
                                accessFile.seek(startPos); 
                            }
                            
                            byte[] buffer = new byte[4096];
                            Log.d(TAG, "url = " + info.url);
                            Log.d(TAG, f.getAbsolutePath());
                            
                            //5、读取网络返回数据实体中数据，保存到本地的临时文件中。
                            int percent = 0;
                            long count = startPos; //计算原有的开始位置
                            int readBytesCounts = 0;
                            //int realDownloadCounts = 0; //本次启动任务的实际下载数目
                            long nowTime = 0;
                            long keepTime = 0;
                            long receiveBlock = 0; //用于计算下载速率
                            int sec = 0; //用于计算速率的时间
                            while((readBytesCounts = bis.read(buffer)) != -1)
                            {
                                //判断是否任务被打断
                                if(info.isInterrupt() || percent > 100)
                                {
                                    Log.d(TAG, "task is interrupted, jump out loop");
                                    //doGet.abort();
                                    break;
                                }
                                
                                accessFile.write(buffer, 0, readBytesCounts);
                                count += readBytesCounts;
                                info.setAlreadyDownloadCount(count);
                                receiveBlock += readBytesCounts;
                                percent = (int)(((double)count) / responseFileLength * 100);
                                nowTime = System.currentTimeMillis();
                                
                                //6、更新下载速度、下载进度
                                //当距离上次更新的时间达到2000ms或下载百分比超过5%时需要更新数据
                                if(percent - info.getProgress() >= 5 || nowTime - keepTime >= 2000)
                                {
                                    Log.d(TAG, "downloadPercent = " + percent);                                                                       
                                    sec = (int) ((nowTime - keepTime) / 1000);
                                    sec = sec > 0 ? sec : 1;
                                    info.setDownloadSpeed(((int)(receiveBlock / sec)));
                                    keepTime = nowTime;
                                    receiveBlock = 0;
                                    info.setProgress(percent);
                                    info.setStatus(DownloadStatus.DOWNLOADING);
                                    handler.sendMessage(handler.obtainMessage(info.getStatus(), info));                            
                                }
                                
                            }
                            
                            //7、判断退出下载循环原因，若是正常下载完成，则需替换原有文件
                            //如果是非打断退出循环则判断文件是否下载完成
                            if(!info.isInterrupt())
                            {
                                //下载的数据长度与应当获取的文件不一致
                                if(count != responseFileLength)
                                {
                                    Log.d(TAG, "count != responseFileLength, count = " + count + ", length = " + responseFileLength);
                                    info.setStatus(DownloadStatus.DOWNLOAD_FAILED);
                                    info.setInterruptByReason(true, InterruptReason.FILE_LENGTH_INVALID);
                                    info.setAlreadyDownloadCount(0);
                                    handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
                                    //长度不一致需要删除临时文件
                                    if(f.exists())
                                    {
                                        f.delete();
                                    }
                                }
                                else
                                {
                                    info.setAlreadyDownloadCount(count);
                                    renameTempFile2NormalFile(f);
                                }
                            }
                            else
                            {
                                //如果中断的原因为用户取消，则需要删除下载的临时文件，并将下载进度置为0
                                if(info.getInterruptReason() == InterruptReason.USER_CANCEL)
                                {
                                    if(f.exists())
                                    {
                                        f.delete();
                                    }
                                    info.setProgress(0);
                                    info.setAlreadyDownloadCount(0);
                                    
                                }
                                
                                handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
                            }
                                               
                        }
                    }

                }
                else 
                {
                    Log.d(TAG, "Environment.getExternalStorageState() = " + Environment.getExternalStorageState());
                    info.setInterruptByReason(true, InterruptReason.SPACE_NOT_ENCOUGH);
                    isError = true;
                    //info.setStatus(DownloadStatus.DOWNLOAD_FAILED);
                }

            }
            else
            {
                Log.e(TAG, "return code = " + response.getStatusLine().getStatusCode());
                //info.setInterruptByReason(true, InterruptReason.);
                isError = true;
            }
        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isError = true;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isError = true;
        }
        finally
        {
            
            
            if(get != null)
            {
                if(!get.isAborted())
                {
                    Log.d(TAG, "doGet is not aborted");
                    get.abort();
                }
            }
            Log.d(TAG, "finaly block is entered");
            if(accessFile != null)
            {
                try
                {
                    accessFile.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                
            }
            

            
            if(is != null)
            {
                try
                {
                    //注意关闭输入流在某些情况下是一个耗时的操作，最好先掉用httpGet.abort方法。
                    //中断之后，调用该关闭流方法可能会抛出异常，表示要关闭的流已经关闭，原因未知。
                    /*
                     java.net.SocketException: recvfrom failed: EBADF (Bad file number)
                     at libcore.io.IoBridge.maybeThrowAfterRecvfrom(IoBridge.java:619)
                        at libcore.io.IoBridge.recvfrom(IoBridge.java:582)
                        at java.net.PlainSocketImpl.read(PlainSocketImpl.java:486)
                        at java.net.PlainSocketImpl.access$000(PlainSocketImpl.java:37)
                        at java.net.PlainSocketImpl$PlainSocketInputStream.read(PlainSocketImpl.java:237)
                        at org.apache.http.impl.io.AbstractSessionInputBuffer.fillBuffer(AbstractSessionInputBuffer.java:103)
                        at org.apache.http.impl.io.AbstractSessionInputBuffer.read(AbstractSessionInputBuffer.java:134)
                        at org.apache.http.impl.io.ContentLengthInputStream.read(ContentLengthInputStream.java:174)
                        at org.apache.http.impl.io.ContentLengthInputStream.read(ContentLengthInputStream.java:188)
                        at org.apache.http.impl.io.ContentLengthInputStream.close(ContentLengthInputStream.java:121)
                        at org.apache.http.conn.BasicManagedEntity.streamClosed(BasicManagedEntity.java:179)
                        at org.apache.http.conn.EofSensorInputStream.checkClose(EofSensorInputStream.java:266)
                        at org.apache.http.conn.EofSensorInputStream.close(EofSensorInputStream.java:213)
                        at download.test.control.DownloadRunnable.doGet(DownloadRunnable.java:347)
                        at download.test.control.DownloadRunnable.run(DownloadRunnable.java:77)
                        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1112)
                        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:587)
                        at java.lang.Thread.run(Thread.java:831)
                        Caused by: android.system.ErrnoException: recvfrom failed: EBADF (Bad file number)
                        at libcore.io.Posix.recvfromBytes(Native Method)
                        at libcore.io.Posix.recvfrom(Posix.java:161)
                        at libcore.io.BlockGuardOs.recvfrom(BlockGuardOs.java:250)
                        at libcore.io.IoBridge.recvfrom(IoBridge.java:579)
                     */
                    is.close();  
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    Log.e(TAG, "", e);
                }
            }
//            
            if(bis != null)
            {
                try
                {
                    bis.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

//            if(entity != null)
//            {
//                try
//                {
//                    entity.consumeContent();
//                }
//                catch (IOException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }

            if(isError)
            {
                info.setStatus(DownloadStatus.DOWNLOAD_FAILED);
                handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
                //info.setInterruptByReason(true, InterruptReason.DEFAULT);
            }
            Log.d(TAG, "task is end");
            //handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
        }        
    };
    
    
    
    /**
     * 生成下载临时文件File对象。
     * */
    private File genTempDownloadFileObj(DownloadFileInfo info)
    {
        File f = new File(DownloadFileInfo.LOCAL_FILE_PATH);
        if(!f.exists())
        {
            Log.d(TAG, "create the dir");
            f.mkdirs();
        }
        
        return new File(info.getTempDownloadFile());
    }
    
    /**
     * 更改下载的临时文件的名称
     * */
    private void renameTempFile2NormalFile(File f)
    {
        if(f != null && f.exists())
        {
            info.setStatus(DownloadStatus.DOWNLOADED);
            info.setInterruptByReason(true, InterruptReason.DOWNLOAD_END);
            File normalFile = new File(info.getLocalFile());
            if(f.renameTo(normalFile))
            {
                Log.d(TAG, "rename succeed, f = " + normalFile.getAbsolutePath());
            }
        }
        else
        {
            Log.d(TAG, "file is not exist");
            info.setStatus(DownloadStatus.DOWNLOAD_FAILED);
            info.setInterruptByReason(true, InterruptReason.PATH_ERROR);
        }
        handler.sendMessage(handler.obtainMessage(info.getStatus(), info));
    }
    
    
    /**
     * 从响应获取下载的文件的长度。
     * */
    @SuppressWarnings({ "deprecation" })
    private long getFileLengthFromResponse(HttpResponse response)
    {
        //获取“Content-Range”名称对应的最后一个Header的值。
        // Content-Range:bytes-unitSPfirst-byte-pos-last-byte-pos/entity-legth
        Header header = response.getLastHeader("Content-Range");
        long fileSize = 0;
        if(header != null)
        {
            String headerValue = header.getValue();
            Log.d(TAG, "content-range = " + headerValue);
            if(headerValue != null && headerValue.startsWith("bytes"))
            {
                int pos = headerValue.lastIndexOf('/');
                if(pos != -1)
                {
                    try
                    {
                        fileSize = Long.parseLong(headerValue.substring(pos + 1));
                    }
                    catch(NumberFormatException e)
                    {
                        e.printStackTrace();
                    }                    
                }
            }
        }
        return fileSize;
    }
}
