package com.example.androidfirsttest;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * @author sWX284798
 *
 */
public class TestService extends Service
{

    /**
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void onCreate()
    {
        stopSelf();
        Log.d("TestService", "is performde ");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("TestService", "is performde111111111111111 ");
        return super.onStartCommand(intent, flags, startId);
        
    }

}
