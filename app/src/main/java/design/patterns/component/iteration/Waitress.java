/*
 * 文 件 名:  Waitress.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package design.patterns.component.iteration;

import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;
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
public class Waitress
{
    AbsComponent allMenus;
    
    /** <默认构造函数>
     */
    public Waitress(AbsComponent allMenus)
    {
        // TODO Auto-generated constructor stub
        this.allMenus = allMenus;
    }
    
    public void printAllMenus()
    {
        allMenus.printComponent();
    }
    
    
    /**
     * 通过使用外部迭代器实现
     * 使用内部迭代方式更简单，但是灵活性较差
     * */
    public void printVegetarianMenus()
    {
        //Log.d(ComponentIteration.TAG, "allMenus.size()=" + allMenus.);
        //使用外部迭代器的方式
//        Iterator<?> iterator = allMenus.createIterator();
//        while(iterator.hasNext())
//        {
//            AbsComponent component = (AbsComponent) iterator.next();
//            try
//            {
//                if(component.isVegetarian())
//                {
//                    component.printComponent();
//                }
//            }
//            catch (UnsupportedOperationException e)
//            {
//                // TODO: handle exception
//            }
//        }
        
        //使用内部迭代方式
        allMenus.printVegetarianMenus();
    }

}
