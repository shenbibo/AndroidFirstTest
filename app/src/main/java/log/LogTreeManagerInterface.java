package log;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public interface LogTreeManagerInterface {

    boolean addLogTree(LogTree logTree);

    boolean addLogTrees(LogTree... logTrees);

    boolean removeLogTree(LogTree logTree);

    boolean clearTrees();
}
