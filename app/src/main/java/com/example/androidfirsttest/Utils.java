package com.example.androidfirsttest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月11日 下午2:28:05
 * @version
 * @since
 */
public class Utils
{
    private static final String TAG = "Utils";
    
    
    public static String readFilenNormal(Context context)
    {
        FileInputStream fis = null;
        BufferedReader br = null;
        String data = "";
        try
        {
            fis = context.openFileInput("test.txt");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = null;
            
            while((str = br.readLine()) != null)
            {
                if("".equals(str))
                {
                    Log.d(TAG + "_NORMAL", "str is empty!");
                }
                else
                {
                    Log.d(TAG + "_NORMAL", str);
                }
                data += str;
            }
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
    
    public static String readFileAllData(Context context)
    {
        FileInputStream fis = null;
        BufferedReader br = null;
        String data = "";
        try
        {
            fis = context.openFileInput("test.txt");
            br = new BufferedReader(new InputStreamReader(fis));
            data = readStringByOneChar(br, 128);
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
    
    public static String readFileOneLineByOneChar(Context context)
    {
        FileInputStream fis = null;
        BufferedReader br = null;
        String data = "";
        try
        {
            fis = context.openFileInput("test.txt");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = null;
            while((str = readLineByOneChar(br, 10)) != null)
            {
                if("".equals(str))
                {
                    Log.d(TAG + "_ONECHAR", "str is empty!");
                }
                else
                {
                    Log.d(TAG + "_ONECHAR", str);
                }
                data += str;
            }
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
    
    public static void writeFile(Context context)
    {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try
        {
            fos = context.openFileOutput("test.txt", Context.MODE_PRIVATE);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            String testValue = "12344654666\nkihqjwiijwqjwogooqpowirtwoqtiop\nsgllsgjsljgqoowu32525\n\nlallslgeowp\r\nkgaw;kwrsq\rlgsj\n\n\nlsgjl\r\n\r\n";
            bw.write(testValue, 0, testValue.length());
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if(bw != null)
            {
                try
                {
                    bw.flush();
                }
                catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try
                {
                    bw.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    /**
     * 读取流中所有能够读取的字符，直到文件末尾。<p>
     * 每次读取一个字符，该方法用于替换因BufferedReader.readLine(),可能导致的OOM。<br>
     * 该方法在未超出给定的最大限定值之前，会一直读取下去，遇到'\r', '\n'时会被跳过。<br>
     * @param in 输入流，注意该流不会再该该方法中被关闭。<br>
     * @param maxAllowChars 最多允许从给定流中读取的字符的个数。<br>
     * @return 输入流为空，或maxAllowChars < 0 则返回null，若读取的字符达到最大值或全部读取完，否则返回包含除'\r', '\n'外的所有字符的字符串。
     * @throws IOException 
     * */
    public static String readStringByOneChar(BufferedReader in, final int maxAllowChars) throws IOException
    {
        if(in == null || maxAllowChars < 0)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder(1024);
        int readData = 0;
        //每次读取一个字节，遇到换行符或者终止符直接仍掉，当返回的字符串长度大于给定的最大值时，直接返回null。
        while((readData = in.read()) != -1) 
        {
            if(readData == '\r' || readData == '\n')
            {
                 continue;                   
            }
            sb.append((char)readData);
            if(sb.length() >= maxAllowChars)
            {
                Log.e(TAG, "readStringByOneChar -> the read chars counts is reached the maxAllowChars = "
                     + maxAllowChars + "sb.length = " + sb.length());
                return sb.toString();
            }            
        }      
        return sb.toString();
    }
    
    /**
     * 实现从文本中读取一行，但是每次读取只读取一个字符，并设置最大允许读取的字符数，防止可能出现的OOM。<br>
     * 调用该方法等同于调用BufferedReader.readLine()方法。<br>
     * 每次读取一个字符，直到遇到'\r', '\n'，返回所有获取的字符，不包含'\r', '\n'。<p>
     * 使用该方法并不会删除，跳过流中超出给定最大允许读取的字符数之后的字符，所以如果有一行文本有100个字符，限定的每次最大读取的字符数设为30，
     * 如果使用该方法读取，该行字符会被分为4次读取，分别为30， 30， 30， 10。
     * @return 若输入流为空，或maxAllowChars < 0，或者已经到流的末尾  则返回null
     * @throws IOException 
     * */
    public static String readLineByOneChar(BufferedReader in, final int maxAllowChars) throws IOException
    {
        if(in == null || maxAllowChars <= 0)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder(1024);
        int readData = 0;
        //每次读取一个字节，遇到换行符或者终止符直接仍掉，当返回的字符串长度大于等于给定的最大值时，直接返回sb。
        int readCounts = 0;
        while((readData = in.read()) != -1) 
        {
            ++readCounts;
            //当碰到'\r'是，尝试读取下一个字符如果是'\n',则跳出，否则返回标记的位置
            if(readData == '\r')
            {
                in.mark(1);  
                if(in.read() != '\n')
                {
                    in.reset();
                }
                break;
            }
            else if(readData == '\n')
            {
                 break;                   
            }
            sb.append((char)readData);
            
            if(sb.length() >= maxAllowChars)
            {
                Log.e(TAG, "readLineByOneChar -> the read chars counts is reached the maxAllowChars = "
                     + maxAllowChars + "sb.length = " + sb.length());
                return sb.toString();
            }
            
        }  
        if(readCounts == 0) //如果为0，则说明从开始读取时，就已经在文件的末尾了，则直接返回null。
        {
            return null;
        }
        return sb.toString();
    }

}
