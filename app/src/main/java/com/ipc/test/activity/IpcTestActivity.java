/*
 * 文 件 名:  IpcTestActivity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ipc.test.activity;

import java.util.List;

import com.database.test.Student;
import com.example.androidfirsttest.R;
import com.ipc.test.MiStudentManagerStubImpl;
import com.ipc.test.aidl.IMiStudentManager;
import com.ipc.test.aidl.IStudentListSizeChangeListener;
import com.ipc.test.aidl.MiStudent;
import com.ipc.test.service.IpcTestService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.IBinder.DeathRecipient;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 结论：
 * 当跨进程（包括子进程）启动启动service时，onServiceConnected返回的service对象是android.os.BinderProxy对象，
 * 当启动本进程的服务时，返回的就是Service中onBind方法返回的Binder对象。
 * 
 * 
 * @author  sWX284798
 * @version  [版本号, 2016年1月14日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class IpcTestActivity extends AppCompatActivity
{

    public static final String TAG = "IpcTestActivity";
    
    private Button getListBtn;
    
    private Button addBtn;
    
    private Button removeBtn;
    
    //private IMiStudentManager studentManager;
    private com.ipc.test.interfaces.IMiStudentManager studentManager;
    private ServiceConnection conn;
    
    
    /** {@inheritDoc} */
     
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_test_activity_layout);
        initViews();
        bindTargetService();
        
        //注册监听器
//        new Thread(){
//
//            public void run()
//            {
//                // TODO Auto-generated method stub
//                try
//                {
//                    Thread.sleep(3000);
//                }
//                catch (InterruptedException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                if(studentManager != null)
//                {
//                    try
//                    {
//                        studentManager.registerListener(listener);
//                    }
//                    catch (RemoteException e)
//                    {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }
    
    private void initViews()
    {
        getListBtn = (Button) findViewById(R.id.getList);
        
        addBtn = (Button) findViewById(R.id.addStudent);
        
        removeBtn = (Button) findViewById(R.id.removeStudent);
        
        setViewsControlEvents();
    }
    
    private void setViewsControlEvents()
    {
        getListBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if(studentManager != null)
                {
                    try
                    {
                        List<MiStudent> list = studentManager.getMiStudent();
                        for(MiStudent s : list)
                        {
                            Log.d(TAG, "s.name = " + s.name + ", s.age = " + s.age);
                        }
                    }
                    catch (RemoteException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }                
            }
        });
        
        addBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if(studentManager != null)
                {
                    try
                    {
                        MiStudent s = new MiStudent("1989", 12);
                        studentManager.addStudent(s);
                    }
                    catch (RemoteException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } 
            }
        });
        
        removeBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if(studentManager != null)
                {
                    try
                    {
                        studentManager.remove(new MiStudent("1989", 12));
                    }
                    catch (RemoteException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } 
            }
        });
    }
    
    /**
     * 监测service是否死亡，重新连接。
     * */
    private DeathRecipient mDeathRecipient = new DeathRecipient()
    {
        
        @Override
        public void binderDied()
        {
            // TODO Auto-generated method stub
            if(studentManager == null)
            {
                return;
            }
            studentManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            studentManager = null;
            //重新绑定
            Log.d(TAG, "serice is dead , restart now");
            bindTargetService();
            
            //注册监听器
            
        }
    };
    
    private void bindTargetService()
    {
        Intent i = new Intent(getApplicationContext(), IpcTestService.class);
        conn = new ServiceConnection()
        {
            
            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                // TODO Auto-generated method stub
                Log.d(TAG, "onServiceDisconnected");
                //studentManager = null;
            }
            
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                // TODO Auto-generated method stub
                
                //当跨进程（包括子进程）启动启动service时，onServiceConnected返回的service对象是android.os.BinderProxy对象，
                //当启动本进程的服务时，返回的就是Service中onBind方法返回的Binder对象。

                Log.d(TAG, "service.getClass()=" + service.getClass().getName());
                                
                if(service instanceof IMiStudentManager)
                {
                    Log.d(TAG, "service is a IMiStudentManager");

                }

                //studentManager = IMiStudentManager.Stub.asInterface(service);
                
                //使用自定义的Binder实现类，效果与使用AIDL的方式完全一致，
                //如果使用该方法最好将MiStudentManagerImpl类像自动生成的Stub写为抽象的，以便于隐藏实现细节。
                studentManager = com.ipc.test.interfaces.MiStudentManagerImpl.asInterface(service);
                
                try
                {
                    //监听远程服务死亡。
                    service.linkToDeath(mDeathRecipient, 0);
                }
                catch (RemoteException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(service instanceof MiStudentManagerStubImpl)
                {
                    Log.d(TAG, "service instanceof MiStudentManagerStubImpl");
                }
                else
                {
                    Log.e(TAG, "service not instanceof MiStudentManagerStubImpl");
                }
                
            }
        };
        
        bindService(i, conn, Context.BIND_AUTO_CREATE);
    }
    
    /**
     * 使用观察者模式监听学生列表的size变化。
     * */
    private IStudentListSizeChangeListener listener = new IStudentListSizeChangeListener.Stub()
    {
        
        @Override
        public void listSizeChanged(int size) throws RemoteException
        {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "student list size changed, size = " + size, Toast.LENGTH_SHORT).show();
        }
    };
    
    
    /** {@inheritDoc} */
     
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
//        try
//        {
//            studentManager.unregisterListener(listener);
//        }
//        catch (RemoteException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        unbindService(conn);
    }
    
}
