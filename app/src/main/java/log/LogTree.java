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

    //    final void prepareLog(int priority, String tag, String msg, Throwable tr) {
    //        if (!isLoggable(priority, tag)) {
    //            return;
    //        }
    //
    //        handleMsg(priority, tag, msg, tr);
    //    }
    //
    //    final void prepareLog(String logPrefix, int priority, String tag, String msg, Throwable tr){
    //        if (!isLoggable(priority, tag)) {
    //            return;
    //        }
    //
    //        handleMsg(logPrefix, priority, tag, msg, tr);
    //    }

    //    final void prepareLog(long time, int priority, String tag, String msg, Throwable tr){
    //        if (!isLoggable(priority, tag)) {
    //            return;
    //        }
    //
    //        handleMsg(time, priority, tag, msg, tr);
    //    }

    final void prepareLog(final LogData logData) {
        if (!isLoggable(logData)) {
            return;
        }

        handleMsg(logData);
    }

    //    protected void handleMsg(int priority, String tag, String msg, Throwable tr) {}
    //
    //    /**
    //     * @param logPrefix 日志前缀
    //     * */
    //    protected void handleMsg(String logPrefix, int priority, String tag, String msg, Throwable tr){}

    //    protected void handleMsg(long time, int priority, String tag, String msg, Throwable tr){}

    protected void handleMsg(final LogData logData) {}

    /**
     * 停止日志打印时调用
     */
    protected void release() {}
}
