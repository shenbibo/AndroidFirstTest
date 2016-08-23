package download.test.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.util.Log;

import com.example.androidfirsttest.MyApplication;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月19日 上午10:32:20
 * @version
 * @since
 */
public class ImageHttpUtils extends HttpHelper
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

                Log.d(TAG, "response is ok");
                //保存到外部存储
                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                {
                    entity = response.getEntity();
                    if(entity != null)
                    {
                        is = entity.getContent();
                        bis = new BufferedInputStream(is);
                    }
                    int length = 0;
                    byte[] buffer = new byte[4096];
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + "ImageTestCache");
                    if(!file.isDirectory())
                    {
                        Log.d(TAG, "create Directory");
                        file.mkdirs();
                    }
                    Log.d(TAG, "url = " + url);
                    String path = file.getAbsolutePath() + File.separator + genImageFileName(url);
                    Log.d(TAG, path);
                    bos = new BufferedOutputStream(new FileOutputStream(new File(file.getAbsolutePath() 
                            + File.separator + genImageFileName(url))), 8192);
                    
                    while((length = bis.read(buffer)) != -1)
                    {
                        bos.write(buffer, 0, length);
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
    
    public void doGetByHttpURLConnection(String url, String userAgent, String encoding) throws IOException
    {
       HttpURLConnection urlc = null;
       BufferedInputStream bis = null;
       BufferedOutputStream bos = null;
       try
       {
           //1、创建请求对象
           URL uri = new URL(url);
           urlc = (HttpURLConnection) uri.openConnection();
           
           //2、设置输入输出属性
           urlc.setDoInput(true); 
           //urlc.setDoOutput(true); //Get方法的时候不能设为true，设置为true，则HTTP请求使用的方法为POST
                   
           //3、设置连接，获取数据的超时时间
           urlc.setConnectTimeout(10000);
           urlc.setReadTimeout(10000);
           
           
           //4、设置使用的方法和参数属性
           urlc.setRequestMethod("GET");
           urlc.setRequestProperty("User-Agent", userAgent);
           urlc.setRequestProperty("Charset", encoding);
           //设置返回的数据不以Gzip的方式返回，设置了该属性，此时调用getInputStream()返回原始的数据，
           //注意如果网络返回的数据是经过压缩的，则客户端需要手动解压缩
           //urlc.setRequestProperty("Accept-Encoding", "identity");
           //设置最大空闲连接数的大小
           //urlc.setRequestProperty("http.maxConnections", "10");
           
           
           //5、连接网络
           urlc.connect();
           
           if(urlc.getResponseCode() == HttpURLConnection.HTTP_OK)
           {
               //获取从网络返回数据的输入流
               InputStream is = urlc.getInputStream();
               bis = new BufferedInputStream(is); 

               int length = 0;
               byte[] buffer = new byte[4096];
               File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                       + File.separator + "ImageTestCache");
               if(!file.isDirectory())
               {
                   Log.d(TAG, "create Directory");
                   file.mkdirs();
               }
               //Log.d(TAG, "url = " + url);
               String path = file.getAbsolutePath() + File.separator + genImageFileName(url);
               Log.d(TAG, path);
               
               bos = new BufferedOutputStream(new FileOutputStream(new File(file.getAbsolutePath() 
                       + File.separator + genImageFileName(url))), 8192);
               
               while((length = bis.read(buffer)) != -1)
               {
                   bos.write(buffer, 0, length);
               }
               bos.flush(); 
           }
           else
           {
               Log.d(TAG, "return code = " + urlc.getResponseCode());
           }
 
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
           
           if(urlc != null)
           {
               try
               {
                   urlc.disconnect();
               }
               catch(Exception e)
               {
                   e.printStackTrace();
               }
           }
       }

    }
    

    /**
     * 
     * 当请求的页面对client请求使用的方法有限制时，若使用的方法不对，则会返回405错误。<p>
     * 405错误：用来访问本页面的 HTTP 谓词不被允许（方法不被允许）
     * {@inheritDoc}
     */
    @Override
    public byte[] doPostGzip(String url, String body, String userAgent,
            String encoding) throws ClientProtocolException, IOException
    {
        HttpClient client = null;
        HttpPost post = null;
        HttpResponse response = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        HttpEntity entity = null;
        InputStream is = null;
        try
        {
            client = HttpUtils.getHttpClient(userAgent);
            post = new HttpPost(url);
            request = post;
            response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {

                Log.d(TAG, "response is ok");
                //保存到外部存储
                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                {
                    entity = response.getEntity();
                    if(entity != null)
                    {
                        is = entity.getContent();
                        bis = new BufferedInputStream(is);
                    }
                    int length = 0;
                    byte[] buffer = new byte[4096];
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + "ImageTestCache");
                    if(!file.isDirectory())
                    {
                        Log.d(TAG, "create Directory");
                        file.mkdirs();
                    }
                    Log.d(TAG, "url = " + url);
                    String path = file.getAbsolutePath() + File.separator + genImageFileName(url);
                    Log.d(TAG, path);
                    bos = new BufferedOutputStream(new FileOutputStream(new File(file.getAbsolutePath() 
                            + File.separator + genImageFileName(url))), 8192);
                    
                    while((length = bis.read(buffer)) != -1)
                    {
                        bos.write(buffer, 0, length);
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
            if(post != null)
            {
                if(!post.isAborted())
                {
                    post.abort();
                }
            }
        } 
    }
    
    
    public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
    
    /*
     * 得到iconToken
     */
    public static String genImageFileName(String key)
    {
        String iconToken;
        if(key == null)
            return null;
        try 
        {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            iconToken = bytesToHexString(mDigest.digest());
        } 
        catch (NoSuchAlgorithmException e) 
        {
            iconToken = String.valueOf(key.hashCode());
        }
        return iconToken;
    }
}
