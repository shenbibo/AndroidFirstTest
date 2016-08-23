package download.test.utils;

import java.net.HttpRetryException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * 
 * @author sWX284798 
 * @version
 * @since
 */
public class HttpUtils 
{
        
    /**默认超时时间*/
    private static final int DEFAULT_SCOKET_TIMEOUT = 10000;
    
    /**主机最大连接数*/
    private static final int DEFAULT_ROUTE_COUNT = 10;
    
    /**连接池最大容量*/
    private static final int DEFAULT_POOL_MAX_COUNT = 20;

    @SuppressWarnings("deprecation")
    private static HttpClient httpClient = null;
    
    @SuppressWarnings("deprecation")
    public static final synchronized HttpClient getHttpClient(String userAgent)
    {
        if(httpClient == null)
        {
            //http参数
            final HttpParams params = new BasicHttpParams();
                        
            //设置连接连接池超时时间
            ConnManagerParams.setTimeout(params, 5000);
            
            //设置连接服务器超时时间
            HttpConnectionParams.setConnectionTimeout(params, DEFAULT_SCOKET_TIMEOUT);
            
            //设置读取数据超时时间
            HttpConnectionParams.setSoTimeout(params, DEFAULT_SCOKET_TIMEOUT);
            
            //设置最大主机连接数
            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(DEFAULT_ROUTE_COUNT));
            
            //设置连接池最大连接数
            ConnManagerParams.setMaxTotalConnections(params, DEFAULT_POOL_MAX_COUNT);
            
            //设置协议版本
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            
            //设置代理名称
            HttpProtocolParams.setUserAgent(params, userAgent);
            
            //设置字符集格式
            //HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            
            //持续握手，发送报文头进行试探，使用试探在安卓上可能导致异常
            /*
            * Android note: Send each request body without first asking the server
            * whether it will be accepted. Asking first slows down the common case
            * and results in "417 expectation failed" errors when a HTTP/1.0 server
            * is behind a proxy. http://b/2471595
            */
            HttpProtocolParams.setUseExpectContinue(params, false);
            
            //设置不延时发送数据
            HttpConnectionParams.setTcpNoDelay(params, true);
            
            //设置缓存大小
            HttpConnectionParams.setSocketBufferSize(params, 8192);
            
            //http,https特性使用不同的端口
            SchemeRegistry scheme = new SchemeRegistry();
            scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            scheme.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            
            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, scheme);
            
            DefaultHttpClient client = new DefaultHttpClient(cm, params);
            
            //设置空闲保持连接的时间60S
            client.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()
            {
                
                @Override
                public long getKeepAliveDuration(HttpResponse arg0, HttpContext arg1)
                {
                    long keepAlive = super.getKeepAliveDuration(arg0, arg1);
                    if(keepAlive == 1L)
                    {
                        keepAlive = 60000L;
                    }
                    return keepAlive;
                }
            });   
  
            //设置重试请求
            client.setHttpRequestRetryHandler(getRetryHandler());
            httpClient = client;
        }
        return httpClient;
    }
    
    @SuppressWarnings("deprecation")
    private static HttpRequestRetryHandler getRetryHandler()
    {
        //重复尝试三次
        return new DefaultHttpRequestRetryHandler(3, true);
    }
}
