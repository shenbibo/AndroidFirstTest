package log;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
final class TreeManager implements LogInterface, LogTreeManagerInterface {
    private final CopyOnWriteArrayList<LogTree> TREES = new CopyOnWriteArrayList<>();

    private LogTree logTree;
    LogData logData = new LogData();

    @Override
    public void handleMsg(int priority, String tag, String msg, Throwable tr) {
        //        if (TREES.isEmpty()) {
        //            return;
        //        }

        //long startTime = System.nanoTime();
        // new一个对象大约需要87us，使用set方法大概70us
        // LogData logData = new LogData(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());
        // logData.set(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());
        //long finishTime = System.nanoTime();
        //Log.i("Logger", "create logDataTime = " + (finishTime - startTime) / 1000);

        //long startTime2 = System.nanoTime();
        //                for (LogTree tree : TREES) {
        //                    tree.prepareLog(logData);
        //                }
        // for 循环比较耗时
        for (LogTree tree : TREES) {
            tree.prepareLog(priority, tag, msg, tr);
        }
        //        for(int i = 0; i < TREES.size(); i++){
        //            TREES.get(i).prepareLog(logData);
        //        }
        // logTree.prepareLog(logData);
        //logTree.handleMsg(priority, tag, msg, tr);

        //        for (int i = 0; i < TREES.size(); i++){
        //           TREES.get(i).prepareLog(priority, tag, msg, tr);
        //        }
        //long finishTime2 = System.nanoTime();
        //Log.i("Logger", "loop time = " + (finishTime2 - startTime2) / 1000);

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
        //logTree = logTrees[0];
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
