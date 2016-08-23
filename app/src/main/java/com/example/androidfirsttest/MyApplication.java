package com.example.androidfirsttest;

import download.test.utils.ViewResgiterUtils;
import android.app.Application;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月13日 下午8:03:22
 * @version
 * @since
 */
public class MyApplication extends Application
{
    private static Application instance;
    
    public MyApplication()
    {
        super();
        instance = this;
    }

    public static Application getInstance()
    {
        return instance;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate()
    {
        
        super.onCreate();
        
        ViewResgiterUtils.init();
    }
}
