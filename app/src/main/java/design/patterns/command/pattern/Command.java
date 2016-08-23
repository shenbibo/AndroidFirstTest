package design.patterns.command.pattern;

/**
 * 
 * 用于封装命令对象的接口。
 * @author sWX284798
 * @date 2015年11月3日 上午11:10:17
 * @version
 * @since
 */
public interface Command
{

    /**
     * 执行实际动作的方法。
     * */
    public void execute();
    
    /**
     * 撤销上一次操作的方法。
     * */
    public void undo();
}
