package download.test.bean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月19日 上午11:48:16
 * @version
 * @since
 */
public class ImageRequestBean extends RequestBean
{
    
    public final static String METHOD = "requestImage";

    /**
     * 
     */
    public ImageRequestBean(String url)
    {
        this.url = url;
        requstType = RequestType.IMAGE_DATA;
        serverName = "";
        method_ = "requestImage";
    }
}
