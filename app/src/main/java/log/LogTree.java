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

    /**
     * 使用封装的LogData
     */
    public void handleMsg(final LogData logData) {
        if (!isLoggable(logData.priority)) {
            onMsg(logData);
        }
    }

    /**
     * 该方法将会在子线程中调用，基于性能考虑，除了Logcat的日志外，其他的都是在子线程调用
     */
    protected void onMsg(final LogData logData) {}

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
