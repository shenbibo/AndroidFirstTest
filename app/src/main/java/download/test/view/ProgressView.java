package download.test.view;

import com.example.androidfirsttest.R;

import download.test.viewbeans.BaseBean;
import download.test.viewbeans.ProgressBean;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月27日 上午11:42:24
 * @version
 * @since
 */
public class ProgressView extends BaseView
{
    private ProgressBar pBar;
    
    /**
     * 
     */
    public ProgressView(Context context)
    {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewGroup fillChildView(ViewGroup rootView)
    {
        rootView = super.fillChildView(rootView);
        View view = LayoutInflater.from(mContext).inflate(R.layout.progress_item_layout, null);
        pBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        pBar.setMax(100);
        rootView.addView(view);
        return rootView;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public View loadContainer(LayoutInflater inflater, ViewGroup root)
    {
        
        return inflater.inflate(R.layout.progress_container, root);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(BaseBean bean)
    {
        
        super.setData(bean);
        ProgressBean pBean = (ProgressBean) bean;
        pBar.setProgress(pBean.progress);
        pBar.setSecondaryProgress(pBean.secondaryProgress);
    }
    
    
}
