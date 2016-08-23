/*
 * 文 件 名:  MiStudentManagerStubImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ipc.test;

import java.util.ArrayList;
import java.util.List;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.ipc.test.aidl.IMiStudentManager.Stub;
import com.ipc.test.aidl.IStudentListSizeChangeListener;
import com.ipc.test.aidl.MiStudent;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2016年1月14日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MiStudentManagerStubImpl extends Stub
{
    private static final String TAG = "MiStudentManagerImpl";

    public List<MiStudent> studentList = new ArrayList<MiStudent>();
    
    private static final Object LOCK = new Object(); 
    
    private RemoteCallbackList<IStudentListSizeChangeListener> listeners = 
            new RemoteCallbackList<IStudentListSizeChangeListener>();
    
    /** <默认构造函数>
     */
    public MiStudentManagerStubImpl(List<MiStudent> studentList)
    {
        // TODO Auto-generated constructor stub
        super();
        if(studentList != null)
        {
            this.studentList = studentList;
        }
        
    }
    
    /** {@inheritDoc} */
     
    @Override
    public List<MiStudent> getMiStudent() throws RemoteException
    {
        // TODO Auto-generated method stub
        synchronized (LOCK)
        {
            return studentList;
        }
        
    }

    /** {@inheritDoc} */
     
    @Override
    public void addStudent(MiStudent student) throws RemoteException
    {
        // TODO Auto-generated method stub
        synchronized (LOCK)
        {
            if(!studentList.contains(student))
            {
                if(studentList.add(student))
                {
                    Log.d(TAG, "add item success");
                    notifyListSizeChanged();
                }
            }
        }

    }

    /** {@inheritDoc} */
     
    @Override
    public boolean remove(MiStudent student) throws RemoteException
    {
        // TODO Auto-generated method stub
        synchronized (LOCK)
        {
            if(studentList.contains(student))
            {
                if(studentList.remove(student))
                {
                    Log.d(TAG, "remove item succeed");
                    notifyListSizeChanged();
                    return true;
                }
                else
                {
                    Log.d(TAG, "have the item but remove item fail");
                }
            }
            Log.d(TAG, "list does not have the taget item");
            return false;
        }
    }

    /** {@inheritDoc} */
     
    @Override
    public void registerListener(IStudentListSizeChangeListener listener) throws RemoteException
    {
        // TODO Auto-generated method stub
        synchronized (LOCK)
        {
            listeners.register(listener);
        }
        
    }

    /** {@inheritDoc} */
     
    @Override
    public void unregisterListener(IStudentListSizeChangeListener listener) throws RemoteException
    {
        // TODO Auto-generated method stub
        synchronized (LOCK)
        {
            listeners.unregister(listener);
        }
        
    }
    
    private void notifyListSizeChanged()
    {
        
        synchronized (LOCK)
        {
            final int N = listeners.beginBroadcast();
            for(int i = 0; i < N; i++)
            {
                try
                {
                    listeners.getBroadcastItem(i).listSizeChanged(studentList.size());
                }
                catch (RemoteException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            listeners.finishBroadcast();
        }
    }

}
