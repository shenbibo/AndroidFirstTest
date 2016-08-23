package download.test.bean;

/**
 * 
 * 错误原因
 * @author sWX284798
 * @date 2015年11月13日 上午11:36:18
 * @version
 * @since
 */
public interface ErrorCause
{
    /**
     * 正常
     * */
    public static final int NORMAL = 0;

    /**
     * 无网络
     * */
    public static final int NO_NETWORK = 1;
    
    /**
     * JSON 解析错误
     * */
    public static final int JSON_ERROR = 2;
    
    /**
     * 网络连接错误
     * */
    public static final int NETWORK_CONNECT_ERROR = 3;
    
    /**
     * IO异常
     * */
    public static final int IO_ERROR = 4;
    
    /**
     * 参数错误
     * */
    public static final int PARA_ERROR = 5;
    
    /**
     * 未知错误
     * */
    public static final int UNKNOW_ERROR = 6;
}
