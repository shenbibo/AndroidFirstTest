package handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import com.sky.slog.Slog;

import java.lang.ref.WeakReference;


/**
 * 封装Handler类，免去对主线程的Activity的状态的判断
 * 仅适用于UI线程
 *
 * @author sky on 2018/5/17
 * @since 3.16
 */
public class BaseUiHandler extends Handler {
    private WeakReference<Activity> attachedActivity;
    //private Map<Integer, WeakReference<ProxyRunnable>> proxyRunnables = new ConcurrentHashMap<>();
    private SparseArray<WeakReference<ProxyRunnable>> proxyRunnables = new SparseArray<>();


    /**
     * 构造器
     */
    public BaseUiHandler(WeakReference<Activity> activityWeakReference) {
        super(Looper.getMainLooper());
        if (activityWeakReference == null) {
            Slog.w("the activityWeakReference is null!!!");
        }

        attachedActivity = activityWeakReference;
    }

    /**
     * 发送消息如果依赖的activity已经在销毁状态中，则忽略消息
     */
    public final boolean postEx(final Runnable runnable) {
        if (attachedActivity == null) {
            return post(runnable);
        }


        return post(createProxyRunnableIfNotExist(runnable));
    }

    /**
     * 扩展postDelay
     */
    public final boolean postDelayEx(final Runnable runnable, long uptimeMillis) {
        if (attachedActivity == null) {
            return postDelayed(runnable, uptimeMillis);
        }

        return postDelayed(createProxyRunnableIfNotExist(runnable), uptimeMillis);
    }

    /**
     * 扩展postAtTime
     */
    public final boolean postAtTimeEx(Runnable runnable, long uptimeMillis) {
        if (attachedActivity == null) {
            return postAtTime(runnable, uptimeMillis);
        }

        return postAtTime(createProxyRunnableIfNotExist(runnable), uptimeMillis);
    }


    //    /**
    //     * 扩展postAtTime
    //     */
    //    public final boolean postAtTimeEx(Runnable runnable, Object token, long uptimeMillis) {
    //        if (attachedActivity == null) {
    //            return postAtTime(runnable, token, uptimeMillis);
    //        }
    //
    //        return postAtTime(createProxyRunnableIfNotExist(runnable), token, uptimeMillis);
    //    }

    /**
     * 扩展postAtFrontQueue
     */
    public final boolean postAtFrontOfQueueEx(Runnable runnable) {
        if (attachedActivity == null) {
            return postAtFrontOfQueue(runnable);
        }

        return postAtFrontOfQueue(createProxyRunnableIfNotExist(runnable));
    }

    /**
     * Remove any pending posts of Runnable r that are in the message queue.
     */
    public final void removeCallbacksEx(Runnable runnable) {
        if (attachedActivity == null) {
            removeCallbacks(runnable);
        }

        removeCallbacks(getRunnableAndRemove(runnable));
    }

    private Runnable getRunnableAndRemove(Runnable runnable) {
        Runnable realRunnable = runnable;
        int targetHashCode = runnable.hashCode();
        synchronized (this) {
            WeakReference<ProxyRunnable> weakReference = proxyRunnables.get(targetHashCode);
            if (weakReference != null) {
                ProxyRunnable proxyRunnable = weakReference.get();
                if (proxyRunnable != null) {
                    realRunnable = proxyRunnable;
                }
                proxyRunnables.remove(targetHashCode);
            }
        }
        return realRunnable;
    }

    //    /**
    //     * Remove any pending posts of Runnable <var>r</var> with Object
    //     * <var>token</var> that are in the message queue.  If <var>token</var> is null,
    //     * all callbacks will be removed.
    //     */
    //    public final void removeCallbacksEx(Runnable runnable, Object token) {
    //    }

    /**
     * 移除所有的消息与回调，注意该类不对同一个Runnable 不同的Token进行支持
     */
    public final void removeCallbacksAndMessagesEx(Object token) {
        if (token == null) {
            synchronized (this) {
                proxyRunnables.clear();
            }
        }

        removeCallbacksAndMessages(token);
    }

    @Override
    public final void handleMessage(Message msg) {
        if (UiHelper.isActivityActive(attachedActivity.get())) {
            handleMessageEx(msg);
        }
    }

    /**
     * 处理消息
     */
    public void handleMessageEx(Message msg) {
    }

    /**
     * 判断runnable是否在保存的队列之中（通过hashcode进行判断），
     * 如果在，使用原来ProxyRunnable，否则创建一个新的，并且放到引用队列
     */
    private Runnable createProxyRunnableIfNotExist(final Runnable runnable) {
        final int hashCode = runnable.hashCode();
        synchronized (this) {
            WeakReference<ProxyRunnable> weakReferenceProxyRunnable = proxyRunnables.get(hashCode);
            ProxyRunnable proxyRunnable =
                weakReferenceProxyRunnable != null ? weakReferenceProxyRunnable.get() : null;

            if (proxyRunnable == null) {
                proxyRunnable = new ProxyRunnable(runnable, hashCode);
                proxyRunnables.put(hashCode, new WeakReference<>(proxyRunnable));
            } else {
                proxyRunnable.incrementTaskCounts();
            }

            return proxyRunnable;
        }
    }

    private class ProxyRunnable implements Runnable {
        private final Runnable realTask;
        private final int taskHashCode;
        //private AtomicInteger taskCounts = new AtomicInteger(1);
        private int taskCounts = 1;


        ProxyRunnable(Runnable task, int taskHashCode) {
            realTask = task;
            this.taskHashCode = taskHashCode;
        }

        void incrementTaskCounts() {
            //taskCounts.incrementAndGet();
            ++taskCounts;
        }

        //        void decrementTaskCounts() {
        //            ///taskCounts.decrementAndGet();
        //        }

        boolean isTaskCountsZeroAfterDecrement() {
            return (--taskCounts) < 1;
        }

        @Override
        public void run() {
            synchronized (BaseUiHandler.this) {
                if (isTaskCountsZeroAfterDecrement()) {
                    proxyRunnables.remove(taskHashCode);
                }
            }

            if (UiHelper.isActivityActive(attachedActivity.get())) {
                realTask.run();
            }
        }
    }
}
