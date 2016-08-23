package download.test.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 通过反射获取字段。
 * @author sWX284798
 * @date 2015年11月10日 下午2:20:53
 * @version
 * @since
 */
public class ReflectUtils
{
    /**
     * 获取所有的字段，包括私有的字段。
     * */
    public static Field[] getDeclaredFields(Class<?> clazz)
    {
        if(clazz == null)
        {
            return new Field[0];
        }
        Field[] curClassFields = new Field[0];
        Field[] superClassFields = new Field[0];
        //递归的方式获取超类的字段
        if(clazz.getSuperclass() != null)
        {
            superClassFields = getDeclaredFields(clazz.getSuperclass());
        }
        curClassFields = clazz.getDeclaredFields();
        //将连个数组合并
        Field[] fields = new Field[curClassFields.length + superClassFields.length];
        System.arraycopy(curClassFields, 0, fields, 0, curClassFields.length);
        System.arraycopy(superClassFields, 0, fields, curClassFields.length, superClassFields.length);
        
        // 过滤掉 shadow$_klass_、shadow$_monitor_ 等无关的系统属性
        List<Field> fieldList = new ArrayList<Field>();
        for (Field field : fields)
        {
            if(field.getName().indexOf("$") < 0)
            {
                fieldList.add(field);
            }
        }
        if(fieldList.size() != fields.length)
        {
            fields = fieldList.toArray(new Field[0]);
        }
        return fields;
    }
    
    /**
     * 获取Map, List接口的参数类型
     * */
    public static Class<?> getMapListGenericType(Field field)
    {
        Class<?> clazz = null;
        if(Map.class.isAssignableFrom(field.getType()))
        {
            clazz = getType(field, 1);
        }
        else if(List.class.isAssignableFrom(field.getType()))
        {
            clazz = getType(field, 0);
        }        
        return clazz;
    }
    
    private static Class<?> getType(Field field, int offset)
    {
        Class<?> clazz = null;
        if(field != null)
        {
            Type type = field.getGenericType();
            //参数化类型
            if(type instanceof ParameterizedType)
            {
                ParameterizedType pType = (ParameterizedType)type;
                Type[] types = pType.getActualTypeArguments();
                if(types.length > offset)
                {
                    Type tempType = types[offset];
                    if(tempType instanceof Class)
                    {
                        clazz = (Class<?>) tempType;
                    }
                    //获取type.toString()中携带的类名构建的Class对象
                    else
                    {
                        String str = tempType.toString();
                        int end = -1;
                        //默认情况下toString返回的字符串是：
                        //getClass().getName() + '@' + Integer.toHexString(hashCode())

                        if((end = str.indexOf('@')) > 0)
                        {
                            str = str.substring(0, end);
                            try
                            {
                                clazz = Class.forName(str);
                            }
                            catch (ClassNotFoundException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return clazz;
    }

}
