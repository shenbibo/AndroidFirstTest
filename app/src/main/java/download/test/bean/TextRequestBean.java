package download.test.bean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月19日 下午6:52:23
 * @version
 * @since
 */
public class TextRequestBean extends RequestBean
{

    public final static String METHOD = "textRequestBean";
    /**
     * 
     */
    public TextRequestBean(String url)
    {
        this.url = url;
        method_ = METHOD;
        httpMethod = HttpMethod.GET;
    }
}
