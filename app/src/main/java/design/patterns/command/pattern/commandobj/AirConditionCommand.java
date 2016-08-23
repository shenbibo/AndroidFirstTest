package design.patterns.command.pattern.commandobj;

import android.util.Log;
import design.patterns.command.pattern.Command;
import design.patterns.command.pattern.CommandPatternTest;

/**
 * 空调控制的命令对象，将实际控制空调的操作的接受者与之合并在一起。
 * 
 * @author sWX284798
 * @date 2015年11月4日 上午9:39:33
 * @version
 * @since
 */
public class AirConditionCommand implements Command
{

    private boolean lightStatus;
    
    private int preStatus = 0;
    
    private String location;
    
    private static int currentStatus = 0;
    
    /**
     * 
     */
    public AirConditionCommand(boolean wantedStatus, String location)
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
        Log.d(CommandPatternTest.TAG, "preStatus = " + preStatus + ", currentStatus = " + currentStatus);
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
        Log.d(CommandPatternTest.TAG, location + " turn on the airCondition");
    }
    
    public void turnOff()
    {
        Log.d(CommandPatternTest.TAG, location + " turn off the airCondition");
    }

}
