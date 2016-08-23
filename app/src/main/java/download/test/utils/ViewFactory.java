package download.test.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle.Control;

import download.test.view.AbsBasicView;
import download.test.viewbeans.BaseBean;
import android.content.Context;
import android.util.SparseArray;

/**
 * 
 * 创建listView子Item的抽象工厂
 * @author sWX284798
 * @date 2015年11月26日 上午9:56:00
 * @version
 * @since
 */
public final class ViewFactory
{

    private static int viewTypeNumber = 0;
    /**
     * View类型与View类之间的映射关系，采用SparseArray有内存性能提升，但是有访问效率的降低.
     * */
    private static final SparseArray<Class<? extends AbsBasicView>> VIEW_CLASS_MAP = 
            new SparseArray<Class<? extends AbsBasicView>>();
    
    
    /**
     * View与View数据之间的映射，保证当传递的数据bean类与给定的ViewType不一致时，直接抛出异常.
     * */
    private static final Map<Class<? extends AbsBasicView>, Class<? extends BaseBean>> VIEW_DATA_CLASS_MAP =
            new HashMap<Class<? extends AbsBasicView>, Class<? extends BaseBean>>();
    
    /**
     * View类型与ViewType映射表
     * */
    private static final Map<Class<? extends AbsBasicView>, Integer> VIEW_TYPE_MAP = 
            new HashMap<Class<? extends AbsBasicView>, Integer>();
    
    public static void registerView(Class<? extends AbsBasicView> clazz, Class<? extends BaseBean> clazz1)
    {
        int viewType = viewTypeNumber++;
        VIEW_CLASS_MAP.put(viewType, clazz);
        VIEW_TYPE_MAP.put(clazz, viewType);
        VIEW_DATA_CLASS_MAP.put(clazz, clazz1);
    }
    
    public static int getViewTypeByClass(Class<? extends AbsBasicView> clazz)
    {
        return VIEW_TYPE_MAP.get(clazz);
    }
    
    
    public static int getViewTypeCount()
    {
        return VIEW_CLASS_MAP.size();
    }
    
    
    public static AbsBasicView createViewFromType(Context context, int viewType)
    {
        Class<? extends AbsBasicView> clazz = VIEW_CLASS_MAP.get(viewType);        
        AbsBasicView view = null;
        if(clazz != null && context != null)
        {
            try
            {
                Constructor<?> constructor = clazz.getConstructor(Context.class);
                view = (AbsBasicView) constructor.newInstance(context);
                view.viewType = viewType;

            }
            catch (NoSuchMethodException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InstantiationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        return view;
    }
    
    public static Class<? extends AbsBasicView> getViewClassByType(int type)
    {
        return VIEW_CLASS_MAP.get(type);
    }
    
    public static Class<? extends BaseBean> getBeanClassByViewClass(Class<? extends AbsBasicView> clazz)
    {
        return VIEW_DATA_CLASS_MAP.get(clazz);
    }
    
}
