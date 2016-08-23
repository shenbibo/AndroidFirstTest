/*
 * 文 件 名:  GCtest.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月12日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.gc.test;

import java.lang.ref.WeakReference;

import android.util.Log;

/**
 * 
 */
public class GCtest
{

    public static final String TAG = "GCtest";
    
    public Student s = new Student("thirdName", "321");
    
    public static class Student
    {
        public String name;
        public String age;
        
        public Object obj = new Object();
        
        public Student(String name, String age)
        {
            this.name = name;
            this.age = age;
        }
                
    }
    
    public static Student getObject()
    {
        Student s = new Student("firstName", "123");
        return s;
    }
    
    /**
     * 测试仅使用弱引用保持对象，调用gc方法是否会回收对象
     * */
    public static void testObjectGc()
    {
        //如果使用如下方式获取对象，则不会被GC掉
        //Student ss = getObject();
        WeakReference<Student> s = new WeakReference<GCtest.Student>(getObject());   
        //System.gc();
//        try
//        {
//            Thread.sleep(3000);
//        }
//        catch (InterruptedException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        Log.d(TAG, "is ready to perform gc");
        System.gc();
        if(s.get() == null)
        {
            Log.d(TAG, "s is hava gc-ed");
        }
        else
        {
            Log.d(TAG, "s is not gc-ed");
        }
    }
    
    
    /**
     * 测试使用一个强引用保持Student s 的一个成员，调用gc，看该对象是否会被回收。
     * */
    public static void testMemberGc()
    {
        
        WeakReference<GCtest> s = new WeakReference<GCtest>(new GCtest());  
        Student obj = s.get().s;
        Log.d(TAG, "Student obj = " + obj );
        WeakReference<Student> sss = new WeakReference<GCtest.Student>(obj);
        Log.d(TAG, "is ready to perform gc");
        System.gc();
        if(s.get() == null)
        {
            Log.d(TAG, "testMemberGc:s is hava gc-ed");
            //System.out.println("testMemberGc:name=" + obj );
        }
        else
        {
            Log.d(TAG, "testMemberGc:s is not gc-ed");
            //System.out.println("testMemberGc:name=" + obj );
        }
        
        if(sss.get() == null)
        {
            Log.d(TAG, "sss is hava gc-ed");
        }
        else
        {
            Log.d(TAG, "sss is not gc-ed");
        }
    }
    
    
    public static void main(String[] args)
    {
        
        //testObjectGc();  //打印结果s is hava gc-ed，调用gc方法，会导致未被使用的和仅有弱引用的对象被回收。
        //结论 即使
        testMemberGc();  
        //打印结果Student obj = test.GCtest$Student@914f6a 
        //testMemberGc:s is hava gc-ed
        //
    }
    
}
