package download.test.control;

import download.test.bean.DataProvider;
import download.test.bean.DataProvider.OnDataChange;
import download.test.utils.ViewFactory;
import download.test.view.AbsBasicView;
import download.test.view.AbsBasicView.ViewEventListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月24日 下午3:01:39
 * @version
 * @since
 */
public class ListAdapter extends BaseAdapter implements OnDataChange
{
    
    private DataProvider provider;
    
    private Context mContext;
    
    private LayoutInflater inflater;
    
    private ViewEventListener listener;
    /**
     * 
     * 
     * @param     
     * @return void    
     * @throws
     */
    public ListAdapter(Context context, DataProvider provider)
    {
        this.provider = provider;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }
    
    
    public void setViewEventListener(ViewEventListener listener)
    {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount()
    {
        
        return provider.getDataSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int position)
    {
        
        return provider.getItemViewWrapper(position).bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position)
    {
        
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getViewTypeCount()
    {
        
        return ViewFactory.getViewTypeCount();
    }
    
    public int getItemViewType(int position) 
    {
        return provider.getItemViewWrapper(position).viewType;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        if(convertView == null)
        {
            convertView = createConvertView(position, parent);
        }
        AbsBasicView view = (AbsBasicView) convertView.getTag();
        view.setData(provider.getItemViewWrapper(position).bean);
        return convertView;
    }

    
    private View createConvertView(int position, ViewGroup parent)
    {
        AbsBasicView view = provider.getItemViewWrapper(position).view;
        if(view == null)
        {
            return new View(mContext);
        }
        //创建容器，并将数据填充到容器中
        ViewGroup covertView = (ViewGroup) view.loadContainer(inflater, null);
        covertView = view.fillChildView(covertView);
        if(covertView != null)
        {
            covertView.setTag(view);
            view.setOnClickListener(listener);
        }
        return covertView;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataHaveChanged()
    {
        this.notifyDataSetChanged();
        
    }
}
