package com.entenstest;

import android.util.Log;


/**
 * 测试子类定义与父类相同的字段，哪个类的该字段会被覆盖。
 * 
 * @author sWX284798
 * @date 2015年9月10日 下午2:31:17
 * @version
 * @since
 */
public class ClassExtendsTest
{
    private static final String TAG = "ClassExtendsTest";
    
    static class FatherClass
    {
        public int testValue = 1;
        
        public String testStr = "FatherClass";
    }
    
    static class SonClass extends FatherClass
    {
        public long testValue = 2L;
        
        public String testStr = "SonClass";
    }
    
    
    
    //打印的日志为：
    //09-10 14:44:00.645: D/ClassExtendsTest(8433): FatherClass class1 = new SonClass();
    //09-10 14:44:00.645: D/ClassExtendsTest(8433): testValue = 1, testStr = FatherClass
    //09-10 14:44:00.645: D/ClassExtendsTest(8433): SonClass class2 = new SonClass();
    //09-10 14:44:00.645: D/ClassExtendsTest(8433): testValue = 2, testStr = SonClass
    //结论：出现如上接口，是在多态的情况下， FatherClass对象调用的属性字段只能是子类与其共有的，
    //对于父类对象而言并不知道子类也定义了相同类型、相同类型的该字段。
    //当将子类的实例赋值给子类的对象时，父类的相同名称字段会被子类的覆盖，不管他们的类型是否相同。
    public static void testClass()
    {
        Log.d(TAG, "FatherClass class1 = new SonClass();");
        FatherClass class1 = new SonClass();        
        Log.d(TAG, "testValue = " + class1.testValue + ", testStr = " + class1.testStr);
        
        Log.d(TAG, "SonClass class2 = new SonClass();");
        SonClass class2 = new SonClass();        
        Log.d(TAG, "testValue = " + class2.testValue + ", testStr = " + class2.testStr);
        
        
    }
}
