package design.patterns.command.pattern;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import design.patterns.command.pattern.commandobj.AirConditionCommand;
import design.patterns.command.pattern.commandobj.CeilingFanCommand;
import design.patterns.command.pattern.commandobj.CommandTV;
import design.patterns.command.pattern.commandobj.LightCommand;
import design.patterns.command.pattern.commandobj.MacroCommand;
import design.patterns.command.pattern.receiver.CeilingFan;
import design.patterns.command.pattern.receiver.Light;
import android.util.Log;

/**
 * 命令模式：将要请求的动作封装成命令对象，命令对象实现一个表示命令的接口，这样可以实现请求对象与实际执行所请求操作的对象之间实现解耦
 * 将实际执行请求操作的对象作为参数传递到命令对象中。
 * 
 * @author sWX284798
 * @date 2015年11月3日 上午11:09:26
 * @version
 * @since
 */
public class CommandPatternTest
{
    public static final String TAG = "CommandPatternTest";
    
    public static void test()
    {
        normalTest();
        macroCommandTest();
        threadGetCommandTest();
    }

    
    /**
     * 正常的测试，没有使用批量命令。
     * */
    public static void normalTest()
    {
        Log.d(TAG, "normal test start/////////////////////////");
        RemoteControl rc = new RemoteControl();
        
        Light light = new Light("bedroom");
        
        CeilingFan fan = new CeilingFan("living room");
        
        Command cfcL = new CeilingFanCommand(fan, CeilingFanCommand.UP);
        Command cfcR = new CeilingFanCommand(fan, CeilingFanCommand.DOWN);
        
        Command lcL = new LightCommand(light, true);
        Command lcR = new LightCommand(light, false);
        
        rc.setCommand(0, lcL, lcR);
        rc.setCommand(1, cfcL, cfcR);
        
        rc.pressLeftButton(0);
        rc.pressLeftButton(1);
        rc.pressLeftButton(1);
        rc.pressLeftButton(1);
        rc.pressRightButton(0);
        rc.pressUndoButton();
        //fan.setFanStatus(CeilingFan.HIGH);
        rc.pressRightButton(1);
        rc.pressUndoButton();
        
        Log.d(TAG, "normal test end///////////////////////////");               
    }
    
    /**
     * 批量执行命令测试
     * */
    public static void macroCommandTest()
    {
        Log.d(TAG, "macroCommandTest start//////////////////////////////");
        
        RemoteControl rc = new RemoteControl();
        
        Light light = new Light("living room");        
        CeilingFan fan = new CeilingFan("bedroom");
        
        Command cfcL = new CeilingFanCommand(fan, CeilingFanCommand.UP);
        Command cfcR = new CeilingFanCommand(fan, CeilingFanCommand.DOWN);
        
        Command lcL = new LightCommand(light, true);
        Command lcR = new LightCommand(light, false);
        
        
        CommandTV tvL = new CommandTV(true, "x room");
        CommandTV tvR = new CommandTV(false, "X ROOM");
        
        AirConditionCommand acL = new AirConditionCommand(true, "Y room");
        AirConditionCommand acR = new AirConditionCommand(false, "Y room");
        
        Command[] csL = {cfcL, lcL, tvL, acL};
        Command[] csR = {cfcR, lcR, tvR, acR};
        
        MacroCommand mcL = new MacroCommand(csL);
        MacroCommand mcR = new MacroCommand(csR);
        
        rc.setCommand(0, mcL, mcR);
        
        rc.pressLeftButton(0);
        rc.pressRightButton(0);
        rc.pressUndoButton();
        
        
        Log.d(TAG, "macroCommandTest end////////////////////////////////");
    }
    
    /**
     * 线程获取命令测试，尤其适用于当各自命令之间没有关系，将命令存储在队列只中，注意使用同步队列来实现。
     * */
    public static void threadGetCommandTest()
    {
        Log.d(TAG, "threadGetCommandTest start//////////////////////////////");
        final BlockingQueue<Command> queue = new LinkedBlockingQueue<Command>();
        final Command[] commands = new Command[10];
        Light light = new Light("living room");        
        CeilingFan fan = new CeilingFan("bedroom");
        
        commands[0] = new CeilingFanCommand(fan, CeilingFanCommand.UP);
        commands[1] = new CeilingFanCommand(fan, CeilingFanCommand.DOWN);
        
        commands[2] = new LightCommand(light, true);
        commands[3] = new LightCommand(light, false);
        
        
        commands[4] = new CommandTV(true, "x room");
        commands[5] = new CommandTV(false, "X ROOM");
        
        commands[6] = new AirConditionCommand(true, "Y room");
        commands[7] = new AirConditionCommand(false, "Y room");
        
        Command[] csL = {commands[0], commands[2], commands[4], commands[6]};
        Command[] csR = {commands[1], commands[3], commands[5], commands[7]};
        
        commands[8] = new MacroCommand(csL);
        commands[9] = new MacroCommand(csR);
        
        
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                int count = 0;
                int index = 0;
                Random rand = new Random();
                while(count < 100)
                {
                    index = rand.nextInt(10);
                    queue.add(commands[index]);
                    try
                    {
                        Thread.sleep(300);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ++count;
                }               
            }
            
        }).start();
        
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                int count = 0;
                int index = 0;
                Random rand = new Random();
                Command c = null;
                while(count < 100)
                {
                    index = rand.nextInt(2);
                    try
                    {
                        c = queue.take();
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(index < 1)
                    {
                        c.execute();
                    }
                    else
                    {
                        c.undo();
                    }
                    ++count;
                }               
            }
            
        }).start();
        

        
        Log.d(TAG, "threadGetCommandTest end////////////////////////////////");
    }
    
}
