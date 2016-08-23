package com.handler.delay.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.BaseActivity;
import com.example.androidfirsttest.R;

/**
 * 发送延时消息，抓取堆栈查看是否会停留在MessageQueue.nativePollOnce()方法中,并且是否会导致主线程阻塞
 * Created by shenbb on 2016/7/20.
 */
public class HandlerDelayTestActivity extends BaseActivity{

    private static final String TAG = "HandlerDelayTestActivi";

    private Button button;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_activity_layout);
        initViews();
        initValues();
        initListeners();
    }

    @Override
    protected void initViews() {
        button = (Button) findViewById(R.id.button);
    }

    @Override
    protected void initValues() {
        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "msg.arg1 = " + msg.arg1);
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void initListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HandlerDelayTestActivity.this, "button is click", Toast.LENGTH_LONG).show();
                new Thread(){
                    @Override
                    public void run() {
                        myHandler.sendMessageAtFrontOfQueue(myHandler.obtainMessage());
                        super.run();
                    }
                }.start();
            }
        });
    }
}
