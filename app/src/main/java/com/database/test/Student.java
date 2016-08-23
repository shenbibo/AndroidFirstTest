package com.database.test;

import java.util.Random;

import com.database.test.DbHelper.ICovertDbParams;

import android.R.integer;
import android.R.string;
import android.content.ContentValues;

/**
 * @author sWX284798
 *
 */
public class Student implements ICovertDbParams
{
    public String name;
    public int age;
    public String info;
    
    public Student()
    {
        
    }
    
    public Student(String name, int age, String info)
    {
        this.name = name;
        this.age = age;
        this.info = info;
    }
    
    @Override
    public String toString()
    {
        return name + " " + age + " " + info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentValues covert2ContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(DbHelper.NAME, name);
        values.put(DbHelper.AGE, age);
        values.put(DbHelper.INFO, info);
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelection()
    {
        
        return "_id < ? and _id > ?";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getSelectionArgs()
    {
        int baseValue = new Random().nextInt(1456);
        String[] values = new String[]{String.valueOf(baseValue + 123), String.valueOf(baseValue)};
        return values;
    }

}
