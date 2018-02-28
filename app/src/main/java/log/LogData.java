package log;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/28
 */
class LogData {
    long time;
    int priority;
    String tag;
    String msg;
    Throwable tr;
    int threadId;

    LogData(long time, int priority, String tag, String msg, Throwable tr, int threadId){
        this.time = time;
        this.priority = priority;
        this.tag = tag;
        this.msg = msg;
        this.tr = tr;
        this.threadId = threadId;
    }
}
