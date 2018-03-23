package log;


import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
final class TreeManager implements LogInterface, LogTreeManagerInterface {
    private final CopyOnWriteArrayList<LogTree> TREES = new CopyOnWriteArrayList<>();

    AtomicBoolean isClearCalled = new AtomicBoolean(false);

    //private LogTree logcatTree;
    //LogData logData = new LogData();

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
        // logcatTree.prepareLog(logData);
        //logcatTree.handleMsg(priority, tag, msg, tr);

        //        for (int i = 0; i < TREES.size(); i++){
        //           TREES.get(i).prepareLog(priority, tag, msg, tr);
        //        }
        //long finishTime2 = System.nanoTime();
        //Log.i("Logger", "loop time = " + (finishTime2 - startTime2) / 1000);

    }

    @Override
    public boolean addLogTree(LogTree logTree) {
        return !isClearCalled.get() && TREES.add(logTree);

    }

    @Override
    public boolean addLogTrees(LogTree... logTrees) {
        return !isClearCalled.get() && TREES.addAll(Arrays.asList(logTrees));

    }

    @Override
    public boolean removeLogTree(LogTree logTree) {
        if (isClearCalled.get()) {
            return false;
        }

        logTree.release();
        return TREES.remove(logTree);
    }

    @Override
    public boolean clear() {
        if (isClearCalled.get()) {
            return false;
        }
        isClearCalled.set(true);

        release();
        TREES.set(0, null);
        // 删除除了第一个元素外的所有元素
        boolean removed = TREES.removeAll(TREES.subList(1, TREES.size() - 1));

        isClearCalled.set(false);
        return removed;
    }

    @Override
    public void release() {
        for (LogTree logTree : TREES) {
            if (logTree != null) {
                logTree.release();
            }
        }
    }
}
