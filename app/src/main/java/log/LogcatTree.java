package log;

import android.util.Log;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public class LogcatTree extends LogTree {
    /**
     * 构造器
     *
     * @param priority 允许输出的日志优先级
     */
    public LogcatTree(int priority) {
        super(priority);
    }

    @Override
    protected void handleMsg(LogData logData) {
        String tempStr = logData.tr == null ? logData.msg : logData.msg
            + '\n' + LogHelper.getStackTraceString(logData.tr);
        Log.println(logData.priority, logData.tag, tempStr);
    }

    @Override
    protected void handleMsg(int priority, String tag, String msg, Throwable tr) {
        String tempStr = tr == null ? msg : msg
            + '\n' + LogHelper.getStackTraceString(tr);
        Log.println(priority, tag, tempStr);
//        Log.println(priority, tag, msg);
    }
}
