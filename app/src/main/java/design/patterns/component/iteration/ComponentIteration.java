/*
 * 文 件 名:  ComponentIteration.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package design.patterns.component.iteration;

import android.util.Log;
import design.patterns.component.iteration.bean.Menu;
import design.patterns.component.iteration.bean.MenuItem;
import design.patterns.component.iteration.superclass.AbsComponent;

/**
 * 组合迭代模式，详情见《Head First 设计模式》第9章
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2015年12月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ComponentIteration
{

    public static final String TAG = "ComponentIteration";
    
    
    public static void test()
    {
        AbsComponent allMenus = new Menu("ALL MENUS", "this is the total menus");
        AbsComponent breakfastMenus = new Menu("BREAKFAST MUENUS", "this is the breakfastMenus");
        AbsComponent lanuchMenus = new Menu("LANUCH MENUS", "this is the lanuchMenus");
        AbsComponent dinnerMenus = new Menu("DINNER", "this is the dimmerMenus");
        
        breakfastMenus.add(new MenuItem("rice stick", "is made by chinese rice", true, 5.5));
        breakfastMenus.add(new MenuItem("noodles", "is made by wheat", true, 6.2));
        breakfastMenus.add(new MenuItem("Chinese Buns filled with meat", "is made by wheat and meat", 
                false, 1.5));
        
        lanuchMenus.add(new MenuItem("rice", "is chinese major food", true, 1.0));
        lanuchMenus.add(new MenuItem("beef", "is made by crow", false, 20.8));
        lanuchMenus.add(breakfastMenus);

        dinnerMenus.add(new MenuItem("eggs soup", "a soup is made by eggs", false, 8.9));
        dinnerMenus.add(new MenuItem("Chinese cabbage", "a type cabbage is produce by china", true, 6.3));
        dinnerMenus.add(breakfastMenus);

        
        allMenus.add(breakfastMenus);
        allMenus.add(lanuchMenus);
        allMenus.add(dinnerMenus);
        
        Waitress waitress = new Waitress(allMenus);
        Log.d(TAG, "print all menus-------------------------------------------------");
        waitress.printAllMenus();
        
        Log.d(TAG, "print the vegetarian menus--------------------------------------");
        waitress.printVegetarianMenus();
    }
}
