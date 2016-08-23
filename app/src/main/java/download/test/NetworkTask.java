package download.test;

import java.io.IOException;
import java.util.concurrent.Executor;

import org.apache.http.client.ClientProtocolException;

import common.tools.StringUtlis;
import common.tools.StringUtlis.Encoding;
import download.test.bean.ErrorCause;
import download.test.bean.RequestBean;
import download.test.bean.RequestBean.HttpMethod;
import download.test.bean.RequestBean.RequestDataType;
import download.test.bean.RequestBean.RequestType;
import download.test.bean.ResponseBean;
import download.test.bean.ResponseCode;
import download.test.utils.DownloadHttpUtils;
import download.test.utils.HttpHelper;
import download.test.utils.ImageHttpUtils;
import download.test.utils.LocalFileUtils;
import download.test.utils.NetworkUtils;
import download.test.utils.ServerMessage;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月9日 下午8:15:11
 * @version
 * @since
 */
public class NetworkTask extends AsyncTask<RequestBean, Void, ResponseBean>
{

    public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
    
    protected RequestBean request;  
    
    
    
    /**
     * 
     */
    public NetworkTask(RequestBean request)
    {
        this.request = request;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseBean doInBackground(RequestBean... params)
    {
        //publishProgress(values);
        callServer();        
        return null;
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void excute(Executor executor)
    {
        executeOnExecutor(executor, request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(ResponseBean result)
    {
        
        super.onPostExecute(result);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute()
    {
        
        super.onPreExecute();
    }
    
    /**
     * 使用post方法发送数据，并获取返回的数据。
     * @throws IOException 
     * @throws ClientProtocolException 
     * */
    private byte[] doPostGZipPool(String url, String body, String encoding, String userAgent) throws ClientProtocolException, IOException
    {
        HttpHelper helper = new HttpHelper();
        helper.doPostGzip(url, body, userAgent, encoding);
        return null;
    }
    
    
    /**
     * 使用get方法获取网络数据
     * @throws IOException 
     * @throws ClientProtocolException 
     * */
    private byte[] doGetPool(String url, String userAgent, String encoding) throws ClientProtocolException, IOException
    {
        HttpHelper helper = new HttpHelper();
        helper.doGet(url, userAgent, encoding);
        return null;
    }
    
    
    private void doImageGet(String url, String userAgent, String encoding) throws ClientProtocolException, IOException
    {
        HttpHelper helper = new ImageHttpUtils();
        helper.doGet(url, userAgent, encoding);
    }
    
    private void doImagePost(String url, String userAgent, String encoding) throws ClientProtocolException, IOException
    {
        HttpHelper helper = new ImageHttpUtils();
        helper.doPostGzip(url, null, userAgent, encoding);
    }
    
    /**
     * 解析返回的json字符串
     * */
    private ResponseBean parseJsonString(String jsonStr, ResponseBean response)
    {
        return null;
    }
    
    /**
     * 请求网络服务器数据。
     * */
    private ResponseBean callServer()
    {
        ResponseBean response = null;        
        try
        {
            //1、首先尝试创建要请求方法对应的response对象，若无法创建则直接返回null
            response = ServerMessage.createResponseBeanObj(request.method_);
            
            if(response == null)
            {
                Log.d(HttpHelper.TAG, "create response obj is null");
                return null;
            }
            
            //2、查看网络是否可用
            if(!NetworkUtils.isNetConnected())
            {
                response.responseCode = ResponseCode.NETWORK_ERROR;
                response.errorCause = ErrorCause.NO_NETWORK;
                Log.d(HttpHelper.TAG, "network is not connected");
            }
            else
            {
                //3、构建请求的URL,url形式可以如下：
                //url=http://www.baidu.test./api/
                //serverName=mapApi
                String url = null;
                if(request.serverName != null)
                {
                    url = request.url + request.serverName;
                }
                else
                {
                    url = request.url;
                }
                
                
                
                //test///////////////////////////////////////
                //url = "http://blog.csdn.net/x844010689/article/details/25642165";
                byte[] resArray = null;
                if(request.requstType == RequestType.TEXT_DATA)
                {
                    //4、生成请求的数据body
                    String body = request.generateRequestBody();
                    
                    //5、像服务器发送请求并接收返回的数据
                    if(HttpMethod.GET.equals(request.httpMethod))
                    {
                        resArray = doGetPool(url, Encoding.UTF_8, USER_AGENT);
                    }
                    else
                    {
                        resArray = doPostGZipPool(url, body, Encoding.UTF_8, USER_AGENT);
                    }
                    
                    
                    //6、判断返回的数据是否json字符串
                    String strRes = null;
                    if(resArray == null /*|| StringUtlis.isJsonString(strRes = new String(resArray))*/)
                    {
                        response.responseCode = ResponseCode.ERROR;
                        response.errorCause = ErrorCause.JSON_ERROR;
                        Log.d(HttpHelper.TAG, "resArray is null");
                    }
                    else   
                    {
                        //7、解析返回的JSON字符串
                        response = parseJsonString(strRes, response);
                        
                        //8、判断是否需要缓存返回的就送数据
                        if(response != null && response.responseCode == ResponseCode.OK 
                                && response.rturnCode_ == ResponseBean.SERVER_RETURN_CODE_OK
                                && request.requestDataType == RequestDataType.REQUEST_CACHE)
                        {
                            //9、本地缓存数据
                            
                        }
                        
                    }
                }
                else if(request.requstType == RequestType.IMAGE_DATA)//请求图片数据，会直接缓存到本地
                {
                    //doImageGet(url, USER_AGENT, Encoding.UTF_8);
                    //doImagePost(url, USER_AGENT, Encoding.UTF_8);
                    new ImageHttpUtils().doGetByHttpURLConnection(url, USER_AGENT, Encoding.UTF_8);
                }
                else if(request.requstType == RequestType.LOCAL_FILE_DATA)
                {
                    new LocalFileUtils().doGetByHttpURLConnection(url, USER_AGENT, Encoding.UTF_8);
                }
                else if(request.requstType == RequestType.FILE_DATA)
                {
                    new DownloadHttpUtils().doGet(url, USER_AGENT, Encoding.UTF_8);
                }
                
            }
                        
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
    
    /**
     * 在调用请求之前调用
     * 
     * */
    protected void onBeforeRequest()
    {
        
    }
    
    /**
     * 在发送网络请求之后调用
     * */
    protected void onAfterRequest()
    {
        
    }
    
    /**
     * 网络返回json数据解析成功后调用
     * */
    protected void onJsonParsed()
    {
        
    }

    /**
     * 返回json解析失败、非法json数据时调用
     * */
    protected void onJsonParseFailed()
    {
        
    }
}
