/*
 * 文 件 名:  IMiStudentManager.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ipc.test.interfaces;

import android.os.IInterface;

/**
 * 尝试不使用AIDL实现Binder机制的功能。
 * 
 */
public interface IMiStudentManager extends IInterface
{


    
    public java.util.List<com.ipc.test.aidl.MiStudent> getMiStudent() throws android.os.RemoteException;
    public void addStudent(com.ipc.test.aidl.MiStudent student) throws android.os.RemoteException;
    public boolean remove(com.ipc.test.aidl.MiStudent student) throws android.os.RemoteException;
    
}
