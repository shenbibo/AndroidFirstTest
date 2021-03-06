package rxjava;

import android.util.Log;

import com.sky.slog.Slog;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * [RxJava基本功能测试类]
 * [详述类的功能。]
 * Created by sky on 2017/4/24.
 */

public class RxJavaBaseTest {
    private static final String TAG = "RxJavaBaseTest";

    /**
     * 测试使用完整的Observer类
     */
    public static void testFullObserver() {
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
    public static void testJustCreateSimpleObservableWithFullObserver() {

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
    public static void testJustWithMultiObject() {
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
    public static void testObserverOnlyOnNext() {
        Observable<String> stringObservable3 = Observable.just("justOnlyOnNext1", "justOnlyOnNext2");

        stringObservable3.subscribe(string -> Log.i(TAG, string));
    }

    /**
     * 测试使用onNext and onError方法
     */
    public static void testObserverOnNextAndOnError() {
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
    public static void testCancelObserverWhenOnSubscribeCalled() {
        Observable<String> stringObservable5 = Observable.just("test CancelObserverWhenSubscribeCalled");

        stringObservable5.subscribe(string -> Log.i(TAG, string),
                e -> Log.i(TAG, e.getMessage()),
                () -> Log.i(TAG, "onCompletedCalled"),
                disposable -> {
                    Log.i(TAG, "stringObservable5 onSubscribe called, ready to dispose");
                    disposable.dispose();
                });
    }


    public static class User {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
