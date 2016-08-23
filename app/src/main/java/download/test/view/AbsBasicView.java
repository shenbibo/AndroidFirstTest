package download.test.view;

import com.example.androidfirsttest.R;

import download.test.viewbeans.BaseBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月24日 下午3:34:29
 * @version
 * @since
 */
public abstract class AbsBasicView
{
    public interface ViewEventListener
    {
        void onClick();
    }
    
    protected Context mContext;
    
    
    protected BaseBean baseBean;
    
    /**
     * VIEW的类型
     * */
    public int viewType = -1;
    
    /**
     * 外层的填充器view
     * */
    protected View containerView;
    
    public abstract View loadContainer(LayoutInflater inflater, ViewGroup root);

    
    /**
     * rootView 用于填充View的容器。
     * */
    public abstract ViewGroup fillChildView(ViewGroup rootView);
    
    
    public abstract void setOnClickListener(final ViewEventListener listener);
    
    /**
     * 设置绑定数据。
     * */
    public abstract void setData(BaseBean bean);


    
}
