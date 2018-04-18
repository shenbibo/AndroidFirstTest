package log;


import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 日志树基类，由子类实现具体的日志功能
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public abstract class LogTree {
    protected int priority;
    protected AtomicBoolean isReleaseCalled = new AtomicBoolean(false);

    /**
     * 构造器
     */
    public LogTree(int priority) {
        this.priority = priority;
    }

    protected boolean isLoggable(final int priority) {
        return priority >= this.priority;
    }

    final void prepareLog(final LogData logData) {
        if (!isLoggable(logData.priority)) {
            return;
        }

        handleMsgOnSubThread(logData);
    }

    final void prepareLog(int priority, String tag, String msg, Throwable tr) {
        if (!isLoggable(priority)) {
            return;
        }

        handleMsgOnCalledThread(priority, tag, msg, tr);
    }

    /**
     * 该方法将会在子线程中调用，基于性能考虑，除了Logcat的日志外，其他的都是在子线程调用
     * */
    protected void handleMsgOnSubThread(final LogData logData) {}

    /**
     * 在打印日志的线程中，目前仅适合LogcatTree
     * */
    protected void handleMsgOnCalledThread(int priority, String tag, String msg, Throwable tr) {}

    /**
     * 停止日志打印时调用，子类必须调用super.release
     */
    protected void release() {
        isReleaseCalled.set(true);
    }

    protected boolean isReleaseCalled() {
        return isReleaseCalled.get();
    }
}
