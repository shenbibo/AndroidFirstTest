package download.test.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import download.test.bean.RequestBean;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月23日 下午4:52:19
 * @version
 * @since
 */
public class DownloadHttpUtils extends HttpHelper
{

    /**
     * 次方法返回一个数组大小为0的byte数组。
     * */
    public byte[] doGet(String url, String userAgent, String encoding) throws org.apache.http.client.ClientProtocolException ,java.io.IOException 
    {
        
        HttpClient client = null;
        HttpGet get = null;
        HttpResponse response = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        HttpEntity entity = null;
        InputStream is = null;
        try
        {
            client = HttpUtils.getHttpClient(userAgent);
            get = new HttpGet(url);
            request = get;
            response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                entity = response.getEntity();
                long fileLength = entity.getContentLength();
                Log.d(TAG, "response is ok, contentLength = " + fileLength);
                //保存到外部存储
                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                {
                    
                    if(entity != null)
                    {
                        is = entity.getContent();
                        bis = new BufferedInputStream(is);
                    }
                    int length = 0;
                    byte[] buffer = new byte[4096];
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + "DownloadFile");
                    if(!file.isDirectory())
                    {
                        Log.d(TAG, "create Directory");
                        file.mkdirs();
                    }
                    Log.d(TAG, "url = " + url);
                    String path = file.getAbsolutePath() + File.separator + ImageHttpUtils.genImageFileName(url);
                    Log.d(TAG, path);
                    bos = new BufferedOutputStream(new FileOutputStream(new File(file.getAbsolutePath() 
                            + File.separator + ImageHttpUtils.genImageFileName(url) + ".apk")), 8192);
                    
                    int percent = 0;
                    int count = 0;
                    while((length = bis.read(buffer)) != -1)
                    {
                        bos.write(buffer, 0, length);
                        count += length;
                        int tempPercent = (int)(((double)count) / fileLength * 100);
                        if(tempPercent - percent >= 2)
                        {
                            Log.d(TAG, "downloadPercent = " + tempPercent);   
                            percent = tempPercent;
                        }
                        
                    }
                    bos.flush();                   
                }
                else 
                {
                    Log.d(TAG, "Environment.getExternalStorageState() = " + Environment.getExternalStorageState());
                }

            }
            else
            {
                Log.e(TAG, "return code = " + response.getStatusLine().getStatusCode());
            }
            return new byte[0];
        }
        finally
        {
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
            if(bos != null)
            {
                try
                {
                    bos.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            if(is != null)
            {
                //销毁实体占用的资源。
                is.close();
            }
            if(get != null)
            {
                if(!get.isAborted())
                {
                    get.abort();
                }
            }
        }        
    };
    
}
