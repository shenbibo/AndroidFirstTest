/*
 * 文 件 名:  MiStudent.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ipc.test.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2016年1月14日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MiStudent implements Parcelable
{
    public String name = "";
    
    public int age;
    
    public MiStudent(String name, int age)
    {
        this.age = age;
        this.name = name;
    }

    /** {@inheritDoc} */
     
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /** {@inheritDoc} */
     
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
        dest.writeString(name);
        dest.writeInt(age);
        
    }
    
    private MiStudent(Parcel in )
    {
        name = in.readString();
        age = in.readInt();
    }
    
    public static final Parcelable.Creator<MiStudent> CREATOR = new Creator<MiStudent>()
    {
        
        @Override
        public MiStudent[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new MiStudent[size];
        }
        
        @Override
        public MiStudent createFromParcel(Parcel source)
        {
            // TODO Auto-generated method stub
            return new MiStudent(source);
        }
    };
    
    public boolean equals(Object o)
    {
        if(o instanceof MiStudent)
        {
            MiStudent s  = (MiStudent)o;
            if(this.age == s.age && this.name.equals(s.name))
            {
                return true;
            }
        }
        return false;
    };
    
}
