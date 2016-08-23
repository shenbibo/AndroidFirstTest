package design.patterns.command.pattern.receiver;

import design.patterns.command.pattern.CommandPatternTest;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月4日 上午9:28:00
 * @version
 * @since
 */
public class AirCondition
{

    private String location;
    /**
     * 
     */
    public AirCondition(String location)
    {
        this.location = location;
    }
    
    public void turnOn()
    {
        Log.d(CommandPatternTest.TAG, location + "turn on the airCondition");
    }
    
    public void turnOff()
    {
        Log.d(CommandPatternTest.TAG, location + "turn off the airCondition");
    }
}
