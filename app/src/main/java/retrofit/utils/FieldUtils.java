package retrofit.utils;

import com.sky.slog.Slog;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import retrofit.HttpUtils;
import retrofit.RequestBean;

/**
 * 字段获取工具类
 * Created by sky on 2017/7/19.
 */
public final class FieldUtils {

    /**
     * 从指定的对象中遍历以指定注解标识的字段，以名称-值得方式返回
     * */
    public static Map<String, String> parseFields(RequestBean requestBean, Class<? extends Annotation> annotationClazz) {
        Map<String, String> map = new HashMap<>();
        List<Field> fields = getField(requestBean.getClass(), annotationClazz);
        Object value;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                value = field.get(requestBean);
                if (value == null) {
                    continue;
                }
                Slog.t(HttpUtils.TAG).i("name = %s, value = %s", field.getName(), String.valueOf(value));
                map.put(field.getName(), String.valueOf(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private static List<Field> getField(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(getFieldWithAnnotation(clazz, annotationClazz));
        Class<?>[] classes = clazz.getInterfaces();

        // 递归获取其超类申明的字段
        if (classes.length != 0) {
            for (Class<?> tempClazz : classes) {
                fields.addAll(getField(tempClazz, annotationClazz));
            }
        }

        return fields;
    }

    private static List<Field> getFieldWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> annotationFields = new ArrayList<>();

        for (Field field : fields) {
            if (field.getAnnotation(annotationClazz) != null) {
                annotationFields.add(field);
            }
        }

        return annotationFields;
    }
}
