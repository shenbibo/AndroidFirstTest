package download.test.bean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import download.test.utils.ReflectUtils;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月9日 下午8:09:10
 * @version
 * @since
 */
public class RequestBean
{

    /**
     * 请求的数据类型，如文件，图片等等。
     * */
    public interface RequestType
    {
        final int IMAGE_DATA = 0;
        
        /**
         * 下载文件
         * */
        final int FILE_DATA = 1;
        
        final int TEXT_DATA = 2;
        
        /**
         * 获取本地文件
         * */
        final int LOCAL_FILE_DATA = 3;
    }
    
    public interface HttpMethod
    {
        final String GET = "GET";
        
        final String POST = "POST";
        
    }
    
    public interface RequestDataType
    {
        /**
         * 首先从缓存获取
         * */
        public static final int REQUEST_CACHE = 0;
        
        /**
         * 直接从网络获取。
         * */
        public static final int REQUEST_NETWORK = 1; 
    }
    

    
    /**
     * 请求数据类型。
     * */
    public int requestDataType = RequestDataType.REQUEST_CACHE;
    
    
    private final static String DEFAULT_END_TAG = "_";
    /**
     * 调用的接口方法。
     * */
    public String method_;
    
    /**
     * 调用的服务器接口名称
     * */
    public String serverName;
    
    /**
     * 网络请求调用的方法
     * 
     * */
    public String httpMethod = HttpMethod.GET;
    /**
     * url地址。
     * */
    public String url;
    
    /**
     * 希望从网络获取的数据类型。
     * */
    public int requstType = RequestType.TEXT_DATA;
    
    /**
     * Object字段可以用于携带额外的参数。
     * */
    public Object obj;
    
    /**
     * 
     * 将以特定标记结尾的字段以key=value&key=value...的方式组合成body，
     * 该模式符合HTML中的application/x-www-form-urlencoded要求的编码方式，名称/值对的方式。
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * */
    public String generateRequestBody() throws IllegalAccessException, IllegalArgumentException
    {
        //以字段名称为未
        Map<String, Field> fields = getSelectedField(null);
        //将Key取出，并进行排序
        String[] keys = fields.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        
        StringBuilder sb = new StringBuilder(128);
        String value = "";
        
        //取值进行组合
        for (String name : keys)
        {
            value = getValue(fields.get(name));
            sb.append(name).append('=').append(value).append('&');
        }
        if(sb.length() > 0 && sb.charAt(sb.length() - 1) == '&')
        {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        return sb.toString();
    }
    
    private String getValue(Field field) throws IllegalAccessException, IllegalArgumentException
    {
        Object obj = null;
        String value = null;
        obj = field.get(this);
        if(obj instanceof JsonBean)
        {
            value = ((JsonBean)obj).toString();
        }
        else
        {
            value = String.valueOf(obj);
        }
        return value;
    }
    
    /**
     * 获取以给定标示结尾的字段
     * @param endTag 给定的结束标志符，如果为null，则使用{@link RequestBean#DEFAULT_END_TAG}
     * */
    protected Map<String, Field> getSelectedField(String endTag)
    {
        if(endTag == null)
        {
            endTag = DEFAULT_END_TAG;
        }
        //字段名, field组成的的map
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        Field[] fields = ReflectUtils.getDeclaredFields(this.getClass());
        String fieldName = "";
        for (Field field : fields)
        {
            field.setAccessible(true);
            fieldName = field.getName();
            if(field.getName().endsWith(endTag))
            {
                fieldName = fieldName.substring(0, fieldName.length() - endTag.length());
                fieldMap.put(fieldName, field);
            }
        }
        return fieldMap;
    
    }
    
    /**
     * 设置属性的值
     * */
    protected void setValues()
    {
        
    }
}
