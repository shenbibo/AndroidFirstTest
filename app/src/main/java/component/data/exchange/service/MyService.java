package component.data.exchange.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月17日 下午8:27:52
 * @version
 * @since
 */
public class MyService extends Service
{

    private static final String TAG = "MyService";
    
    private MyBinder binder = new MyBinder();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        
        return binder;
    }
    
    public class MyBinder extends Binder
    {
        public MyService getService()
        {
            return MyService.this;
        }
    }

    public void setValue(String value)
    {
        Log.d(TAG, "value = " + value);
    }
    
}
