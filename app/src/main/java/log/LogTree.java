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

    protected boolean isLoggable(int priority, String tag) {
        return priority >= this.priority;
    }

    final void prepareLog(int priority, String tag, String msg, Throwable tr) {
        if (!isLoggable(priority, tag)) {
            return;
        }

        handleMsg(priority, tag, msg, tr);
    }

    final void prepareLogWithTime(String time, int priority, String tag, String msg, Throwable tr){
        if (!isLoggable(priority, tag)) {
            return;
        }

        handleMsgWithTime(time, priority, tag, msg, tr);
    }

    //    protected String handleThrowable(Throwable tr) {
    //        return LogHelper.getStackTraceString(tr);
    //    }

    public void handleMsg(int priority, String tag, String msg, Throwable tr) {}

    public void handleMsgWithTime(String time, int priority, String tag, String msg, Throwable tr){}
}
