package rxjava;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import com.sky.slog.Slog;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.NewThreadWorker;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


/**
 * 过滤器类别的操作符测试
 * Created by sky on 2017/6/23.
 */
@RunWith(AndroidJUnit4.class)
public class RxJavaFilterTest {
    public static final String TAG = RxJavaFilterTest.class.getSimpleName();

    /**
     * 过滤符合条件的操作符测试
     */
    @Test
    public void filterOperatorTest() {
        Integer[] arrays = new Integer[]{1, 2, 3, 4, 5, 6};
        Observable.fromArray(arrays)
                  .filter(integer -> integer > 2)
                  .subscribe(value -> Slog.t(TAG).i("value = %d", value));
    }

    /**
     * 从原始的数据源中发射前n个数据
     */
    @Test
    public void takeOperatorTest() {
        Integer[] arrays = new Integer[]{1, 2, 3, 4, 5, 6};
        Observable.fromArray(arrays)
                  .take(3)
                  .subscribe(value -> Slog.t(TAG).i("value = %d", value));


        // take(long, TimeUnit) 在给定时间内才能发射事件，一旦超过停止发射
        Observable.fromArray(arrays)
                  .take(100, TimeUnit.NANOSECONDS)     // 100纳秒内能够发射的事件
                  .subscribe(value -> Slog.t(TAG).i("value = %d", value));
    }

    /**
     * 发射事件序列中的后多少个事件，注意顺序是正的的，而不是从末尾开始取第一个
     * TODO 疑问Rxjava 中竟然没有从末尾逆序返回元素的操作符？？？？？
     */
    @Test
    public void takeLastOperatorTest() {
        Integer[] arrays = new Integer[]{1, 2, 3, 4, 5, 6, 66, 65, 56, 878, 45, 232, 76, 9, 780, 3435, 23, 5, 2, 2, 35, 43, 679, 78};
        Observable.fromArray(arrays)  // 从末尾取三个元素并且限定在10纳秒类，在IO线程执行，出错不推迟抛出，每次取事件到缓存的大小为5
                  .takeLast(10, 10, TimeUnit.NANOSECONDS, Schedulers.io(), false, 5)
                  .subscribe(value -> Slog.t(TAG).i("value = %d", value));

//        Observable.fromArray(arrays).
    }

    /**
     * takeUntil(Observable)订阅并开始发射原始Observable，同时监视我们提供的第二个Observable。
     * 如果第二个Observable发射了一项数据或者发射了一个终止通知，takeUntil()返回的Observable会停止发射原始Observable并终止。
     * 或者是符合终止条件的事件发生了就停止整个事件。
     * <p>
     * <strong>注意事件正常终止onComplete，onError方法不会被调用</strong>
     */
    @Test
    public void takeUntilOperatorTest() {
        Observable<Long> observable = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS);

        Observable.interval(10, TimeUnit.MILLISECONDS)
                  .takeUntil(observable)
                  .subscribe(new Observer<Long>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(Long aLong) {
                          Slog.t(TAG).i("LONG = " + aLong);
                      }

                      @Override
                      public void onError(Throwable e) {
                          Slog.t(TAG).i("takeUntilTest onError called");  // 不会被调用
                      }

                      @Override
                      public void onComplete() {
                          Slog.t(TAG).i("takeUntilTest onComplete called");  // 不会被调用
                      }
                  });
    }

    /**
     * 忽略事件序列中的前N个事件(或指定时间内的事件) / 忽略事件序列中的后N个事件
     */
    @Test
    public void skipAndSkipLastTest() {
        Integer[] arrays = new Integer[]{1, 2, 3, 4, 5, 6, 66, 65, 56, 878, 45, 232, 76, 9, 780, 3435, 23, 5, 28};

        // 其实现方式是缓存最近的事件结果，如果在时间窗口到达后还有新的事件，则发送出去，否则就直接自己处理掉。
        // skipLast(10, TimeUnit.MICROSECONDS)

        // 跳过前5个事件和最后10微秒的事件
        Observable.fromArray(arrays).skip(5)
                  .skipLast(10, TimeUnit.MICROSECONDS)
                  .subscribe(value -> Slog.t(TAG).i("value = %d", value));
    }

    /**
     * 获取事件序列中的某个事件作为唯一事件发送出去
     */
    @Test
    public void elementAtTest() {
        Integer[] arrays = new Integer[]{1, 2, 3, 4, 5, 6, 66, 65, 56, 878, 45, 232, 76, 9, 780, 3435, 23, 5, 28};

        Observable.fromArray(arrays)
                  .elementAt(5)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));
    }

    /**
     * debounce(long, TimeUnit)过滤掉了由Observable发射的速率过快的数据；如果在一个指定的时间间隔过去了仍旧没有发射一个，那么它将发射最后的那个。
     * <p>
     * <p>
     * <strong>注意如果源事件发送的速率一直大于设置的去除重复的时间间隔，则不会有任何事件发送到下游</strong>
     */
    @Test
    public void debounceTest() {
        Observable<Long> observable = Observable.interval(100, TimeUnit.MILLISECONDS);

//        observable.take(15)
//                  .subscribe(value -> Slog.t(TAG).i("value = " + value));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 300 MS 只允许发射一次数据
        observable.debounce(30, TimeUnit.MILLISECONDS).take(8)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));

        // 前面800ms只允许发射一个事件，后面取总共10个事件
        observable.debounce(time -> Observable.interval(50, 800, TimeUnit.MILLISECONDS).take(1))
                  .take(10)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value), e -> {}, countDownLatch::countDown);

//        SystemClock.sleep(3000);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * distinct()的过滤规则是只允许还没有发射过的数据通过，所有重复的数据项都只会发射一次。
     * 可以通过参数指定不同的key, 每个key为一类数据，每一类数据不可以重复，同时我们还可以使用Collection来指定哪些事件是可以发射的
     */
    @Test
    public void distinctOperatorTest() {
        // 去除骑宠重复的数，其内部其实调用了HashSet()
        Integer[] arrays = new Integer[]{1, 2, 2, 4, 5, 6, 6, 65, 56, 878, 45, 45, 76, 9, 780, 23, 5, 780};
        Observable.fromArray(arrays)
                  .distinct()
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));


        // @param keySelector    // 指定的特定的key， 这里的key在原始数据的基础上转化为任何的类型
        // @param collectionSupplier   // 将前面拿到的key 使用这里返回的Collection的add进行添加操作，如果可以添加成功，则该key对应的事件会被发射，否则不会
        Observable.fromArray(arrays)
                  .distinct(integer -> "This is" + integer, ArrayList::new)  // 这里使用ArrayList() 其add 不具有去重的效果，所以所有的事件都会发射
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));
    }

    /**
     * distinctUntilChanged()和distinct()类似，只不过它判定的是Observable发射的当前数据项和前一个数据项是否相同。
     *
     * */
    @Test
    public void distinctUntilChangedTest(){
        Integer[] arrays = new Integer[]{1, 2, 2, 4, 5, 6, 6, 65, 56, 878, 45, 45, 76, 9, 780, 23, 5, 780};

        Slog.t(TAG).i("distinctUntilChangedTes 1");
        // 只比较和前一个是否相等或者key是否相同，相同则被抛弃掉
        Observable.fromArray(arrays)
                  .distinctUntilChanged()
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));


        Slog.t(TAG).i("distinctUntilChangedTes 2");
        // 这里小于50则认为是同一个事件
        Observable.fromArray(arrays)
                .distinctUntilChanged(integer -> {
                    if(integer < 50){
                        return 1;
                    }else {
                        return 2;
                    }})
                .subscribe(value -> Slog.t(TAG).i("value = " + value));

        Slog.t(TAG).i("distinctUntilChangedTes 3");
        // 这里前一个和后一个相减若小于20 则认为相等
        Observable.fromArray(arrays)
                  .distinctUntilChanged((integer1, integer2) -> Math.abs(integer2 - integer1) <= 20)
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));
    }

    /**
     * first()顾名思义，它是的Observable只发送观测序列中的第一个数据项。
     * */
    @Test
    public void firstOperatorTest(){
        Integer[] arrays = new Integer[]{1, 2, 2, 4, 5, 6, 6, 65, 56, 878, 45, 45, 76, 9, 780, 23, 5, 780};
        Integer[] arrayEmpty = new Integer[0];

        Observable.fromArray(arrays)
                  .firstElement()
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));

        Observable.fromArray(arrayEmpty)
                  .firstOrError()
                  .subscribe(value -> Slog.t(TAG).i("value = " + value), e -> Slog.t(TAG).e(e, "this is empty, so throw error"));
    }

    /**
     * last()只发射观测序列中的最后一个数据项。
     * */
    @Test
    public void lastOperatorTest(){
        Integer[] arrays = new Integer[]{1, 2, 2, 4, 5, 6, 6, 65, 56, 878, 45, 45, 76, 9, 780, 23, 5, 780};
        Integer[] arrayEmpty = new Integer[0];

        Observable.fromArray(arrays)
                  .lastElement()
                  .subscribe(value -> Slog.t(TAG).i("value = " + value));

        Observable.fromArray(arrayEmpty)
                  .last(1000)
                  .subscribe(value -> Slog.t(TAG).i("empty arrays ,return default value = " + value));
    }
}
