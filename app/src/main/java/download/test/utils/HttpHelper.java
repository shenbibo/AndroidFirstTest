package download.test.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;

import common.tools.StringUtlis.Encoding;

import android.util.Log;

/**
 * Http帮助类，主要使用get,post方法。
 * 
 * @author sWX284798
 * @date 2015年11月13日 下午3:47:41
 * @version
 * @since
 */
public class HttpHelper
{
    public static final String TAG = "download";

    private String userAgent = "android/google/china";
    
    protected HttpRequestBase request;
    
    /**
     * 使用get方法获取网络返回的数据。
     * @throws IOException 
     * @throws ClientProtocolException 
     * */
    public byte[] doGet(String url, String userAgent, String encoding) throws ClientProtocolException, IOException
    {
        HttpClient client = null;
        HttpGet get = null;
        HttpResponse response = null;
        try
        {
            client = HttpUtils.getHttpClient(userAgent);
            get = new HttpGet(url);
            request = get;
            response = client.execute(get);
            return parseResponseData(response);
        }
        finally
        {
            if(get != null)
            {
                if(!get.isAborted())
                {
                    get.abort();
                }
            }
        }
        //return null;
    }
    
    /**
     * 使用post方法获取数据，使用GZIP方式压缩数据。
     * @throws IOException 
     * @throws ClientProtocolException 
     * */
    public byte[] doPostGzip(String url, String body, String userAgent, String encoding) throws ClientProtocolException, IOException
    {
        HttpClient client = null;
        HttpPost post = null;
        HttpResponse response = null;

        try
        {
            //1、获取请求的HTTP对象
            client = HttpUtils.getHttpClient(userAgent);
            
            //2、构建post请求对象
            post = new HttpPost(url);
            this.request = post;
            
            //3、添加请求头
            addHeader(post);
            
            //4、压缩请求的的数据body，设置到请求的body里面去
            ByteArrayEntity entity = new ByteArrayEntity(GZIPUtils.gzipData(body.getBytes(encoding)));
            if(entity.getContentLength() != 0)
            {
                post.setEntity(entity); 
            }
        
            
            //5、执行网络请求，获取响应.
            response = client.execute(post);
            
            //6、解析响应的数据，转换成byte[]数组输出
            return parseResponseData(response);
        }
        finally
        {
            //7、关闭连接，如果未停止，则打断。
            try
            {
                if(post != null)
                {
                    if(!post.isAborted())
                    {
                        post.abort();
                    }
                }
            }
            catch(Exception e)
            {
                Log.d(TAG, "" + e);
            }
            
        }

        
    }
    
    /**
     * 添加请求头
     * */
    protected void addHeader(HttpRequest request)
    {
        if(request instanceof HttpGet)
        {
            request.addHeader("Content-Type", "text/json");
        }
        else if(request instanceof HttpPost)
        {
            request.addHeader("Content-Type", "text/application-xgizp");
            request.addHeader("Content-Encoding", "gzip");
        }
    }
    
    /**
     * 解析返回的数据变成byte数组。
     * @throws IOException 
     * @throws IllegalStateException 
     * */
    protected byte[] parseResponseData(HttpResponse response) throws IllegalStateException, IOException
    {
       
        BufferedInputStream bis = null;
        HttpEntity entity = null;
        try
        {
            //1、获取响应的实体。
            entity = response.getEntity();
            if(entity == null || response.getStatusLine().getStatusCode() != 200)
            {
                Log.d(TAG, "response.getStatusLine().getStatusCode() = " + 
                        response.getStatusLine().getStatusCode());
                return new byte[0];
            }
                    
            //3、获取实体的编码方式
            String contentType = null;
            Header header = entity.getContentEncoding();
            if(header != null)
            {
                contentType = header.getValue();
            }
            
            //4、根据不同的编码格式，使用不同的的输出流
            if("gzip".equalsIgnoreCase(contentType) || "z".equalsIgnoreCase(contentType))
            {
                bis = new BufferedInputStream(new GZIPInputStream(entity.getContent()));
            }
            else
            {
                bis = new BufferedInputStream(entity.getContent());
            }
            
            //5、从获取的响应流中读取数据到缓存中
            int length = 0;
            byte[] bytes = new byte[2048];
            //2、构建缓存数据对象
            ByteArrayOutputStream  baos = new ByteArrayOutputStream(1024);
            while((length = bis.read(bytes)) != -1)
            {
                baos.write(bytes, 0, length);
            }    
            //baos.close(); 可以不用调用该方法，因为该方法实现为empty.
            Log.d(TAG, "content = " + new String(baos.toByteArray(), Encoding.UTF_8));
            return baos.toByteArray();
        }
        finally
        {
            if(bis != null)
            {
                try
                {
                    bis.close();
                }
                catch(Exception e)
                {
                    Log.e(TAG, "", e);
                }
                
                if(entity != null)
                {
                    entity.consumeContent();
                }
            }
        }

    }
    
    public void abortConnect()
    {
        if(request != null)
        {
            try
            {
                if(!request.isAborted())
                {
                    request.abort();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
