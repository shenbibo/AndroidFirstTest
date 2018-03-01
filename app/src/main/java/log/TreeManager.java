package log;


import android.os.Process;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
final class TreeManager implements LogInterface, LogTreeManagerInterface {
    private final CopyOnWriteArrayList<LogTree> TREES = new CopyOnWriteArrayList<>();

    @Override
    public void handleMsg(int priority, String tag, String msg, Throwable tr) {
        if (TREES.isEmpty()) {
            return;
        }

        LogData logData = new LogData(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());
        for (LogTree tree : TREES) {
            tree.prepareLog(logData);
        }
    }

    @Override
    public void addLogTree(LogTree logTree) {
        TREES.add(logTree);
    }

    @Override
    public void addLogTrees(LogTree... logTrees) {
        for (LogTree logTree : logTrees) {
            addLogTree(logTree);
        }
    }

    @Override
    public void removeLogTree(LogTree logTree) {
        if (logTree == null) {
            return;
        }

        logTree.release();
        TREES.remove(logTree);
    }

    @Override
    public void clear() {
        release();
        TREES.clear();
    }

    @Override
    public void release() {
        for (LogTree logTree : TREES) {
            logTree.release();
        }
    }
}
