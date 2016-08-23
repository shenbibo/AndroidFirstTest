package download.test.utils;

import download.test.NetworkTask;
import download.test.bean.RequestBean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月19日 下午2:08:05
 * @version
 * @since
 */
public class ServerAgent
{
    
    /**
     * 调用服务接口请求网络数据。
     * */
    public static void invokeServer(RequestBean request)
    {
        NetworkTask task = new NetworkTask(request);
        task.excute(NetworkUtils.IMAGE_EXECUTOR);
    }

}
