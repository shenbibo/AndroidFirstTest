package download.test.bean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import download.test.utils.ViewFactory;
import download.test.view.AbsBasicView;
import download.test.viewbeans.BaseBean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月26日 下午3:48:26
 * @version
 * @since
 */
public class DataProvider<T>
{

    public interface OnDataChange
    {
        void notifyDataHaveChanged();
    }
    
    private List<ViewWrapper> data = new ArrayList<ViewWrapper>();
    
    private Context mContext;
    
    private OnDataChange changeListener;
    /**
     * 
     */
    public DataProvider(Context context)
    {
        mContext = context.getApplicationContext();
    }
    
    public void addData(BaseBean bean, int viewType)
    {
        AbsBasicView view = ViewFactory.createViewFromType(mContext, viewType);
        if(!(bean == null && view == null))
        {
            //先判断传入的数据的类型是否与viewType对应的bean一致，不一致则抛出RuntimeException.
            Class<?> clazz = ViewFactory.getBeanClassByViewClass(ViewFactory.getViewClassByType(viewType));
            if(bean.getClass() != clazz)
            {
                throw new RuntimeException("input bean's class is not right, the bean'class = " 
                        + bean.getClass() + "the viewType need bean's class = " + clazz);
            }
            ViewWrapper wrapper = new ViewWrapper(viewType, view, bean);
            synchronized(data)
            {
                data.add(wrapper);
            }
            
        }        
    }
    
    public int getDataSize()
    {
        return data.size();
    }
    
    
    public ViewWrapper getItemViewWrapper(int position)
    {
        return data.get(position);
    }
    
    public void setOnDataChangeListener(OnDataChange listener)
    {
        changeListener = listener;
    }
    
    public void notifyOnDataChanged()
    {
        if(changeListener != null)
        {
            changeListener.notifyDataHaveChanged();
        }
    }
    
   
    @SuppressWarnings("unchecked")
    public List<T> getDataList()
    {
        List<T> dataList = new ArrayList<T>();
        synchronized (data)
        {
            for (ViewWrapper wrapper : data)
            {
                dataList.add(((T)(wrapper.bean)));
            }
        }
        return dataList;        
    }
}
