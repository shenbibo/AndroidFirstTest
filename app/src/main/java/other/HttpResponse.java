package other;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;


/**
 * 
 * 
 * @author sWX284798
 * @date 2015年10月14日 下午4:59:39
 * @version
 * @since
 */
public class HttpResponse
{
    
    @SuppressWarnings("deprecation")
    public static void Test()
    {
        org.apache.http.HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");       
        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");               
        response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
        HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));
        while (it.hasNext()) 
        {
            HeaderElement elem = it.nextElement();
            System.out.println(elem.getName() + " = " + elem.getValue());
            NameValuePair[] params = elem.getParameters();
            for (int i = 0; i < params.length; i++) 
            {
                System.out.println(" " + params[i]);
            }
        }
    }
    
    public static class TestElement 
    { 
        private int num; 
        public TestElement(final int num) 
        { this.num = num; } 
        
        @Override public int hashCode() 
        { return 1; } 
        
        @Override public boolean equals(final Object obj)
        { 
          if (this == obj) 
          { 
              return true; 
          } 
          if (obj == null) 
          { 
              return false; 
          } 
          if (getClass() != obj.getClass()) 
          { 
              return false; 
          } 
          final TestElement other = (TestElement) obj; 
          if (num != other.num) 
          { 
              return false; 
          } 
          return true; 
          } 
        
        public static void main1(final String[] args) 
        { 
            Set<TestElement> set = new HashSet<TestElement>(); 
            set.add(new TestElement(1)); 
            set.add(new TestElement(1)); 
            set.add(new TestElement(2)); 
            set.add(new TestElement(3)); 
            System.out.println(set.size()); 
        }
     } 
    
    public static class test331 extends TestElement
    {

        /**
         * @param num
         */
        public test331(int num)
        {
            super(num);
            
        }
        
//        @Override
//        public static void main1(final String[] args)
//        {
//            
//        }
    }
        


    public static void main(String[] args)
    {
        Test();
//        int x = 3 ;
//        int y =(x++)+(x++)+(++x);
//        System.out.println(y);        

    }
    
    public static int testTryCatch()
    {
        int i = 3;
        try
        {
            int[] y={12,35,8,7,2};
            Arrays.sort(y);
            for (int j : y)
            {
                System.out.print(j);
            }
            int x = i / 0;
        }
        catch (Throwable e)
        {
            // TODO: handle exception
            return i;
        }
        finally
        {
            i++;
        }
        return i;
    }
    
    public static class Foo { 
        public static void main123 (String [] args) 
        { 
            StringBuffer a = new StringBuffer ("A"); 
            StringBuffer b = new StringBuffer ("B"); 
            operate (a,b); 
            System.out.println(a + "," +b); 
        } 
        static void operate (StringBuffer x, StringBuffer y)
        { 
            x.append(y); 
            y = x; 
        } 
    }
    

    
}
