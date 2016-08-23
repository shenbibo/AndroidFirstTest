package download.test.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import other.HttpResponse;
import junit.framework.Assert;
import android.util.Log;

/**
 * 压缩/解压数据，注意当数据很小，只有几个字节或者几十个字节时，压缩操作之后的大小会变大。
 * 
 * @author sWX284798
 * @date 
 * @version
 * @since
 */
public class GZIPUtils
{

    private static final String TAG = "GZIPUtils";
    
    
    public static byte[] gzipData(byte[] data) 
    {
        if(data == null)
        {
            return null;
        }
        
        byte[] gzipData = null;
        ByteArrayOutputStream bos = null;
        OutputStream gzipOts = null;
        try
        {
            bos = new ByteArrayOutputStream();
            gzipOts = new DataOutputStream(new GZIPOutputStream(bos, data.length));
            gzipOts.write(data, 0, data.length); 
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        finally
        {
            if(gzipOts != null)
            {
                try
                {
                    gzipOts.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
       
        if(bos != null)
        {
            gzipData = bos.toByteArray();
        }
        Log.d(TAG, "gzipData.length = " + gzipData.length);
        return gzipData;
    }
    
    public static byte[] ungzipData(InputStream in)
    {
        if(in == null)
        {
            return null;
        }
        byte[] resultData = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
        try
        {
            bis = new BufferedInputStream(new GZIPInputStream(in));
            byte[] tempData = new byte[128];
            int readLegth = -1;
            while((readLegth = bis.read(tempData)) != -1)
            {
                bos.write(tempData, 0, readLegth);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if(bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        if(bos != null)
        {
            resultData = bos.toByteArray();
        }
        Log.d(TAG, "resultData.length = " + resultData.length);
        return resultData;
    }
    
    public static byte[] ungzipData(byte[] data)
    {
        return ungzipData(new ByteArrayInputStream(data));
    }
    
    public static void Test()
    {
        String[] testStr = {"aktgwpiikwlektlklw三个点跟谁跟谁看akldsjgglal902181-94=-21-=4-1==49-1029490了最拉风了骄傲就发生过送健康更快乐撒精度高沙龙上万人破武器88610-10-0弹框萨拉三字经干",
                "123456",
                "  ",
                "\\\\\\",
                "\n",
                "\"",
                "nidaye"};
        for(int i = 0; i < testStr.length; i++)
        {
            Log.d(TAG, testStr[i]);
            String str = new String(ungzipData(gzipData(testStr[i].getBytes())));
            Log.d(TAG, str);
            Assert.assertEquals(testStr[i], str);
            if(str.equals(testStr[i]))
            {
                Log.d(TAG, "they are equals");
            }
        }
        try
        {
            HttpResponse.Test();
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.d(TAG, "", e);
        }
        
        
    }
}
