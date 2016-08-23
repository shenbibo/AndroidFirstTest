package javacontainer.DelayQueue.bean;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月5日 下午7:29:09
 * @version
 * @since
 */
public class DelayedItem<T> implements Delayed
{
    private T t;
    
    private long liveTime;
    
    private long cacheTime;
    
    /**
     * 
     */
    public DelayedItem(T t, long liveTime)
    {
        if(t == null)
        {
            throw new NullPointerException();
        }
        this.t = t;
        this.liveTime = liveTime;
        cacheTime = TimeUnit.NANOSECONDS.convert(liveTime, TimeUnit.MILLISECONDS) + System.nanoTime(); 
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
        if(another instanceof DelayedItem)
        {
            DelayedItem<?> dt = (DelayedItem<?>) another;
            if(liveTime > dt.liveTime)
            {
                return 1;
            }
            else if(liveTime == dt.liveTime)
            {
                return 0;
            }
            else
            {
                return -1;
            }
                
        }
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDelay(TimeUnit unit)
    {
        
        return unit.convert(cacheTime - System.nanoTime(),  TimeUnit.NANOSECONDS);
    }
    
    public T getT()
    {
        return t;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {        
        return t.hashCode();
    }
    
    /**
     * 若两个对象相同或者取决于t.equals(o)的返回值;
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        //
        if(o instanceof DelayedItem)
        {
            DelayedItem<?> item = (DelayedItem<?>) o;
            if(item == this)
            {
                return true;
            }
            return t.equals(item);
        }
        return false;
    }

}
