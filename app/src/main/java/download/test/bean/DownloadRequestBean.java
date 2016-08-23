package download.test.bean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月23日 下午4:42:56
 * @version
 * @since
 */
public class DownloadRequestBean extends RequestBean
{

    public static final String METHOD = "downloadFile";
    
    /**
     * 
     */
    public DownloadRequestBean(String url)
    {
        this.url = url;
        method_ = METHOD;
    }
}
