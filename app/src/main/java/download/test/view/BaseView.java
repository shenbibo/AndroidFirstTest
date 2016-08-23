package download.test.view;

import com.example.androidfirsttest.R;

import download.test.viewbeans.BaseBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月25日 上午11:30:34
 * @version
 * @since
 */
public class   BaseView extends AbsBasicView
{

    
    /**
     * 
     */
    public BaseView(Context context)
    {
        mContext = context;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ViewGroup fillChildView(ViewGroup rootView)
    {
        if(rootView == null)
        {
            rootView = (ViewGroup) loadContainer(LayoutInflater.from(mContext), null);
        }
        containerView = rootView;
        return rootView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnClickListener(final ViewEventListener listener)
    {
        if(containerView != null && listener != null)
        {
            containerView.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    listener.onClick();
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(BaseBean bean)
    {
        this.baseBean = bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View loadContainer(LayoutInflater inflater, ViewGroup root)
    {
        return inflater.inflate(R.layout.list_container, root);
    }

}
