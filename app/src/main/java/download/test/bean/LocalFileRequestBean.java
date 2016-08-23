package download.test.bean;

/**
 * 获取本地文件测试
 * 
 * @author sWX284798
 * @date 2015年11月23日 下午2:18:39
 * @version
 * @since
 */
public class LocalFileRequestBean extends RequestBean
{

    public static final String METHOD = "getLocakFile";
    
    /**
     * 
     */
    public LocalFileRequestBean(String url)
    {
        super();
        this.url = url;
        method_ = METHOD;
    }
    
}
