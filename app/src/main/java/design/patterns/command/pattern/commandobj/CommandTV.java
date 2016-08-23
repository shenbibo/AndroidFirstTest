package design.patterns.command.pattern.commandobj;

import android.util.Log;
import design.patterns.command.pattern.Command;
import design.patterns.command.pattern.CommandPatternTest;
import design.patterns.command.pattern.receiver.Light;
import design.patterns.command.pattern.receiver.TV;

/**
 * TV操作的命令类，此处将命令对象和命令的执行者合并在一起。
 * 当合并在一起的时候，有一个问题，当执行命令的对象是唯一的时候，如客厅中电视只有一台，
 * 为了保证一致性，可能需要建立一个内部类，并且使用static 申明这个类的对象，这样才好保证不同的CommandTV对象操纵的是同一台TV。
 * 
 * @author sWX284798
 * @date 2015年11月4日 上午9:38:39
 * @version
 * @since
 */
public class CommandTV implements Command
{    
    private boolean lightStatus;
    
    private int preStatus = 0;
    
    private String location;
    
    private static int currentStatus = 0;
    
    /**
     * 
     */
    public CommandTV(boolean wantedStatus, String location)
    {
        lightStatus = wantedStatus;
        this.location = location; 
    }
    
    /**
     * @hide
     * */
    
    public void setLightStatus(boolean wantedStatus)
    {
        lightStatus = wantedStatus;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        if(lightStatus && currentStatus <= 0)
        {           
            turnOn();
            preStatus = currentStatus;
            currentStatus = 1;
        }
        else if(!lightStatus && currentStatus > 0)
        {
            turnOff();
            preStatus = currentStatus;
            currentStatus = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo()
    {
        if(preStatus != currentStatus)
        {
            if(preStatus > 0)
            {
                turnOn();
            }
            else
            {
                turnOff();
            }
        }
    }
    
    public void turnOn()
    {
        Log.d(CommandPatternTest.TAG, location + " turn on the TV");
    }
    
    public void turnOff()
    {
        Log.d(CommandPatternTest.TAG, location + " turn off the TV");
    }

}
