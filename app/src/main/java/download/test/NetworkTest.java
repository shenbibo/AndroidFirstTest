package download.test;

import java.io.File;

import android.os.Environment;
import android.util.Log;
import download.test.bean.DownloadRequestBean;
import download.test.bean.ImageRequestBean;
import download.test.bean.LocalFileRequestBean;
import download.test.bean.RequestBean;
import download.test.bean.RequestBean.HttpMethod;
import download.test.bean.RequestBean.RequestType;
import download.test.bean.ResponseBean;
import download.test.bean.TextRequestBean;
import download.test.utils.HttpHelper;
import download.test.utils.ServerAgent;
import download.test.utils.ServerMessage;
import download.test.utils.TestDataUtils;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月19日 下午2:16:05
 * @version
 * @since
 */
public class NetworkTest
{
    
    static 
    {
        ServerMessage.registerResponseBean(ImageRequestBean.METHOD, ResponseBean.class);
        ServerMessage.registerResponseBean(TextRequestBean.METHOD, ResponseBean.class);
        ServerMessage.registerResponseBean(LocalFileRequestBean.METHOD, ResponseBean.class);
        ServerMessage.registerResponseBean(DownloadRequestBean.METHOD, ResponseBean.class);
    }
    

    /**
     * 使用Get方法获取网络图片。
     * */
    public static void ImageGetTest()
    {
        for (String iconUrl : TestDataUtils.TEST_ICON_URL)
        {
            ServerAgent.invokeServer(new ImageRequestBean(iconUrl));
        }
        

    }
    
    
    /**
     * 使用Get方法获取网页数据。
     * */
    public static void textGetTest()
    {
        RequestBean bean = new TextRequestBean("http://blog.csdn.net/jaycee110905/article/details/21130557");
        ServerAgent.invokeServer(bean);
    }
    
    /**
     * 使用post方法获取网络数据
     * */
    public static void textPostTest()
    {
        RequestBean bean = new TextRequestBean("http://blog.csdn.net/jaycee110905/article/details/21130557");
        bean.httpMethod = HttpMethod.POST;
        ServerAgent.invokeServer(bean);
    }
    
    /**
     * 使用http方法获取文件
     * */
    public static void getLocalFileTest()
    {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d(HttpHelper.TAG, "Environment.getExternalStorageDirectory() = " + 
                Environment.getExternalStorageDirectory().getAbsolutePath());
        RequestBean bean = new LocalFileRequestBean("file:" + rootPath + File.separator + "test.txt");
        bean.requstType = RequestType.LOCAL_FILE_DATA;
        ServerAgent.invokeServer(bean);
    }
    
    
    /**
     * 下载文件测试
     * */
    public static void downloadFileTest()
    {
        String url1 = "http://122.11.38.214/dl/appdl/application/apk/ce/cedb03e166234465b1fad0a12158683b/com.sangdh.1511161007.apk";
        String url = "http://122.11.38.214/dl/appdl/application/apk/ce/cedb03e166234465b1fad0a12158683b/com.sangdh.1511161007.apk?sign=c9cr1011cr11010020000000@C54786D76D0DCD4CE6D538B6D2C7656D&cno=4010001&source=autoList&listId=2&position=25&hcrId=A82AF2C0A7E04867A4889994E64F751D&extendStr=3b79700d096d0e7baabaec9879a6a118%3BserviceType%3A0%3Bisshake%3A0&encryptType=1";
        String url2 = "http://122.11.38.214/dl/appdl/application/apk/59/59d650d1e1064453897802048a60d366/com.achievo.vipshop.1511171434.apk?sign=a3d81011ct11010720000000@628931D9E71F80D7951715554A78B482&cno=4010001&source=HiAd&subsource=10685&listId=2&position=1&hcrId=4201379F4D884A6C956EDAFA9560BB7B&extendStr=a7537156cbc5b8d3785ed54e0ffac3b3%3BcdrInfo%3A201512011133251551969%5E3%5E16132%5E9.0%5E20637%5E10218%5E10685%5ECPA%5E2a74fad8d97011e292cf101b543e3aa5%5E20358%5E17263%5E1440%5E2015-12-01+11%3A33%3A25%5E9.0%5E30%5E1009136%5E%5E1%5EOL%3A13%7ESL%3A%7EPT%3A%7ET%3A%7EPL%3AA%3B%3BserviceType%3A0%3Bisshake%3A0&encryptType=1";
        RequestBean bean = new DownloadRequestBean(url2);
        bean.requstType = RequestType.FILE_DATA;   
        ServerAgent.invokeServer(bean);
    }
}
