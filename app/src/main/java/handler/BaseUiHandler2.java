package handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.sky.slog.Slog;

import java.lang.ref.WeakReference;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/5/18
 * @since 3.16
 */
public final class BaseUiHandler2 extends Handler {
    private WeakReference<Activity> attachedActivity;

    /**
     * 构造器
     * @param activityWeakReference 如果为null，则表示不做任何处理与原生Handler保持一致
     */
    public BaseUiHandler2(WeakReference<Activity> activityWeakReference) {
        super(Looper.getMainLooper());
        if (activityWeakReference == null) {
            Slog.w("the activityWeakReference is null!!!");
        }

        attachedActivity = activityWeakReference;
    }

    /**
     * 发送消息如果依赖的activity已经在销毁状态中，则忽略消息
     */
    public final boolean postEx(final UiRunnable runnable) {
        return post(runnable.setActivity(attachedActivity));
    }

    /**
     * 扩展postDelay
     */
    public final boolean postDelayEx(final UiRunnable runnable, long uptimeMillis) {
        return postDelayed(runnable.setActivity(attachedActivity), uptimeMillis);
    }

    /**
     * 扩展postAtTime
     */
    public final boolean postAtTimeEx(UiRunnable runnable, long uptimeMillis) {
        return postAtTime(runnable.setActivity(attachedActivity), uptimeMillis);
    }


    /**
     * 扩展postAtTime
     */
    public final boolean postAtTimeEx(UiRunnable runnable, Object token, long uptimeMillis) {
        return postAtTime(runnable.setActivity(attachedActivity), token, uptimeMillis);
    }

    /**
     * 扩展postAtFrontQueue
     */
    public final boolean postAtFrontOfQueueEx(UiRunnable runnable) {
        return postAtFrontOfQueue(runnable.setActivity(attachedActivity));
    }

    @Override
    public final void handleMessage(Message msg) {
        if (attachedActivity == null || UiHelper.isActivityActive(attachedActivity.get())) {
            handleMessageEx(msg);
        }
    }

    /**
     * 处理消息
     */
    public void handleMessageEx(Message msg) { }

    public abstract static class UiRunnable implements Runnable {
        private WeakReference<Activity> activityWeakReference;

        UiRunnable setActivity(WeakReference<Activity> activityWeakReference) {
            this.activityWeakReference = activityWeakReference;
            return this;
        }

        @Override
        public final void run() {
            if (activityWeakReference == null || UiHelper.isActivityActive(activityWeakReference.get())) {
                runOnUiThread();
            }
        }

        public abstract void runOnUiThread();
    }
}
