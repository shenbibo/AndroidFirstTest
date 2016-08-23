package download.test.bean;


/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月9日 下午8:10:31
 * @version
 * @since
 */
public class ResponseBean extends JsonBean
{
    
    /**
     * 表示响应完成服务器返回数据为完成。
     * */
    public static final int SERVER_RETURN_CODE_OK = 0;
    
    /**
     * 返回数据返回给业务层的最终结果。
     * */
    public int responseCode = ResponseCode.ERROR;
    
    /**
     * 服务器返回的响应Code。
     * */
    public int rturnCode_ = SERVER_RETURN_CODE_OK;
    
    /**
     * 响应错误原因
     * */
    public int errorCause = ErrorCause.NORMAL;
    
    /**
     * 响应获取的数据的来源。
     * */
    public interface ResponsedDataType
    {
        /**
         * 来自本地缓存
         * */
        public static final int FROM_CACHE = 0;
        
        /**
         * 来自网络返回的数据
         * */
        public static final int FROM_NETWORK = 1;
        
    }
    
}
