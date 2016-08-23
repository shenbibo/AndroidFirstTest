package download.test.utils;

import download.test.bean.DownloadFileInfo;
import download.test.view.DownloadListItemView;
import download.test.view.ProgressView;
import download.test.viewbeans.ProgressBean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月26日 下午8:33:27
 * @version
 * @since
 */
public class ViewResgiterUtils
{
    public static void init()
    {
        ViewFactory.registerView(DownloadListItemView.class, DownloadFileInfo.class);
        ViewFactory.registerView(ProgressView.class, ProgressBean.class);
    }

}
