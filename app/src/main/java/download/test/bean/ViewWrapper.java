package download.test.bean;

import download.test.view.AbsBasicView;
import download.test.viewbeans.BaseBean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月26日 下午3:58:37
 * @version
 * @since
 */
public class ViewWrapper
{
    public int viewType;
    
    public AbsBasicView view;
    
    public BaseBean bean;
    
    /**
     * 
     */
    public ViewWrapper(int viewType, AbsBasicView view, BaseBean bean)
    {
        this.view = view;
        this.viewType = viewType;
        this.bean = bean;
    }
        
}
