import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/6/7
 * @since
 */
@RunWith(JUnit4.class)
public class BinarySearchTestSuit {

    @Test
    public void binarySearch() {
//        List<Integer> integerList = new ArrayList<>();
//        integerList.add(1);
//        integerList.add(2);
//        integerList.add(3);
//        integerList.add(5);
//        integerList.add(6);
//        integerList.add(7);
//
//        // 查找一个比所有数都大的数，返回（-6 - 1） = -7
//        System.out.println(Collections.binarySearch(integerList, 8));
//
//        // 查找一个指定的元素的存在的元素
//        System.out.println(Collections.binarySearch(integerList, 3));
//
//        // 查找一个不存在的元素，返回其应该插入的位置-1的负值（-index -1 ），即 （-3 -1） = -4
//        System.out.println(Collections.binarySearch(integerList, 4));
//
//        // 将null转为其他对象是没有问题的
//        Object object = null;
//        String ss = (String) object;
//        if(ss == null){
//            System.out.println("ss is null");
//        }
        String s = "123";
        System.out.println("s is" + s.substring(0,0));
    }

    public static class IntegerComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }
}
