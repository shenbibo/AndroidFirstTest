/*
 * 文 件 名:  Menu.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package design.patterns.component.iteration.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;
import design.patterns.component.iteration.ComponentIteration;
import design.patterns.component.iteration.iteration.ComponentIterator;
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
public class Menu extends AbsComponent
{
    private ArrayList<AbsComponent> menuComponents;
    private String menuName;
    private String description;
    
    /** <默认构造函数>
     */
    public Menu(String name, String description)
    {
        // TODO Auto-generated constructor stub
        menuName = name;
        this.description = description;
        menuComponents = new ArrayList<AbsComponent>();
    }
    
    
    /** 
     * {@inheritDoc} 
     * */     
    @Override
    public void add(AbsComponent component)
    {
        // TODO Auto-generated method stub
        menuComponents.add(component);
    }
    
    /** {@inheritDoc} */
     
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return menuName;
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
    public void remove(AbsComponent component)
    {
        // TODO Auto-generated method stub
        menuComponents.remove(component);
    }
    
    /** {@inheritDoc} */
     
    @Override
    public Iterator<?> createIterator()
    {
        // TODO Auto-generated method stub
        //
        //return menuComponents.iterator();
        //保证每一个Menu对象都支持深度遍历其子节点（如果子节点也有子节点的话）
        return new ComponentIterator(menuComponents.iterator());
    }
    
//    /** {@inheritDoc} */
//     
//    @Override
//    public Iterator<?> createIterator(LinkedList<Iterator<?>> list)
//    {
//        // TODO Auto-generated method stub
//        return new ComponentIterator(menuComponents.iterator(), list);
//    }
    
    /** {@inheritDoc} */
     
    @Override
    public void printComponent()
    {
        // TODO Auto-generated method stub
        Log.d(ComponentIteration.TAG, "menu.name = " + menuName + "\n"
                + "menu.description = " + description + "\n"
                + "menu.size = " + menuComponents.size());

        Iterator<AbsComponent> iterator = menuComponents.iterator();
        while (iterator.hasNext())
        {
            //注意此处递归调用子类的print方法。
            AbsComponent absComponent = (AbsComponent) iterator.next();
            absComponent.printComponent();
            
            if(absComponent instanceof MenuItem)
            {
                if(absComponent.isVegetarian())
                {
                    absComponent.printComponent();
                }
            }
            
        }
    }
    
    /** 
     * 内部迭代方式实现帅选素食。
     * {@inheritDoc} */
     
    @Override
    public void printVegetarianMenus()
    {
        // TODO Auto-generated method stub
        Log.d(ComponentIteration.TAG, "menu.name = " + menuName + "\n"
                + "menu.description = " + description + "\n"
                + "menu.size = " + menuComponents.size());

        Iterator<AbsComponent> iterator = menuComponents.iterator();
        while (iterator.hasNext())
        {
            //注意此处递归调用子类的print方法。
            AbsComponent absComponent = (AbsComponent) iterator.next();
            if(absComponent instanceof MenuItem)
            {
                if(absComponent.isVegetarian())
                {
                    absComponent.printComponent();
                }
            }
            else
            {
                absComponent.printVegetarianMenus();
            }
        }
    }
}
