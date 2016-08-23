/*
 * 文 件 名:  IpcTestService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ipc.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ipc.test.MiStudentManagerStubImpl;
import com.ipc.test.aidl.MiStudent;
import com.ipc.test.interfaces.MiStudentManagerImpl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2016年1月14日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class IpcTestService extends Service
{

    private List<MiStudent> data = new ArrayList<MiStudent>();
    
    //private IBinder binder = new MiStudentManagerStubImpl(data);
    
    //以下为不使用AIDL实现Binder机制，即手写Binder的实现类。
    private IBinder binder = new MiStudentManagerImpl(data);
    
    /** {@inheritDoc} */
     
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return binder;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        if(data.isEmpty())
        {
            Random rand = new Random();
            for(int i = 0; i < 8; i++)
            {
                data.add(new MiStudent("1234" + rand.nextInt(100) , rand.nextInt(100))); 
            }            
        }
    }
    

}
