/*
 * 文 件 名:  AbsComponent.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package design.patterns.component.iteration.superclass;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 抽象类接口，用于给其他的所有的菜单、或菜单项继承，默认实现全部抛出不支持的异常
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2015年12月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class AbsComponent
{

    public void add(AbsComponent component)
    {
        throw new UnsupportedOperationException();
    }
    
    public void printComponent()
    {
        throw new UnsupportedOperationException();
    }
    
    public void printVegetarianMenus()
    {
        throw new UnsupportedOperationException();
    }
    
    public AbsComponent getChild(int i)
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 是否是素食
     * */
    public boolean isVegetarian()
    {
        throw new UnsupportedOperationException();
    }
    
    public String getName()
    {
        throw new UnsupportedOperationException();
    }
    
    public double getPrice()
    {
        throw new UnsupportedOperationException();
    }
        
    public String getDescription()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 创建迭代器
     * */
    public Iterator<?> createIterator()
    {
        throw new UnsupportedOperationException();
    }
    
//    public Iterator<?> createIterator(LinkedList<Iterator<?>> list)
//    {
//        throw new UnsupportedOperationException();
//    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param component
     * @see [类、类#方法、类#成员]
     */
    public void remove(AbsComponent component)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }
    
    /**
     * 是否已经放入队列中。
     * */
    public boolean isInList = false;
    
}
