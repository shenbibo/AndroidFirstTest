package javacontainer.DelayQueue.bean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import javacontainer.DelayQueue.DelayQueueTest;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月4日 下午5:36:33
 * @version
 * @since
 */
public class Student implements Delayed, Runnable
{

    protected String name;
    
    /**学生考试时间，以纳秒为单位*/
    protected long workTime;
    
    protected int testTime;
    
    protected CountDownLatch counter;
    
    protected boolean isForced = false;
    
    /**
     * 
     */
    public Student(String name, int testTime, CountDownLatch counter)
    {
        this.name = name;
        this.testTime = testTime;
        workTime = TimeUnit.NANOSECONDS.convert(testTime, TimeUnit.MILLISECONDS) + System.nanoTime();
        this.counter = counter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Delayed another)
    {
        if(another == null)
        {
            return 1;
        }
        if(this == another)
        {
            return 0;
        }
        if(another instanceof Student)
        {
            Student s = (Student) another;
            //在一个类的内部定义一个该类的对象时，可以直接访问其私有成员。
            if(s.testTime > testTime)
            {
                return -1;
            }
            else if (s.testTime == testTime)
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
        //return getDelay(TimeUnit.NANOSECONDS) >= another.getDelay(TimeUnit.NANOSECONDS) ? 1 : -1;
        return 1;
    }

    /**
     * {@inheritDoc}<p>
     * <strong>DelayQueue类中API中调用该方法时（如take方法），传递的TimeUnit参数是{@link TimeUnit.NANOSECONDS}</strong>
     */
    @Override
    public long getDelay(TimeUnit unit)
    {
        //若学生考试时间比当前时间小，说明已经过期，已经可以取出删除了。
        return unit.convert(workTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        //return (workTime - System.nanoTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        //如果时间到了还没有结束，则会实行强制交卷模式。
        if(isForced)
        {
            Log.d(DelayQueueTest.TAG, "student name = " + name + ", hope use time = " + testTime + "M, real use time = 120M" );
        }
        else
        {
            Log.d(DelayQueueTest.TAG, "student name = " + name + ", hope use time = " + testTime + "M, real use time = " + testTime + "M");
        }
        counter.countDown();
    }
    
    public boolean getForce()
    {
        return isForced;
    }

    public void setForce(boolean isForce)
    {
        isForced = isForce;       
    }
}
