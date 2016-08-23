package download.test.utils;

import java.util.HashMap;
import java.util.Map;

import download.test.bean.ResponseBean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月13日 上午10:35:58
 * @version
 * @since
 */
public class ServerMessage
{
    /**
     * 用于存储接口方法与返回数据对象的Map
     * key: requestBean.method_
     * value:ResponseBean
     * */
    private static final Map<String, Class<?>> responseMap = new HashMap<String, Class<?>>();
    
    public static void registerResponseBean(String methodName, Class<?> clazz)
    {
        responseMap.put(methodName, clazz);
    }
    
    public static ResponseBean createResponseBeanObj(String methodName) throws InstantiationException, IllegalAccessException
    {
        Class<?> clazz = responseMap.get(methodName);
        if(clazz == null)
        {
            throw new InstantiationException("class not found , method = " + methodName);
           
        }
        return (ResponseBean) clazz.newInstance();
    }

}
