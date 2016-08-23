package com.test;

//import android.app.RemoteServiceException;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月21日 上午10:27:12
 * @version
 * @since
 */
public class MockAPITest
{
    private static final String TAG = "MockAPITest";
    
    public static void testTextUtilClass()
    {
        Log.d(TAG, "call testTextUtilClass()");
        TextUtils.isEmpty("");
    }
    
//    public static void testException()
//    {
//        try
//        {
//            throwException(-1);
//        }
//        catch (RemoteServiceException e)
//        {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        
//    }
//    
//    public static void throwException(int i)throws RemoteServiceException
//    {
//        if(i < 0)
//        {
//            throw new RemoteServiceException("is exception");
//        }
//    }
}
