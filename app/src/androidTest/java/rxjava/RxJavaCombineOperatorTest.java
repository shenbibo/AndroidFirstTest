package rxjava;

import android.os.SystemClock;

import com.sky.slog.Slog;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * 组合类操作符测试代码
 * Created by sky on 2017/6/25.
 */
public class RxJavaCombineOperatorTest {
    public static final String TAG = RxJavaCombineOperatorTest.class.getSimpleName();

    /**
     * merge(Observable, Observable)将两个Observable发射的事件序列组合并成一个事件序列，
     * 就像是一个Observable发射的一样。你可以简单的将它理解为两个Obsrvable合并成了一个Observable，合并后的数据是无序的
     * <p>
     * <p>
     * 有很多的重载方法，支持数组，迭代器，可以限制最大并发数等等。
     */
    @Test
    public void mergeOperatorTest() {
        String[] strings = new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7"};

        Observable strObservable = Observable.interval(100, TimeUnit.MILLISECONDS)
                                             .take(7)
                                             .map(integer -> strings[integer.intValue()]);

        Observable interObservable = Observable.interval(50, TimeUnit.MILLISECONDS).take(10);


        CountDownLatch countDownLatch = new CountDownLatch(1);
        //noinspection unchecked
        Observable.merge(strObservable, interObservable)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value),
                             e -> {},
                             countDownLatch::countDown);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * startWith(T)用于在源Observable发射的数据前插入数据。使用startWith(Iterable<T>)我们还可以在源Observable发射的数据前插入Iterable
     * <p>
     * <p>
     * 注意源事件的参数和要添加的事件必须是同一种类型
     */
    @Test
    public void startWithTest() {
        Integer[] arrays = new Integer[]{1, 2, 2, 4, 5, 6, 6, 65, 56, 878, 45, 45, 76, 9, 780, 23, 5, 780};
        Integer[] arrays2 = new Integer[]{789, 123, 124};

        Observable.fromArray(arrays)
                  .startWith(Observable.fromArray(arrays2))
                  .subscribe(value -> Slog.t(TAG).i(" value = " + value));
    }

    /**
     * concat用于将多个obserbavle发射的的数据进行合并发射，concat严格按照顺序发射数据，
     * 前一个Observable没发射玩是不会发射后一个Observable的数据的。
     * <p>
     * <p>
     * 注意和merge的区别就是有有序的
     */
    @Test
    public void concatOperatorTest() {
        String[] strings = new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7"};

        Observable strObservable = Observable.interval(100, TimeUnit.MILLISECONDS)
                                             .take(7)
                                             .map(integer -> strings[integer.intValue()]);

        Observable interObservable = Observable.interval(50, TimeUnit.MILLISECONDS).take(10);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        //noinspection unchecked
        Observable.concat(strObservable, interObservable)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value),
                             e -> {},
                             countDownLatch::countDown);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * zip(Observable, Observable, Func2)用来合并两个Observable发射的数据项，
     * 根据Func2函数生成一个新的值并发射出去。当其中一个Observable发送数据结束或者出现异常后，另一个Observable也将停在发射数据。
     * <br> 在其重载版本中是可以支持iterable的数据源的
     * <p>
     * 比如一个事件源是3个事件 后一个是4个事件，那么他们会两两组队，发射完第三个事件对之后就不再发送了。
     */
    @Test
    public void zipOperatorTest() {
        String[] strings = new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7"};
        Integer[] arrays = new Integer[]{1, 2, 2, 4, 5, 6, 6, 65, 56, 878, 45, 45, 76, 9, 780, 23, 5, 780};

        // 只会发射前7个事件
        Observable.zip(Observable.fromArray(strings), Observable.fromArray(arrays), (str, integer) -> str + " is " + integer)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));

    }

    /**
     * comnineLatest(Observable, Observable, Func2)用于将两个Observale最近发射的数据已经Func2函数的规则进展组合。
     * <p>
     * <p>
     * 注意 它不同于`zip`的两两一一组合的方式， 它只会取两个事件源最近的两个，而且会在最后一个事件源终止之后才会结束。
     */
    @Test
    public void comnineLatestTest() {
        String[] strings = new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7"};

        Observable.combineLatest(Observable.interval(100, TimeUnit.MILLISECONDS)
                                           .take(7)
                                           .map(integer -> strings[integer.intValue()]),
                                 Observable.interval(150, TimeUnit.MILLISECONDS)
                                           .take(15),
                                 (str, integer) -> str + " is " + integer)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));

        SystemClock.sleep(4000);
    }

    /**
     * switchOnNext(Observable<? extends Observable<? extends T>>
     * 用来将一个发射多个小Observable的源Observable转化为一个Observable，
     * 然后发射这多个小Observable所发射的数据。如果一个小的Observable正在发射数据的时候，
     * 源Observable又发射出一个新的小Observable，则前一个Observable发射的数据会被抛弃，直接发射新的小Observable所发射的数据。
     */
    @Test
    public void switchOnNextTest() {
        final Integer[] arrays = new Integer[]{0, 1, 2, 3, 4, 5};
        final Integer[] array1 = new Integer[]{11, 12, 13, 14, 15};
        final Integer[] array2 = new Integer[]{21, 22, 23, 24, 25};
        final Integer[] array3 = new Integer[]{31, 32, 33, 34, 35};
        final Integer[] array4 = new Integer[]{41, 42, 43, 44, 45};
        final Integer[] array5 = new Integer[]{51, 52, 53, 54, 55};

        final Integer[][] array22 = new Integer[][]{arrays, array1, array2, array3, array4, array5};


        // 每次延时50ms, 因为每个数组的数据发送时都会延时50ms，所有最终打印出来的只会是 array5的值，因为它在最后，每次都会取消掉其前面的值
        // 逻辑是这样的 array1 取消 arrays, array2 取消 array1， 依次类推
        Observable<Observable<Integer>> source = Observable.fromArray(arrays)
                                                           .map(integer -> Observable.fromArray(array22[integer])
                                                                                     .map(integer1 -> {
                                                                                         SystemClock.sleep(50);
                                                                                         return integer1;
                                                                                     })
                                                                                     .subscribeOn(Schedulers.newThread()));


        Observable.switchOnNext(source)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));

        SystemClock.sleep(5000);
    }

    /**
     * observable1.join(Observable2, Func1, Func1, Func2)
     * 其参数可以这样理解，
     * 第一个Func1 observable1每发射一个事件A，其对对应的 Func1方法就会被执行，在其返回Observable对象的的事件没有完成前，A事件我们称它还在可用范围内。
     * <p>
     * 第2个Func1 observable2每发射一个事件B，其对对应的 Func2方法就会被执行，在其返回Observable对象的事件没有完成前，B事件我们称它还在可用范围内。
     * <p>
     * 当A和B同时有效时，此时就会调用Func2，其返回值就是A事件和B事件的组合结果
     * <p>
     * 注意这里关注的重点是事件的有效期，如果observable1和observable2在同一个时间段各自都有多个事件在有效期那他们各自都会两两组合，
     * 可以认为是数学中的组合。
     *
     * 另外如果无法构成组合的事件是不会发送出来的。
     */
    @Test
    public void joinOperatorTest() {

        // 因为interval 和 Timer内部使用的都是Schedulers.computation()线程进行计时，
        // 所以当过多时会导致computation()线程全部被用完可能导致线程阻塞，计时不准确
        // Schedulers.computation() 的线程池的数目是有限度的，这里正好会导致longValue2对应Observable.timer需要唤醒时没有线程可以用
        // 导致会晚50ms，所以这里给interval都指定了不同的线程Schedulers.newThread()
        // 以上解释不准确
        Observable<Long> observable1 = Observable.interval(100, TimeUnit.MILLISECONDS, Schedulers.newThread())
                                                 .take(3);

        Observable<Long> observable2 = Observable.interval(50, TimeUnit.MILLISECONDS, Schedulers.newThread())
                                                 .take(6);

//        observable2.subscribe(value -> Slog.t(TAG).i("currentTime = %d, value = %d",
//                                                     System.currentTimeMillis(), value));

        // 进行组合，observable1,observable2每个时间的有效周期为150ms
        // 所以最终应该是部分observable2的事件组合不到observable1的事件 [0,4],[0, 5] 这两个组合就不会有
        observable1.join(observable2,
                         longValue -> Observable.timer(150, TimeUnit.MILLISECONDS),
                         longValue2 -> Observable.timer(150, TimeUnit.MILLISECONDS),
                         (longValue3, longValue4) -> "[" + longValue3 + ", " + longValue4 + "]")
                   .subscribe(value -> Slog.t(TAG).i("value = " + value));


        SystemClock.sleep(5000);
    }
}
