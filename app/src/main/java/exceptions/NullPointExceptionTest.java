/*
 * 文 件 名:  NullPointExceptionTest.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月31日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package exceptions;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2015年12月31日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NullPointExceptionTest
{

    public class Student
    {
        int age;
        String name;
    }
    
    
    public static void test(Student s)
    {
        s.age = 18;
        s.name = "1234";
    }
    
    public static void main(String[] args)
    {
        Student s = null;
        test(s);
                
    }
}
