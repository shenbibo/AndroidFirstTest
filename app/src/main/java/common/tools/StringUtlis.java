package common.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import download.test.utils.ImageHttpUtils;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月10日 上午11:22:46
 * @version
 * @since
 */
public class StringUtlis
{
    
    public interface Encoding
    {
        String UTF_8 = "UTF-8";
    }

    /**
     * 将HTML格式的字符串解析成UTF-8编码的字符串。<p>
     * 使用URLDecoder.decode(url, Encoding.UTF_8)方法来实现。
     * */
    public static String decode2Utf8(String url)
    {
        if(url == null)
        {
            return null;
        }
        
        try
        {
            return URLDecoder.decode(url, Encoding.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将 String 转换为 application/x-www-form-urlencoded MIME 格式*/
    public static String encode2Url(String url)
    {
        if(url == null)
        {
            return null;
        }
        
        try
        {
            return URLEncoder.encode(url, Encoding.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 判断字符串是不是json字符串。
     * */
    public static boolean isJsonString(String str)
    {
        if(str != null)
        {
            String tempStr = str.trim();
            if(tempStr.startsWith("{") && tempStr.endsWith("}"))
            {
                return true;
            }
        }
        return false;
    }
    
    public static String genMD5HexStr(String url)
    {
        return ImageHttpUtils.genImageFileName(url);
    }
    
    public static String getHexString(int value)
    {
        String hexString = Integer.toHexString(value);
        if(hexString.length() == 1)
        {
            hexString = "0" + hexString;
        }
        return hexString;
    }
    
}
