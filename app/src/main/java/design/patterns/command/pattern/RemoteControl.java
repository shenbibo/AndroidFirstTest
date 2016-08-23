package design.patterns.command.pattern;

import design.patterns.command.pattern.commandobj.NullCommand;

/**
 * 
 * 用于发送请求的遥控器类
 * @author sWX284798
 * @date 2015年11月3日 下午3:45:41
 * @version
 * @since
 */
public class RemoteControl
{
    private Command[] commandLeft;
    
    private Command[] commandRight;
    
    /**
     * 用于按回退键记录的命令。
     * */
    private Command undoCommand;
        
    private final static int MAX_COMMAND_COUNTS = 10;
    
    /**
     * 
     */
    public RemoteControl()
    {
        Command c = new NullCommand();
        commandLeft = new Command[MAX_COMMAND_COUNTS];
        commandRight = new Command[MAX_COMMAND_COUNTS];
        for(int i = 0; i < MAX_COMMAND_COUNTS; i++)
        {
            commandLeft[i] = c;
            commandRight[i] = c;
        }
        undoCommand = c;
    }
    
    public void setCommand(int index, Command cl, Command cr)
    {

        checkArgsIsLegal(index);
        if(cl != null)
        {
            commandLeft[index] = cl;
        }
        if(cr != null)
        {
            commandRight[index] = cr;
        }
    }
    
    public void pressLeftButton(int index)
    {
        checkArgsIsLegal(index);
        commandLeft[index].execute();
        undoCommand = commandLeft[index];
    }

    public void pressRightButton(int index)
    {
        checkArgsIsLegal(index);
        commandRight[index].execute();
        undoCommand = commandRight[index];        
    }
    
    public void pressUndoButton()
    {
        undoCommand.undo();
        undoCommand = new NullCommand();
    }
    
    private static void checkArgsIsLegal(int index)
    {
        if(index < 0 || index >= MAX_COMMAND_COUNTS)
        {
            throw new IllegalArgumentException("index must > 0 or < 10, index = " + index);
        }
    }
}
