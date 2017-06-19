package rxjava;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.sky.slog.Slog;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.concurrent.*;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * [RxJava基本功能测试类]
 * [详述类的功能。]
 * Created by sky on 2017/4/24.
 */
@RunWith(AndroidJUnit4.class)
public class RxJavaBaseTest2 {
    private static final String TAG = "RxJavaBaseTest2";

    /**
     * 测试使用完整的Observer类
     */
    @Test
    public void testFullObserver() {
        Observable<String> stringObservable = Observable.create(emitter -> {
            emitter.onNext("fullObserverOnNext1");
            emitter.onNext("fullObserverOnNext2");
            emitter.onNext("fullObserverOnNext3");
            emitter.onComplete();
        });

        Observer<String> stringObserver1 = new Observer<String>() {
            // 该方法一定在onNext之前调用，但是并不一定在同一个线程，具体见ObservableSubscribeOn.subscribeActual方法
            // 该方法的调用绝大多数在执行subscribe方法的线程中调用
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "full stringObserver1 called");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "full stringObserver1 onError called");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "stringObserver1 onComplete called");
            }
        };

        stringObservable.subscribeOn(Schedulers.io()).subscribe(stringObserver1);
    }

    /**
     * 测试使用Just方法简单创建可观察的对象
     */
    @Test
    public void testJustCreateSimpleObservableWithFullObserver() {

        // just 方法最多可以组合10个item(对象)
        Observable<String> stringObservable = Observable.just("justCreateObservable1", "justCreateObservable2");

        Observer<String> stringObserver2 = new Observer<String>() {
            // 该方法的调用和onNext方法在同一线程，并且先于onNext调用
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "full stringObserver2 called");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "full stringObserver2 onError called");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "full stringObserver2 onComplete called");
            }
        };

        stringObservable.subscribe(stringObserver2);
    }

    /**
     * 使用just创建被观察者，给它传递的数据是不同的对象
     */
    @Test
    public void testJustWithMultiObject() {
        Observable<Object> multiObjectJustObservable = Observable.just("string", 123, 1.732f, 3.14, 'c', new User("sky", 30));

        multiObjectJustObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                Slog.t(TAG).i("multiObjectJustObservableTest, obj = %s", o.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * 仅仅测试onNext方法
     */
    @Test
    public void testObserverOnlyOnNext() {
        Observable<String> stringObservable3 = Observable.just("justOnlyOnNext1", "justOnlyOnNext2");

        stringObservable3.subscribe(string -> Log.i(TAG, string));
    }

    /**
     * 测试使用onNext and onError方法
     */
    @Test
    public void testObserverOnNextAndOnError() {
        Observable<String> stringObservable4EndWithOnError = Observable.create(e -> {
            e.onNext("stringObservable4OnNext");
            e.onError(new Exception("stringObservable4 onError called"));
        });

        stringObservable4EndWithOnError.subscribe(string -> Log.i(TAG, string), e -> Log.i(TAG, e.getMessage()));
    }

    /**
     * 测试使用onNext and onSubscribe
     * 调用onSubscribe时取消订阅
     */
    @Test
    public void testCancelObserverWhenOnSubscribeCalled() {
        Observable<String> stringObservable5 = Observable.just("test CancelObserverWhenSubscribeCalled");

        // 一旦disposable.dispose(); 调用了，所有的事件在观察者则不再接收
        stringObservable5.subscribe(string -> Log.i(TAG, string),
                e -> Log.i(TAG, e.getMessage()),
                () -> Log.i(TAG, "onCompletedCalled"),
                disposable -> {
                    Log.i(TAG, "stringObservable5 onSubscribe called, ready to dispose");
                    disposable.dispose();
                });
    }

    /** 测试from操作符 */
    @Test
    public void testFromOperateObservable() {
        // 非背压模式下from支持5种方式：
        // array
        // iterable
        // Future   // 返回future.get()的返回值，并且可以添加超时机制，还可以指定future.get()执行的线程
        // publisher(是不是支持将背压模式转换为非背压模式)
        // Callable

        Integer[] integerArray = new Integer[]{1, 2, 3, 4, 456};
        Observable<Integer> integerFromObservable = Observable.fromArray(integerArray);

        integerFromObservable.subscribe(i -> Slog.t(TAG).i("testFromOperateObservable, array item = %d", i));
    }

    /**
     * 测试特殊被观察者对象，empty/never/Error
     */
    @Test
    public void testSpecialOperateObservable() {
        Observer specialObserver = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {
                Slog.t(TAG).w(e, "special Observable error() onError called");
            }

            @Override
            public void onComplete() {
                Slog.t(TAG).i("special Observable empty complete called");
            }
        };

        // 空的可观察者对象，直接调用onComplete方法。
        Observable.empty().subscribe(specialObserver);

        // 无作用的可观察者对象，不会调用Observer的任何方法，不发生任何事件
        Observable.never().subscribe(specialObserver);

        // 直接抛出异常的可观察者对象，直接调用onError方法。
        Observable.error(new RuntimeException("test the error observable")).subscribe(specialObserver);
    }

    /**
     * 测试范围操作符，以第一个参数为起始值，第二个参数为递增发送次数，支持long和int两种形式
     */
    @Test
    public void testRangeOperateObservable() {
        Observable rangeObservable = Observable.range(100, 5);
        rangeObservable.subscribe(i -> Slog.t(TAG).i("rangeOperateObservableTest, i = %d", i));
    }

    /**
     * 测试定时器操作符，有一个重载方法，可以指定执行定时器的线程
     */
    @Test
    public void testTimerOperateObservable() {
        // 默认定时器的的线程是Schedulers.computation()中这里我指定到IO中
        Observable timberObservable = Observable.timer(2000, TimeUnit.MICROSECONDS, Schedulers.io());

        timberObservable.subscribe(l -> {
            // 注意此代码在IO子线程中执行
            Slog.t(TAG).s(false).th(true).i("testTimerOperateObservable, after 2000ms, l = %d", l);
        });
    }

    /**
     * 每隔指定间隔的时间发射一个数，数无限递增
     */
    @Test
    public void testIntervalOperateObservable() {
        // 初始延时1000ms, 后面每条延时500ms
        Observable<Long> intervalObservable = Observable.interval(1000, 500, TimeUnit.MILLISECONDS, Schedulers.newThread());

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Observer<Long> intervalObserver = new Observer<Long>() {
            private long startTime = 0;
            Disposable disposable;
            private int count = 0;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                startTime = System.currentTimeMillis();
            }

            @Override
            public void onNext(Long aLong) {
                ++count;

                long curTime = System.currentTimeMillis();

                Slog.t(TAG).i("testIntervalOperate, interval time = %d, value = %d", (curTime - startTime), aLong);
                startTime = curTime;

                if (count >= 8) {
                    disposable.dispose();
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onError(Throwable e) {
                Slog.t(TAG).i("testIntervalOperate, onError");
            }

            @Override
            public void onComplete() {
                Slog.t(TAG).i("testIntervalOperate, onComplete");
            }
        };

        intervalObservable.subscribe(intervalObserver);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRepeatOperateObservable(){
        Slog.t(TAG).i("testRepeatOperateObservable, start now");

        ArrayList<String> testStrings = new ArrayList<>();
        testStrings.add("interval first string");
        testStrings.add("interval second string");

        Observable<String> repeatObservable = Observable.fromIterable(testStrings).repeat(2);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        repeatObservable.subscribeOn(Schedulers.computation()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Slog.t(TAG).s(false).th(true).i(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Slog.t(TAG).i("testRepeatOperateObservable, onComplete");
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * defer在发生订阅时才执行Observable对象的创建，并且每一次订阅可以返回一个新的对象，适合于每次订阅都获取最新的数据
     * */
    @Test
    public void testDeferOperateObservable(){
        final User user = new User("sky", 123);
        Observable<User> deferObservable = Observable.defer(()-> Observable.just(user));

        user.setName("gavin");
        user.setAge(321);

        deferObservable.subscribe(user1 -> Slog.t(TAG).i("testDeferOperateObservable, USER = " + user1.toString()));
    }

}
