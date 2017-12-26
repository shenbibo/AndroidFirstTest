package rxjava;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import com.sky.slog.Slog;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
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
        User[] users1 = new User[]{new User("1_sky", 123), new User("1_gavin", 456), new User("1_smith", 789)};
        User[] users2 = new User[]{new User("2_sky", 123), new User("2_gavin", 456), new User("2_smith", 789)};
        User[] users3 = new User[]{new User("3_sky", 123), new User("3_gavin", 456), new User("3_smith", 789)};
        User[] users4 = new User[]{new User("4_sky", 123), new User("4_gavin", 456), new User("4_smith", 789)};

        User[][] allUsers = new User[][]{users1, users2, users3, users4};

        CountDownLatch countDownLatch = new CountDownLatch(1);
        // TODO 此处如果在 flatMap是否是交叉的取决于 Function.apply方法返回的Observable.subscribeOn方法是否不在同一个线程
        // 现在我希望在最终的观察者中接收的参数为单个user
        Observable.fromArray(allUsers)
                  .flatMap(users -> {
//                          return Observable.fromIterable(users);  // 可以直接使用该行代码
                      return Observable.create(emitter -> {
                          Slog.t(TAG).s(false).th(true).i("print rxjava.ObservableOnSubscribe.subscribe method function ");
                          for (User user : users) {
                              SystemClock.sleep(5);
                              Slog.t(TAG).i(user);
                          }
                      }).subscribeOn(Schedulers.newThread());  // 指定不同的线程发射事件时，就不能保证事件的有序性
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

        for (int i = 0; i < 10; i++) {
            list1.add(new User("1_sky_" + i, 123));
            list2.add(new User("2_sky_" + i, 123));
            list3.add(new User("3_sky_" + i, 123));
            list4.add(new User("4_sky_" + i, 123));
            list5.add(new User("5_sky_" + i, 123));
        }

        ArrayList<ArrayList<User>> allList = new ArrayList<>();
        allList.add(list1);
        allList.add(list2);
        allList.add(list3);
        allList.add(list4);
        allList.add(list5);


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

    /**
     * flatMapIterable 与 flatMap类似也是对数据的拆分再打包处理，知不过它返回的数据是iterable接口的
     * <p>
     * flatMapIterable()和flatMap()几乎是一样的，不同的是flatMapIterable()它转化的多个Observable是使用Iterable作为源数据的。
     */
    @Test
    public void flatMapIterableTest() {
        ArrayList<User> list1 = new ArrayList<>();
        ArrayList<User> list2 = new ArrayList<>();
        ArrayList<User> list3 = new ArrayList<>();
        ArrayList<User> list4 = new ArrayList<>();
        ArrayList<User> list5 = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list1.add(new User("1_sky_" + i, 123));
            list2.add(new User("2_sky_" + i, 124));
            list3.add(new User("3_sky_" + i, 125));
            list4.add(new User("4_sky_" + i, 126));
            list5.add(new User("5_sky_" + i, 127));
        }

        ArrayList<ArrayList<User>> allList = new ArrayList<>();
        allList.add(list1);
        allList.add(list2);
        allList.add(list3);
        allList.add(list4);
        allList.add(list5);

        Observable.fromIterable(allList)
                  .flatMapIterable(users -> {   // 将得到的列表中的每一个元素取出来，修改它的名字，并返回修改后的列表
                      List<User> newUsers = new ArrayList<>();
                      Observable.fromIterable(users).map(user -> {
                          user.setName(user.getName() + "_new");
                          return user;
                      }).subscribe(newUsers::add);   // 此处不能采用.subscribeOn(Schedulers.newThread())方法来异步操作，否则会返回空数据

                      return newUsers;
                  }, (users, user) -> {  // 第二个参数为BiFunction 返回一个结果选择器选择之后的值
                      if (user.getAge() >= 125 && user.getAge() < 127) {    // >125 则在原来的名字的基础上加上年龄名字
                          user.setName(user.getName() + "_" + user.getAge());
                      }
                      return user;
                  })
                  .subscribe(user -> Slog.t(TAG).i(user));
    }

    /**
     * switchMap()和flatMap()很像，除了一点：每当源Observable发射一个新的数据项（Observable）时，
     * 它将取消订阅并停止监视之前那个数据项产生的Observable，并开始监视当前发射的这一个。
     */
    @Test
    public void switchMapTest() {
        ArrayList<User> list1 = new ArrayList<>();
        ArrayList<User> list2 = new ArrayList<>();
        ArrayList<User> list3 = new ArrayList<>();
        ArrayList<User> list4 = new ArrayList<>();
        ArrayList<User> list5 = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            list1.add(new User("1_sky_" + i, 123));
            list2.add(new User("2_sky_" + i, 124));
            list3.add(new User("3_sky_" + i, 125));
            list4.add(new User("4_sky_" + i, 126));
            list5.add(new User("5_sky_" + i, 127));
        }

        ArrayList<ArrayList<User>> allList = new ArrayList<>();
        allList.add(list1);
        allList.add(list2);
        allList.add(list3);
        allList.add(list4);
        allList.add(list5);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Observable.fromIterable(allList).
                switchMap(users -> Observable.fromIterable(users).subscribeOn(Schedulers.newThread()))
                  .subscribe(user -> Slog.t(TAG).i(user),   // 最后打印的日志中很可能之后最后一个list的数据被打印
                          e -> Slog.t(TAG).e(e),
                          countDownLatch::countDown);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * scan()对一个序列的数据应用一个函数，并将这个函数的结果发射出去作为下个数据应用合格函数时的第一个参数使用。
     */
    @Test
    public void scanTest() {
        String[] strings = {"a", "a", "a", "a", "a", "a", "a", "a"};
        Observable.fromArray(strings)
                  .scan((str1, str2) -> str1 + str2)
                  .subscribe(str -> Slog.t(TAG).i(str));
    }

    /**
     * groupBy()将原始Observable发射的数据按照key来拆分成一些小的Observable，
     * 然后这些小Observable分别发射其所包含的的数据，和SQL中的groupBy类似。实
     * 际使用中，我们需要提供一个生成key的规则（也就是Func1中的call方法），
     * 所有key相同的数据会包含在同一个小的Observable中。另外我们还可以提供一个函数来对这些数据进行转化，有点类似于集成了flatMap。
     **/
    @Test
    public void groupByTest() {
        Integer[] integers = new Integer[]{123, 124, 125, 126, 221, 222, 223, 224, 225, 227};

        // 将按照key分组好的Observable<String>使用concat 连接组合起来
        Observable.concat(Observable.fromArray(integers).groupBy(integer -> {
                                if (integer < 200) {
                                    return 1;
                                } else {
                                    return 2;
                                }
                            }, integer -> "this is " + integer))   //valueSelector将 int -> string
                  .subscribe(string -> Slog.t(TAG).i(string));
    }
}
