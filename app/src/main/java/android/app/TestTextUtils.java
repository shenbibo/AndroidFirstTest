package android.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月21日 上午11:00:26
 * @version
 * @since
 */
public class TestTextUtils
{
    private static final String TAG = "TestTextUtils";
    
    public static void callTextUtils()
    {
        Log.d(TAG, "CALL callTextUtils");
        TextUtils.isEmpty("123456");     
        //throw new RemoteServiceException("test this error");
    }
    
    public static RemoteServiceException getExceptionObj()
    {
        try
        {
            Constructor<RemoteServiceException> con = RemoteServiceException.class.getConstructor(String.class);
            RemoteServiceException rse = con.newInstance("test this error");
            return rse;
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
