package log;


/**
 * 日志树基类，由子类实现具体的日志功能
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public abstract class LogTree {
    protected int priority;

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

        handleMsg(logData);
    }

    final void prepareLog(int priority, String tag, String msg, Throwable tr) {
        if (!isLoggable(priority)) {
            return;
        }

        handleMsg(priority, tag, msg, tr);
    }

    protected void handleMsg(final LogData logData) {}

    protected void handleMsg(int priority, String tag, String msg, Throwable tr) {}

    /**
     * 停止日志打印时调用
     */
    protected void release() {}
}
