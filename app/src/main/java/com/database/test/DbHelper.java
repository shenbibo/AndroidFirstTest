package com.database.test;


import java.util.List;

import com.example.androidfirsttest.MyApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * 
 * @Time
 * @Description 创建数据库，更新数据库信息
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG = "DbHelper";
    
    private SQLiteDatabase mDb = null;
    
    private static DbHelper mInstance;
    
    private int referenceCount = 1;
    
    private static final Object LOCK = new Object();
    
    private static final String DB_NAME = "test.db";
        
    public static final String TABLE_NAME = "person";
    
    @SuppressWarnings("unused")
    private Context context;
    
    public static final String KEY_ID = "_id";
    
    public static final String NAME = "name";
    
    public static final String AGE = "age";
    
    public static final String INFO = "info";
    
    public static final int  DATABASE_VERSION = 1;

    protected DbHelper(Context context) {
        this(context, DB_NAME, null, DATABASE_VERSION);
    }  
    
    protected DbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context.getApplicationContext();
    }
    
    public static synchronized DbHelper getInstance() 
    {  
        if (mInstance == null) 
        {  
            mInstance = new DbHelper(MyApplication.getInstance());  
        }  
        return mInstance;  
    } 

    /**
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[name] TEXT,");
        sBuffer.append("[age] INTEGER,");
        sBuffer.append("[info] TEXT)");
        db.execSQL(sBuffer.toString());
        //if(mDb == null)
        {
            mDb = db;
        }
               
    }
    

    public void deleteAndCreateNewTable()
    {
        // TODO Auto-generated method stub
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);

    }
    
    
    /***/
    public void excuteSQL(String sql)
    {
        incrementReferenceCount();
        SQLiteDatabase db = null;
        try
        {
            db = getWritableDatabase();
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e(TAG + "_excuteSQL", "", e);
        }
        finally
        {
            decrementReferenceCount();
        }
    }
    
    public long insert(String table, ContentValues values)
    {
        incrementReferenceCount();
        SQLiteDatabase db = null;
        long index = -1;
        try
        {
            db = getWritableDatabase();
            index = db.insertOrThrow(table, null, values);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e(TAG + "_insert", "", e);
        }
        finally
        {
            decrementReferenceCount();
        }
        return index;
    }
    
    public long insertTest(String table, ContentValues values)
    {
        long index = -1;
        try
        {
            //mDb = getWritableDatabase();
            index = mDb.insertOrThrow(table, null, values);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e(TAG + "_insert", "", e);
        }
        finally
        {
            //decrementReferenceCount();
        }
        return index;
    }
    
    public int delete(String table, String whereClause, String[] whereArgs)
    {
        incrementReferenceCount();
        SQLiteDatabase db = null;
        int count = -1;
        try
        {
            db = getWritableDatabase();
            count = db.delete(table, whereClause, whereArgs);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e(TAG + "_delete", "", e);
        }
        finally
        {
            decrementReferenceCount();
        }
        return count;
    }
    
    public int update(String table, ContentValues values, String whereClause, String[]whereArgs)
    {
        incrementReferenceCount();
        SQLiteDatabase db = null;
        int count = -1;
        try
        {
            db = getWritableDatabase();
            count = db.update(table, values, whereClause, whereArgs);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e(TAG + "_update", "", e);
        }
        finally
        {
            decrementReferenceCount();
        }
        return count;
    }
    
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, 
            String groupBy, String having, String orderBy, String limit)
    {
        incrementReferenceCount();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try
        {
            db = getWritableDatabase();
            cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e(TAG + "_query", "", e);
        }
        finally
        {
            decrementReferenceCount();
        }               
        return cursor;
    }
    
    public interface ICovertDbParams
    {
        
        
        public ContentValues covert2ContentValues();
        
        public String getSelection();
        
        public String[] getSelectionArgs();
    }
    
    public void insertBatch(String table, List<ICovertDbParams> list)
    {
        if(list != null && list.size() != 0)
        {
            incrementReferenceCount();
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try
            {
                for (ICovertDbParams values : list)
                {
                    db.insertOrThrow(table, null, values.covert2ContentValues());
                }
                db.setTransactionSuccessful();
            }
            catch (Exception e)
            {
                // TODO: handle exception
                Log.e(TAG + "_insertBatch", "", e);
            }
            finally
            {
                try
                {
                    db.endTransaction();
                }
                catch (Exception e2)
                {
                    // TODO: handle exception
                    Log.e(TAG + "_insertBatch", "", e2);
                } 
                decrementReferenceCount();
            }
        }
    }

    public void updateBatch(String table, List<ICovertDbParams> list)
    {
        if(list != null && list.size() != 0)
        {
            incrementReferenceCount();
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();           
            try
            {
                for(ICovertDbParams values : list)
                {
                    db.update(table, values.covert2ContentValues(), values.getSelection(), values.getSelectionArgs());
                }                
                db.setTransactionSuccessful();
            }
            catch (Exception e)
            {
                // TODO: handle exception
                Log.e(TAG + "_updateBatch", "", e);
            }
            finally
            {
                try
                {
                    db.endTransaction();
                }
                catch (Exception e2)
                {
                    // TODO: handle exception
                    Log.e(TAG + "_updateBatch", "", e2);
                }               
                decrementReferenceCount();
            }
        }
    }
    
    
    public void mergeBatch(String table, List<ICovertDbParams> list)
    {
        if(list != null && list.size() != 0)
        {
            incrementReferenceCount();
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();           
            try
            {
                for(ICovertDbParams values : list)
                {
                    if(db.update(table, values.covert2ContentValues(), 
                            values.getSelection(), values.getSelectionArgs()) <= 0)  //若不存在，则插入新的数据
                    {
                        db.insertOrThrow(table, null, values.covert2ContentValues());
                    }
                }                
                db.setTransactionSuccessful();
            }
            catch (Exception e)
            {
                // TODO: handle exception
                Log.e(TAG + "_mergeBatch", "", e);
            }
            finally
            {
                try
                {
                    db.endTransaction();
                }
                catch (Exception e2)
                {
                    // TODO: handle exception
                    Log.e(TAG + "_mergeBatch", "", e2);
                }               
                decrementReferenceCount();
            }
        }
    }
    
    
    /**
     * 更新数据库信息
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mDb = db;
        //initTables(newVersion);
    }

    /**
     * 更新数据库信息
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mDb = db;
        //initTables(newVersion);
    }

    

    
    
    
    
    /**
     * 引用计数加1。
     * */
    private void incrementReferenceCount()
    {
        synchronized(LOCK)
        {
            if(referenceCount <= 0)  //此处当小于等于0的时候，表示数据库已经关闭，此时赋值为2，代表重新打开数据库。
            {
                referenceCount = 2;
            }
            else
            {
                referenceCount++;
            }
        }
    }
    
    /**
     * 引用计数减1。
     * @throws 
     * */
    private void decrementReferenceCount()
    {
        boolean isReferenceZero = false;
        synchronized(LOCK)
        {
            isReferenceZero = (--referenceCount <= 0);
            if(isReferenceZero)
            {    
                Log.d(TAG, "invoke the super.close() to close the db");
                super.close(); 
                //close();
            }
            else
            {
                Log.d(TAG, "referenceCount = " + referenceCount);    
            }
        }       
    }
    
    @Override
    public void close()
    {
        Log.d(TAG, "called the close()");
        decrementReferenceCount();
    }
    
}

