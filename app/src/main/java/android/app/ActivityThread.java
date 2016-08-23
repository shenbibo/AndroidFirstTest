package android.app;

import android.util.AndroidRuntimeException;
import android.util.Log;


/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月21日 上午10:23:59
 * @version
 * @since
 */
final class RemoteServiceException extends AndroidRuntimeException
{
    private static final String TAG = "RemoteServiceException";
    
    public RemoteServiceException(String msg) {
        super(msg);
        Log.d(TAG, "CALLED RemoteServiceException");
    }
}

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月21日 下午1:53:48
 * @version
 * @since
 */
final public class ActivityThread
{

}
