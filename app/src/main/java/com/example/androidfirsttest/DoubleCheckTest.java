package com.example.androidfirsttest;

import android.os.SystemClock;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月5日 上午9:47:56
 * @version
 * @since
 */
public class DoubleCheckTest
{
    private static final String TAG = "DoubleCheckTest";
    
    private static final Object LOCK = new Object();
    
    private static boolean isStart = false;
    
    private static final Object dumpyObj = new Object();
    
    private static final int A_VALUE = 256;
    
    private static final int B_VALUE = 512;
    
    private static final int C_VALUE = 1024;
    
    static ObjectHolder[] singletons;  // array of static references
    static Thread[] threads; // array of racing threads
    static int threadCount; // number of threads to create
    static int singletonCount; // number of singletons to create
    
    static volatile int recentSingleton;
    
    static class Singleton
    {
        int a;
        int b;
        int c;
        Object obj;
        public Singleton()
        {
            a = A_VALUE;
            b = B_VALUE;
            c = C_VALUE;
            obj = dumpyObj;
        }
    }
    
    static class ObjectHolder
    {
        public Singleton reference;
    }
    
    static void checkSingletonValue(Singleton s, int index)
    {
        int a = s.a;
        int b = s.b;
        int c = s.c;
        Object o = s.obj;
        if(a != A_VALUE)
        {
            Log.d(TAG, "a != A_VALUE, a = " + a + " index = " + index);
        }
        if(b != B_VALUE)
        {
            Log.d(TAG, "b != B_VALUE, b = " + b + " index = " + index);
        }
        if(c != C_VALUE)
        {
            Log.d(TAG, "c != C_VALUE, c = " + c + " index = " + index);
        }
        if(o != dumpyObj)
        {
            Log.d(TAG, "o != dumpyObj, o = " + o + " dumpyObj = " + dumpyObj + " index = " + index);
        }
    }
    
    static class ThreadTest implements Runnable
    {

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            //while(!isStart)
            {
                
            }

            for(int i = 0; i < singletonCount; i++)
            {
                ObjectHolder o = singletons[i];
                if(o.reference == null)
                {
                    synchronized (o)
                    {
                        if(o.reference == null)
                        {
                            o.reference = new Singleton();
                            recentSingleton = i;
                            //Log.d(TAG, "initial o.reference finished, i = " + i);
                        }
                    }
                }
                else
                {
                    checkSingletonValue(o.reference, i);
                    int j = recentSingleton - 1;
                    if(j > i)
                    {
                        i = j;
                    }
                }
            }
        }
        
    }
    
    
    public static void testDoubleCheck(int threadCounts, int singletonCounts)
    {
        if(threadCounts < 0 || singletonCounts < 0)
        {
            Log.d(TAG, "invalid params, return");
            return;
        }
        threadCount = threadCounts;
        singletonCount = singletonCounts;
        
        threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++)
        {
            threads[i] = new Thread(new ThreadTest());
        }
        
        singletons = new ObjectHolder[singletonCount];
        for (int i = 0; i < singletonCount; i++)
        {
            singletons[i] = new ObjectHolder();
        }
        
        long startTime = System.currentTimeMillis();
        //start threads 
        for (int i = 0; i < threadCount; i++)
        {
            threads[i].start();
        }
//        SystemClock.sleep(100);
//        isStart = true;
        //wait threads finish
        for (int i = 0; i < threadCount; i++)
        {
            try
            {
                threads[i].join();
                Log.d(TAG, "i = " + i + " thread finished");
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        long finishedTime = System.currentTimeMillis();
        Log.d(TAG, "the test is finished, used time = " + (finishedTime - startTime));
    }    
}
