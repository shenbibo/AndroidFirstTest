package notification.test;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年10月22日 下午5:12:06
 * @version
 * @since
 */
public class MyNotificationListenerService extends NotificationListenerService
{
    
    private static final String TAG = "MyNotificationListenerS";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn)
    {
        
        //super.onNotificationRemoved(sbn);
        if(sbn != null)
        {
            String packageName = sbn.getPackageName();
            String id = String.valueOf(sbn.getId());
            String tag = sbn.getTag();
            Log.d(TAG, "Removed msg, pkg = " + packageName + ", id = " + id + ", tag = " + tag);
        }
        else
        {
            Log.d(TAG, "Removed msg, sbn = null"); 
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn)
    {
        
        //super.onNotificationPosted(sbn);
        if(sbn != null)
        {
            String packageName = sbn.getPackageName();
            String id = String.valueOf(sbn.getId());
            String tag = sbn.getTag();
            Log.d(TAG, "Posted msg, pkg = " + packageName + ", id = " + id + ", tag = " + tag);
        }
        else
        {
            Log.d(TAG, "Posted msg, sbn = null"); 
        }
    }
    
}
