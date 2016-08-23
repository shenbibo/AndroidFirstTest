package com.database.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月25日 上午10:15:34
 * @version
 * @since
 */
public final class DbTest
{
    private static final String TAG = "DbTest";
    
    private static int times = 10;
    
    private static DbHelper db;
    
    private static Context context;
    
    private static ArrayList<ArrayList<Student>> data = new ArrayList<ArrayList<Student>>();
    
    private static List<Student> listStudent = new ArrayList<Student>();
    
    private static boolean isDataReady = false;
    
    private static volatile int startIndex = 0;
    
    private static volatile boolean isDataCheckFinished = true;
    
    private static final SparseArray<String> sMap = new SparseArray<String>(20);
    
    static 
    {
        sMap.append(0, "insert one data");
        sMap.append(1, "insert some datas");
        sMap.append(2, "query data");       
        sMap.append(3, "delete one data");
        sMap.append(4, "delete some datas");
        sMap.append(5, "update one data");
        sMap.append(6, "update some datas");
        sMap.append(7, "merge datas");
        sMap.append(8, "close db");
    }
    
    
    private static void verifyStudentData(List<Student> list)
    {
        boolean isError = false;
        int errorCount = 0;
        //isDataCheckFinished = false;
        for(Student s : list)
        {
            String[] values1 = s.name.split("_");
            String[] values2 = s.info.split("_");
            if(!(String.valueOf(s.age).equals(values2[1]) 
                    && values1[0].equals(values2[0])))
            {
                isError = true;
                errorCount++;
                Log.d(TAG, s.toString());
            }
        }
        if (isError)
        {
            Log.d(TAG, "errorCount = " + errorCount);
        }
        else
        {
            Log.d(TAG, "they are all right");
        }
        //isDataCheckFinished = true;
    }
    
    static class MyThread extends Thread
    {
        private int num;
        /**
         * 
         */
        public MyThread(int num)
        {
            // TODO Auto-generated constructor stub
            this.num = num;
        }
        
        @Override
        public void run()
        {
            
            int randomNum = (new Random().nextInt(20));
            
            Log.d(TAG, "perform = " + sMap.get(num) + ", randomNum = " + randomNum);
            switch (num)
            {
                case 0:   //插入一条数据
                    db.insert(DbHelper.TABLE_NAME, 
                            new Student(startIndex + "_zhang", randomNum, startIndex + "_" + randomNum)
                                .covert2ContentValues());
                    ++startIndex;
                    break;
                case 1:   //批量插入数据
                    db.insertBatch(DbHelper.TABLE_NAME, createTestData(startIndex));
                    break;
                    
                case 8:  //关闭数据库
                    db.close();
                    break;
                 
                case 3:  //删除一条数据                    
                    db.delete(DbHelper.TABLE_NAME, "_id = ?", new String[]{String.valueOf(startIndex)});
                    break;
                    
                case 4:  //批量删除
                    db.delete(DbHelper.TABLE_NAME, "_id < ? and _id > ?", 
                            new String[]{String.valueOf(startIndex - randomNum), String.valueOf(startIndex - 2 * randomNum)});
                    break;
                    
                case 5: //更新一条数据
                    db.update(DbHelper.TABLE_NAME, new Student(startIndex + "_zhang", randomNum, startIndex + "_" + randomNum)
                                .covert2ContentValues(), "_id = ? ", 
                                new String[]{String.valueOf(startIndex - randomNum)});
                    break;
                case 6: //批量更新
                    db.updateBatch(DbHelper.TABLE_NAME, createTestData(startIndex));
                    break;
                    
                case 7:  //合并
                    db.mergeBatch(DbHelper.TABLE_NAME, createTestData(startIndex));
                    break;
                    
                case 2:  //查询
                    checkDatabaseData("_id < ?", new String[]{String.valueOf(1024)}, String.valueOf(1000 + randomNum));
                    break;
                    
                default:
                    break;
            }
            //Log.d(TAG, "this insert is end num = " + num);
                
        }
    }
    
    private static List<DbHelper.ICovertDbParams> createTestData(int num)
    {
        ArrayList<DbHelper.ICovertDbParams> temp = new ArrayList<DbHelper.ICovertDbParams>();
        //ArrayList<Student> temp = new ArrayList<Student>();
        for (int i = 0; i < 50; i++)
        {
            temp.add(new Student(startIndex + "_zhang", i, String.valueOf(startIndex) + "_" + String.valueOf(i)));
            ++startIndex;
        }       
        //isDataReady = true;
        return temp;
    }
    
    public static void startTest(Context context)
    {
        DbTest.context = context;
        getDbHelperObj();
        db.deleteAndCreateNewTable();
        new Thread()
        {
            @Override
            public void run() 
            {
                db.insertBatch(DbHelper.TABLE_NAME, createTestData(startIndex));
                for (int i = 0; i < 200; i++)
                {
                    int performNum = -1;
                    if(i == 123)
                    {
                        performNum = 8;
                    }
                    else
                    {
                        performNum = new Random().nextInt(8);                       
                    } 
                    (new MyThread(performNum)).start();
                    //SystemClock.sleep(10);
                }
                //SystemClock.sleep(5000);
//                db.insertTest(DbHelper.TABLE_NAME, new Student(startIndex + "_li", 19891001, startIndex + "_" + 19891001)
//                                .covert2ContentValues());
                
            };
        }.start();
    }
    
    
    
    private static SQLiteOpenHelper getDbHelperObj()
    {
        synchronized (DbTest.class)
        {
            if(db == null)
            {
                db = new DbHelper(context);
            } 
        }       
        return db;
    }
    
    public static void testDbisClosed()
    {
        try
        {
            db.insertTest(DbHelper.TABLE_NAME, new Student(startIndex + "_li", 19891001, startIndex + "_" + 19891001)
            .covert2ContentValues());
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e(TAG, "", e);
        }
    }
    
    
    private static void checkDatabaseData(String selection, String[] selectionArgs, String limit)
    {
        Cursor c = db.query(DbHelper.TABLE_NAME, null, selection, selectionArgs, null, null, null, limit);
        List<Student> list = new ArrayList<Student>();
        if(c != null)
        {
            //ArrayList<Student> persons = new ArrayList<Student>();
            while (c.moveToNext())
            {
                //List<Student> list = new ArrayList<Student>();
                Student person = new Student();
                person.name = c.getString(c.getColumnIndex("name"));
                person.age = c.getInt(c.getColumnIndex("age"));
                person.info = c.getString(c.getColumnIndex("info"));
                //if(isDataCheckFinished)
                {
                    list.add(person);                    
                }
            }
            c.close();
        }
        //if(isDataCheckFinished)
        {
            Log.d(TAG, "listStudent length = " + list.size());
            verifyStudentData(list);
        }
        
    }
}
