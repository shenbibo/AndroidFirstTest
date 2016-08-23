/*
 * 文 件 名:  GCtestAtivity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月12日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.gc.test.view;

import java.lang.ref.WeakReference;

import com.example.androidfirsttest.R;
import com.gc.test.GCtest;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 执行system.gc方法并不意味着虚拟机一定会执行gc操作。
 * 结论：
 * 若一个对象只被持有弱引用，则GC时，会被回收掉
 * 若一个对象的成员对象被只有强引用，则GC时，并不影响该对象被回收，只是被持有强引用的那个对象分配的内存不会被回收。
 * 疑问：当在GCtestAtivity启动一个线程，当线程没有执行完前，即使activity已经执行onDestory方法，执行GC操作，
 * 也无法回收activity对象，直到线程执行完，才能回收成功，若将启动线程封装在静态方法中，则不受影响。
 * 原因分析：匿名内部类隐式的持有了其外部对象的引用，随意它才能随意访问其外部类的成员变量和方法。
 * 
 */
public class GCtestAtivity extends AppCompatActivity
{
    
    public GCtest testObject = new GCtest();
    
    private Button testButton;
    
    private Button memberButton;
    
    private TextView textTips;
    
    public static WeakReference<GCtestAtivity> actvity;
    
    public static GCtest finalGCtestObj;
    
    public static Thread thread;
    
    public static void getActivityObjStatus()
    {
        new Thread(){
            
            public void run() 
            {
                try
                {
                    //首先测试弱引用保持activity的对象，是否会导致activity对象不会被回收。
                    Thread.sleep(2000);
                    Log.d(GCtest.TAG, "is ready to perform gc");
                    System.gc();
                    Thread.sleep(1000);
                    if(actvity.get() == null)
                    {
                        Log.d(GCtest.TAG, "activity object is gced");
                    }
                    else
                    {
                        Log.d(GCtest.TAG, "activity object is not gced");
                    }
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };
        }.start();
    }
    
    public static void getActivityMemberObjStatu()
    {
        new Thread(){
            
            public void run() {
                try
                {
                    //首先测试弱引用保持activity的对象，是否会导致activity对象不会被回收。
                    Thread.sleep(2000);
                    Log.d(GCtest.TAG, "is ready to perform gc");
                    System.gc();
                    Thread.sleep(1000);
                    if(finalGCtestObj != null)
                    {
                        if(actvity.get() == null)
                        {
                            Log.d(GCtest.TAG, "finalGCtestObj is not null, but activity object is gced");
                        }
                        else
                        {
                            Log.d(GCtest.TAG, "finalGCtestObj is not null and activity object is not gced");
                        }
                    }
                    else
                    {
                        if(actvity.get() == null)
                        {
                            Log.d(GCtest.TAG, "finalGCtestObj is null, but activity object is gced");
                        }
                        else
                        {
                            Log.d(GCtest.TAG, "finalGCtestObj is null and activity object is not gced");
                        }
                    }

                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            };
        }.start();
    }

    /** {@inheritDoc} */
     
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gc_test_activity_layout);
        new Thread(){
            
            public void run() {
                SystemClock.sleep(10000);                
            }
        }.start();
        actvity = new WeakReference<GCtestAtivity>(this);
        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                //测试是否可以gc掉对象
                //actvity = new WeakReference<GCtestAtivity>(GCtestAtivity.this);
                //GCtest.main(null);                

                getActivityObjStatus();
                GCtestAtivity.this.finish();
            }
        });
        
        memberButton = (Button) findViewById(R.id.memberTest);
        memberButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                //actvity = new WeakReference<GCtestAtivity>(GCtestAtivity.this);
                finalGCtestObj = testObject;
                //getActivityMemberObjStatu();
                //注意使用以下被注释掉的代码并不会导致activity对象马上被回收掉。
//                new Thread(){
//                    
//                    public void run() {
//                        try
//                        {
//                            //首先测试弱引用保持activity的对象，是否会导致activity对象不会被回收。
//                            Thread.sleep(10000);
//                        }
//                        catch (InterruptedException e)
//                        {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        
//                    }
//                }.start();

                GCtestAtivity.this.finish();
            }
        });
        
        textTips = (TextView) findViewById(R.id.testDescription);
        
        textTips.setText("点击ObjectTest按钮，测试弱引用持有activity对象时，finish掉Activity后，并执行Gc，查看activity对象是否已回收"
                        + "\n点击MemberTest按钮， 测试强引用持有activity对象的GCtest成员对象，finish掉Activity后，并执行Gc，查看activity对象是否已回收" );
    }
    
    /** {@inheritDoc} */
     
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(GCtest.TAG, "onDestory is called");
    }
}
