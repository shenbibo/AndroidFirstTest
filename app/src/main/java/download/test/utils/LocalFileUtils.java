package download.test.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.util.Log;

/**
 * 读取本地文件的测试类
 * 
 * @author sWX284798
 * @date 2015年11月23日 下午3:07:21
 * @version
 * @since
 */
public class LocalFileUtils extends HttpHelper
{
    
    public void doGetByHttpURLConnection(String url, String userAgent, String encoding) throws IOException
    {
       URLConnection urlc = null;
       BufferedInputStream bis = null;
       BufferedOutputStream bos = null;
       try
       {
           //1、创建请求对象
           URL uri = new URL(url);
           urlc = (URLConnection) uri.openConnection();
           
           //2、设置输入输出属性
           urlc.setDoInput(true); 
           //urlc.setDoOutput(true); //Get方法的时候不能设为true，设置为true，则HTTP请求使用的方法为POST
                   
           //3、设置连接，获取数据的超时时间
           urlc.setConnectTimeout(10000);
           urlc.setReadTimeout(10000);
           
           
           //4、设置使用的方法和参数属性
           //urlc.setRequestMethod("GET");
           urlc.setRequestProperty("User-Agent", userAgent);
           urlc.setRequestProperty("Charset", encoding);
           //设置返回的数据不以Gzip的方式返回，设置了该属性，此时调用getInputStream()返回原始的数据，
           //注意如果网络返回的数据是经过压缩的，则客户端需要手动解压缩
           //urlc.setRequestProperty("Accept-Encoding", "identity");
           //设置最大空闲连接数的大小
           //urlc.setRequestProperty("http.maxConnections", "10");
           
           
           //5、连接网络
           urlc.connect();
           
           //if(urlc.getResponseCode() == HttpURLConnection.HTTP_OK)
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
               String path = file.getAbsolutePath() + File.separator + ImageHttpUtils.genImageFileName(url);
               Log.d(TAG, path);
               
               bos = new BufferedOutputStream(new FileOutputStream(new File(file.getAbsolutePath() 
                       + File.separator + ImageHttpUtils.genImageFileName(url))), 8192);
               
               while((length = bis.read(buffer)) != -1)
               {
                   bos.write(buffer, 0, length);
               }
               bos.flush(); 
           }
//           else
//           {
//               Log.d(TAG, "return code = " + urlc.getResponseCode());
//           }
 
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
           
//           if(urlc != null)
//           {
//               try
//               {
//                   urlc.disconnect();
//               }
//               catch(Exception e)
//               {
//                   e.printStackTrace();
//               }
//           }
       }

    }

    
}
