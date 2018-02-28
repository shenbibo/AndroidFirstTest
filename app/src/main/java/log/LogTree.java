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

    protected boolean isLoggable(final LogData logData) {
        return logData.priority >= this.priority;
    }

    final void prepareLog(final LogData logData) {
        if (!isLoggable(logData)) {
            return;
        }

        handleMsg(logData);
    }

    protected void handleMsg(final LogData logData) {}

    /**
     * 停止日志打印时调用
     */
    protected void release() {}
}
