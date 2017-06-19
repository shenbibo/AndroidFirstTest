package rxjava;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import com.sky.slog.Slog;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.R.id.list;


/**
 * [Rxjava转换操作符测试]
 * [详述类的功能。]
 * Created by sky on 2017/6/19.
 */
@RunWith(AndroidJUnit4.class)
public class RxJavaTransformTest {
    private static final String TAG = RxJavaTransformTest.class.getSimpleName();

    /**
     * 普通map转换原始数据转换成新的数据类型
     */
    @Test
    public void mapOperatorTest() {
        Observable<Integer> integerObservable = Observable.just(1, 2, 3, 4, 5);

        integerObservable.map(integer -> "This is a " + integer)
                         .subscribe(string -> Slog.t(TAG).i(string));

    }

    /**
     * flatMap 与map 不同，其Function返回的是一个降维拆分后的多个Observable对象，合并一起发送，但是并不保证顺序
     * flatMap 有非常多的不同参数的重载版本，因为不保证合并后的数据的顺序，所以其事件的发送是可以并发的进行的
     */
    @Test
    public void flatMapOperatorTest() {
        //        User[] users1 = new User[]{new User("1_sky", 123), new User("1_gavin", 456), new User("1_smith", 789)};
        //        User[] users2 = new User[]{new User("2_sky", 123), new User("2_gavin", 456), new User("2_smith", 789)};
        //        User[] users3 = new User[]{new User("3_sky", 123), new User("3_gavin", 456), new User("3_smith", 789)};
        //        User[] users4 = new User[]{new User("4_sky", 123), new User("4_gavin", 456), new User("4_smith", 789)};
        //
        //        User[][] allUsers = new User[][]{users1, users2, users3, users4};

        ArrayList<User> list1 = new ArrayList<>();
        ArrayList<User> list2 = new ArrayList<>();
        ArrayList<User> list3 = new ArrayList<>();
        ArrayList<User> list4 = new ArrayList<>();
        ArrayList<User> list5 = new ArrayList<>();
        ArrayList<User> list6 = new ArrayList<>();
        ArrayList<User> list7 = new ArrayList<>();
        ArrayList<User> list8 = new ArrayList<>();
        ArrayList<User> list9 = new ArrayList<>();
        ArrayList<User> list10 = new ArrayList<>();
        ArrayList<User> list11 = new ArrayList<>();
        ArrayList<User> list12 = new ArrayList<>();
        ArrayList<User> list13 = new ArrayList<>();
        ArrayList<User> list14 = new ArrayList<>();
        ArrayList<User> list15 = new ArrayList<>();
        ArrayList<User> list16 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(new User("1_sky_" + i, 123));
            list2.add(new User("2_sky_" + i, 123));
            list3.add(new User("3_sky_" + i, 123));
            list4.add(new User("4_sky_" + i, 123));
            list5.add(new User("5_sky_" + i, 123));
            list6.add(new User("6_sky_" + i, 123));
            list7.add(new User("7_sky_" + i, 123));
            list8.add(new User("8_sky_" + i, 123));
            list9.add(new User("9_sky_" + i, 123));
            list10.add(new User("10_sky_" + i, 123));
            list11.add(new User("11_sky_" + i, 123));
            list12.add(new User("12_sky_" + i, 123));
            list13.add(new User("13_sky_" + i, 123));
            list14.add(new User("14_sky_" + i, 123));
            list15.add(new User("15_sky_" + i, 123));
            list16.add(new User("16_sky_" + i, 123));
        }

        ArrayList<ArrayList<User>> allList = new ArrayList<>();
        allList.add(list1);
        allList.add(list2);
        allList.add(list3);
        allList.add(list4);
        allList.add(list5);
        allList.add(list6);
        allList.add(list7);
        allList.add(list8);
        allList.add(list9);
        allList.add(list10);
        allList.add(list11);
        allList.add(list12);
        allList.add(list13);
        allList.add(list14);
        allList.add(list15);
        allList.add(list16);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        // TODO 此处如果在 flatMap是否是交叉的取决于 Function.apply方法返回的Observable.subscribeOn方法是否不在同一个线程
        // 现在我希望在最终的观察者中接收的参数为单个user
        Observable.fromIterable(allList)
                  .flatMap(new Function<ArrayList<User>, ObservableSource<?>>() {
                      @Override
                      public ObservableSource<?> apply(@NonNull final ArrayList<User> users) throws Exception {
                          //                          return Observable.fromIterable(users);
                          return Observable.create(emitter -> {
                              Slog.t(TAG).s(false).th(true).i("print rxjava.ObservableOnSubscribe.subscribe method function ");
                              for (User user : users) {
                                  SystemClock.sleep(5);
                                  Slog.t(TAG).i(user);
                              }
                          }).subscribeOn(Schedulers.newThread());  // 指定不同的线程发射事件时，就不能保证事件的有序性
                      }
                  })    // flatMap Function的 apply 方法会被并发的执行
                  .subscribe(user -> Slog.t(TAG).i(user),
                          Slog::e,
                          () -> {
                              Slog.t(TAG).i("complete called");
                              countDownLatch.countDown();
                          });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * concatMap 与flatMap的区别是保证数据的有序性。
     */
    @Test
    public void concatMapOperatorTest() {

        ArrayList<User> list1 = new ArrayList<>();
        ArrayList<User> list2 = new ArrayList<>();
        ArrayList<User> list3 = new ArrayList<>();
        ArrayList<User> list4 = new ArrayList<>();
        ArrayList<User> list5 = new ArrayList<>();
        ArrayList<User> list6 = new ArrayList<>();
        ArrayList<User> list7 = new ArrayList<>();
        ArrayList<User> list8 = new ArrayList<>();
        ArrayList<User> list9 = new ArrayList<>();
        ArrayList<User> list10 = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list1.add(new User("1_sky_" + i, 123));
            list2.add(new User("2_sky_" + i, 123));
            list3.add(new User("3_sky_" + i, 123));
            list4.add(new User("4_sky_" + i, 123));
            list5.add(new User("5_sky_" + i, 123));
            list6.add(new User("6_sky_" + i, 123));
            list7.add(new User("7_sky_" + i, 123));
            list8.add(new User("8_sky_" + i, 123));
            list9.add(new User("9_sky_" + i, 123));
            list10.add(new User("10_sky_" + i, 123));

        }

        ArrayList<ArrayList<User>> allList = new ArrayList<>();
        allList.add(list1);
        allList.add(list2);
        allList.add(list3);
        allList.add(list4);
        allList.add(list5);
        allList.add(list6);
        allList.add(list7);
        allList.add(list8);
        allList.add(list9);
        allList.add(list10);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        // TODO 此处如果在 flatMap是否是交叉的取决于 Function.apply方法返回的Observable.subscribeOn方法是否不在同一个线程
        // 现在我希望在最终的观察者中接收的参数为单个user
        Observable.fromIterable(allList)
                  .concatMap(list -> Observable.fromIterable(list).subscribeOn(Schedulers.newThread()))
                  .subscribe(user -> Slog.t(TAG).i(user),
                          Slog::e,
                          countDownLatch::countDown);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
