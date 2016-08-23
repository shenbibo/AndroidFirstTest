/*
 * 文 件 名:  ComponentIterator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package design.patterns.component.iteration.iteration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import android.util.Log;
import design.patterns.component.iteration.ComponentIteration;
import design.patterns.component.iteration.bean.Menu;
import design.patterns.component.iteration.superclass.AbsComponent;

/**
 * 组合模式的外部迭代器
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2015年12月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ComponentIterator implements Iterator<AbsComponent>
{

    /**
     * 用于记录迭代器在当前组合的树形结构中的位置，模拟栈的功能，后进先出
     * */
    private LinkedList<Iterator<?>> recordList;
    
    private AbsComponent tempComponent = null;
    //private Stack<Iterator<?>> recordList = new Stack<Iterator<?>>();
    
    /** <默认构造函数>
     */
//    public ComponentIterator(Iterator<?> iterator, LinkedList<Iterator<?>> list)
//    {
//        // TODO Auto-generated constructor stub
//        recordList = list;
//        recordList.push(iterator);
//    }
    
    public ComponentIterator(Iterator<?> iterator)
    {
        // TODO Auto-generated constructor stub
        recordList = new LinkedList<Iterator<?>>();
        recordList.push(iterator);
    }
    
     
    @Override
    public boolean hasNext()
    {
        // TODO Auto-generated method stub
        if(!recordList.isEmpty())
        {
            Iterator<?> iterator = recordList.peek();
            //Iterator<?> iterator = recordList.getLast();
            //如果获取的迭代器所指向的集合没有下一个元素，则从保存的栈中移除该迭代器，继续访问下一个保存的迭代器
            if(!iterator.hasNext())
            {
                //移除栈顶元素
                recordList.pop();
                //recordList.removeLast();
                
                //从列表中移除其迭代器时，将标志位置为false，且不持有该组件的引用。
                if(tempComponent != null)
                {
                    tempComponent.isInList = false;   
                    tempComponent = null;
                }
                
                return hasNext();
            }
            else
            {
                return true; 
            }                        
        }
        else
        {
            return false;
        }
        
    }

    /** {@inheritDoc} */
     
    @Override
    public AbsComponent next()
    {
        // TODO Auto-generated method stub
        if(hasNext())
        {
            //使用peek方法等同于获取栈顶的元素
            Iterator<?> iterator = recordList.peek();
            //Iterator<?> iterator = recordList.getLast();
            //注意此处获得的iterator一定不是ComponentIterator对象，否则会导致无限死循环
            //此处AbsComponent的接口类型的iteration来自arrayList.iterator()方法。
            AbsComponent component = (AbsComponent) iterator.next();
            //此部分代码保证保存在iterator列表中的迭代器全部都是菜单，不包括菜单子项。
            //注意当一个菜单位于多级菜单之下，此段代码会导致同一个元素被多次打印，次数为n-1,n为菜单级别
            //出现该问题的原因为递归调用next方法，导致以下if部分代码被重复调用
            //解决方案:为component添加一个标志位isInList，ComponentIterator添加tempComponent成员变量
            //当该组件位于任意list中时，标志位置为true，不在时标志位置为false，且将tempComponent=null。
            if(component instanceof Menu && !component.isInList)
            {
                Log.d(ComponentIteration.TAG, "-------next menu.name = " + component.getName());
                //等同于将一个元素压入栈顶
                //recordList.push(component.createIterator());
                //Iterator<?> tempIterator = component.createIterator();
                Iterator<?> tempIterator = component.createIterator();
                //if(!recordList.contains(tempIterator))
                {
                    recordList.push(tempIterator);
                    //将标志位值为true，且保存该组件的引用
                    component.isInList = true;
                    tempComponent = component;
                }
                
            }
            return component;
        }
        else
        {
            return null;  
        }
        
    }

    /** 
     * 暂不支持移除操作
     * {@inheritDoc} 
     * */
     
    @Override
    public void remove()
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }
    
    /** 
     * 
     * {@inheritDoc} 
     * <p>若两个迭代器的recordList参数为同一个，则认为它们相等，否则不等。
     * */
     
    @Override
    public boolean equals(Object o)
    {
        // TODO Auto-generated method stub
        if(o instanceof ComponentIterator)
        {
            ComponentIterator iterator = (ComponentIterator) o;
            if(iterator.recordList == this.recordList)
            {
                return true;
            }            
        }
        return false;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public int hashCode()
    {
        // TODO Auto-generated method stub
        return recordList.hashCode();
    }

}
