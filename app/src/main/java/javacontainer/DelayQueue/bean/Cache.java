package javacontainer.DelayQueue.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import javacontainer.DelayQueue.DelayQueueTest;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月5日 下午7:26:17
 * @version
 * @since
 */
public class Cache<K, V>
{
    private Map<K, V> cacheMap = new ConcurrentHashMap<K, V>();
    
    private DelayQueue<DelayedItem<K>> queue = new DelayQueue<DelayedItem<K>>();
        
    private Thread t;
    
    public Cache()
    {
        t = new Thread(new Runnable()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void run()
            {
                startCacheMonitor();
                Log.d(DelayQueueTest.TAG, "daemon thread is will down");
            }
            
        });        
        t.setDaemon(true);
        t.start();
    }
    
    public Thread getDaemonThread()
    {
        return t;
    }

    /**
     * 当使用的key具有相同的hashCode时
     * */
    public void put(K k, V v, long liveTime)
    {
        V v1 = cacheMap.put(k, v);
        DelayedItem<K> item = new DelayedItem<K>(k, liveTime);
        //当在缓存的Map中放入的数据已经存在时，需要同时刷新缓存和过期队列中的数据。
        if(v1 != null)
        {
            queue.remove(item);
        }
        queue.put(item);
    }
    
    private void startCacheMonitor()
    {
        try
        {
            while(true)
            {
                DelayedItem<K> item = queue.take();
                K key = item.getT();
                Log.d(DelayQueueTest.TAG, "remove the cache: key = " + key + ", v = " + cacheMap.get(key));
                cacheMap.remove(key);
                Thread.sleep(50);
            }
        }
        catch(InterruptedException e)
        {
            Log.d(DelayQueueTest.TAG, "is InterruptedException", e);
        }
    }
}
