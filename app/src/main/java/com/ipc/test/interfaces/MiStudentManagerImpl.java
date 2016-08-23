/*
 * 文 件 名:  MiStudentManagerImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ipc.test.interfaces;

import java.util.ArrayList;
import java.util.List;

import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;

import com.ipc.test.aidl.MiStudent;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author sWX284798
 * @version [版本号, 2016年1月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MiStudentManagerImpl extends Binder implements IMiStudentManager
{

    private static final String TAG = "MiStudentManagerImpl";
    
    static final java.lang.String DESCRIPTOR = "com.ipc.test.aidl.IMiStudentManager";
    
    static final int TRANSACTION_getMiStudent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addStudent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_remove = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    
    /** Construct the stub at attach it to the interface. */
    public MiStudentManagerImpl()
    {
        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * Cast an IBinder object into an com.ipc.test.aidl.IMiStudentManager
     * interface, generating a proxy if needed.
     */
    public static com.ipc.test.interfaces.IMiStudentManager asInterface(android.os.IBinder obj)
    {
        if ((obj == null))
        {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (((iin != null) && (iin instanceof com.ipc.test.aidl.IMiStudentManager)))
        {
            return ((com.ipc.test.interfaces.IMiStudentManager) iin);
        }
        return new MiStudentManagerImpl.Proxy(obj);
    }

    @Override
    public android.os.IBinder asBinder()
    {
        return this;
    }

    @Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
            throws android.os.RemoteException
    {
        switch (code)
        {
        case INTERFACE_TRANSACTION:
        {
            reply.writeString(DESCRIPTOR);
            return true;
        }
        case TRANSACTION_getMiStudent:
        {
            data.enforceInterface(DESCRIPTOR);
            java.util.List<com.ipc.test.aidl.MiStudent> _result = this.getMiStudent();
            reply.writeNoException();
            reply.writeTypedList(_result);
            return true;
        }
        case TRANSACTION_addStudent:
        {
            data.enforceInterface(DESCRIPTOR);
            com.ipc.test.aidl.MiStudent _arg0;
            if ((0 != data.readInt()))
            {
                _arg0 = com.ipc.test.aidl.MiStudent.CREATOR.createFromParcel(data);
            }
            else
            {
                _arg0 = null;
            }
            this.addStudent(_arg0);
            reply.writeNoException();
            return true;
        }
        case TRANSACTION_remove:
        {
            data.enforceInterface(DESCRIPTOR);
            com.ipc.test.aidl.MiStudent _arg0;
            if ((0 != data.readInt()))
            {
                _arg0 = com.ipc.test.aidl.MiStudent.CREATOR.createFromParcel(data);
            }
            else
            {
                _arg0 = null;
            }
            boolean _result = this.remove(_arg0);
            reply.writeNoException();
            reply.writeInt(((_result) ? (1) : (0)));
            return true;
        }
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements com.ipc.test.interfaces.IMiStudentManager
    {
        private android.os.IBinder mRemote;

        Proxy(android.os.IBinder remote)
        {
            mRemote = remote;
        }

        @Override
        public android.os.IBinder asBinder()
        {
            return mRemote;
        }

        public java.lang.String getInterfaceDescriptor()
        {
            return DESCRIPTOR;
        }

        @Override
        public java.util.List<com.ipc.test.aidl.MiStudent> getMiStudent() throws android.os.RemoteException
        {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            java.util.List<com.ipc.test.aidl.MiStudent> _result;
            try
            {
                _data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getMiStudent, _data, _reply, 0);
                _reply.readException();
                _result = _reply.createTypedArrayList(com.ipc.test.aidl.MiStudent.CREATOR);
            }
            finally
            {
                _reply.recycle();
                _data.recycle();
            }
            return _result;
        }

        @Override
        public void addStudent(com.ipc.test.aidl.MiStudent student) throws android.os.RemoteException
        {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            try
            {
                _data.writeInterfaceToken(DESCRIPTOR);
                if ((student != null))
                {
                    _data.writeInt(1);
                    student.writeToParcel(_data, 0);
                }
                else
                {
                    _data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addStudent, _data, _reply, 0);
                _reply.readException();
            }
            finally
            {
                _reply.recycle();
                _data.recycle();
            }
        }

        @Override
        public boolean remove(com.ipc.test.aidl.MiStudent student) throws android.os.RemoteException
        {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            boolean _result;
            try
            {
                _data.writeInterfaceToken(DESCRIPTOR);
                if ((student != null))
                {
                    _data.writeInt(1);
                    student.writeToParcel(_data, 0);
                }
                else
                {
                    _data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_remove, _data, _reply, 0);
                _reply.readException();
                _result = (0 != _reply.readInt());
            }
            finally
            {
                _reply.recycle();
                _data.recycle();
            }
            return _result;
        }
    }

    public List<MiStudent> studentList = new ArrayList<MiStudent>();
    
    private static final Object LOCK = new Object(); 
    
    /** <默认构造函数>
     */
    public MiStudentManagerImpl(List<MiStudent> studentList)
    {
        // TODO Auto-generated constructor stub
        this();
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

}
