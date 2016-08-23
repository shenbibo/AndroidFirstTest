package design.patterns.command.pattern.receiver;

import android.util.Log;
import design.patterns.command.pattern.CommandPatternTest;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月4日 上午9:24:54
 * @version
 * @since
 */
public class TV
{

    private String location;
    
    /**
     * 
     */
    public TV(String location)
    {
        this.location = location;
    }
    
    public void turnOn()
    {
        Log.d(CommandPatternTest.TAG, location + "turn on the TV");
    }
    
    public void turnOff()
    {
        Log.d(CommandPatternTest.TAG, location + "turn off the TV");
    }
}
