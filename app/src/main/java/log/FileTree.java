package log;


/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public class FileTree extends LogTree {
    /**
     * 构造器
     *
     * @param priority 日志输出优先级
     */
    public FileTree(int priority) {
        super(priority);
    }

    @Override
    public void handleMsg(int priority, String tag, String msg, Throwable tr) {
        // 组装日志样式：2018-02-26 16:53:25.123 D/Tag(pid/tid)：msg + '\n' + LogHelper.getStackTraceString(Throwable tr)
    }
}
