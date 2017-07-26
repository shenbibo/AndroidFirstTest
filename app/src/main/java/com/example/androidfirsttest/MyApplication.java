package com.example.androidfirsttest;

import download.test.utils.ViewResgiterUtils;
import retrofit.common.HttpUtils;
import retrofit.test.weather.WeatherRequest;

import android.app.Application;

import com.sky.slog.LogcatTree;
import com.sky.slog.Slog;

/**
 * @author sWX284798
 * @date 2015年8月13日 下午8:03:22
 */
public class MyApplication extends Application {
    private static Application instance;

    public MyApplication() {
        super();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {

        super.onCreate();

        Slog.init(new LogcatTree()).simpleMode(true).prefixTag("AF");

        HttpUtils.init(WeatherRequest.BASE_URL);

        ViewResgiterUtils.init();
    }
}
