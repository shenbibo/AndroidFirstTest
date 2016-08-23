package download.test.bean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月13日 上午11:47:32
 * @version
 * @since
 */
public interface ResponseCode
{

    /**
     * 响应成功
     * */
    public static final int OK = 0;
    
    /**
     * 响应错误*/
    public static final int ERROR = 1;
    
    /**
     * 解析错误
     * */
    public static final int PARSE_ERROR = 2;
    
    /**
     * 网络错误
     * 
     * */
    public static final int NETWORK_ERROR = 3;
    
    /**
     * 超时
     * */
    public static final int TIME_OUT = 4;
}
