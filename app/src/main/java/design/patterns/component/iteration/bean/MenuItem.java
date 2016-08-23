/*
 * 文 件 名:  MenuItem.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package design.patterns.component.iteration.bean;

import java.util.Iterator;

import android.util.Log;
import design.patterns.component.iteration.ComponentIteration;
import design.patterns.component.iteration.iteration.NullIterator;
import design.patterns.component.iteration.superclass.AbsComponent;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2015年12月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MenuItem extends AbsComponent
{

    private String name;
    
    private String description;
    
    private boolean isVegetarian;
    
    private double price;
    
    /** <默认构造函数>
     */
    public MenuItem(String name, String description, boolean isVegetarian, double price)
    {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.description = description;
        this.isVegetarian = isVegetarian;
        this.price = price;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return name;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public String getDescription()
    {
        // TODO Auto-generated method stub
        return description;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public boolean isVegetarian()
    {
        // TODO Auto-generated method stub
        return isVegetarian;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public double getPrice()
    {
        // TODO Auto-generated method stub
        return price;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public Iterator<?> createIterator()
    {
        // TODO Auto-generated method stub
        return new NullIterator();
    }
    
    /** {@inheritDoc} */
     
    @Override
    public void printComponent()
    {
        // TODO Auto-generated method stub
        Log.d(ComponentIteration.TAG, "menuItem.name = " + name + "\n"
                + "menuItem.description = " + description + "\n"
                + "menuItem.price = " + price + "\n"
                + "menuItem.isVegetarian = " + isVegetarian 
                + "\n--------------------------------------------------------------");
    }
    
}
