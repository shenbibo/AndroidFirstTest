package rxjava;

import android.os.Handler;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * [RxJAVA线程调度功能测试]
 * [详述类的功能。]
 * Created by sky on 2017/4/26.
 */

public class RxJavaThreadSchedulerTest {

    private static final String TAG = "RxJavaThreadTest";

    public static void testDoInSubThreadAndCallbackInMainThread() {
        Observable<String> stringObservable = Observable.create(e -> {
            Log.i(TAG, "current thread = " + Thread.currentThread().getName());
            e.onNext("doInSubThreadTest");
            e.onComplete();
        });

        stringObservable.subscribeOn(Schedulers.io()) // 该方法只在第一次调用的时候有效，后面调用会被忽略掉，为什么会被忽略？
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread()) // observeOn连续调用时最后一次调用生效
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {  // 该方法的调用与设置的subscribeOn线程无关，在调用subscribe的线程中调用
                                Log.i(TAG, "onSubscribe called, thread = " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onNext(String s) {
                                Log.i(TAG, "thread = " + Thread.currentThread().getName() + ", value = " + s);
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
     * 测试连续调用三次ObserveOn方法
     */
    public static void testDoInSubThreadAndObserveOnFunctionContinueCallThreeTimes() {
        Observable<String> stringObservable = Observable.create(e -> {
            Log.i(TAG, "current thread = " + Thread.currentThread().getName());
            e.onNext("doInSubThreadTest, and observeOn call three times");
            e.onComplete();
        });

        // subscribeOn方法多次调用指定不同的线程只有第一次生效，其原理类似于在在一个线程中嵌套启动2个线程，最终执行的代码是在第三个线程中。
        // 具体可以查看ObservableSubscribeOn.subscribeActual方法。

        // 一般地，每调用一次Observable的操作方法就会创建一个新的Observable对象该对象持有原来的对象。
        // 同样的除了ObservableSubscribeOn.subscribeActual方法其他Observable.subscribeActual会创建一个新的Observer对象
        // 该新对象同样持有原来Observer对象的引用
        // 正是通过这种递归持有（或者叫深层次代理方式）在最终调用Observable.subscribe(Observer)方法时Observable是最初的原始
        // Observable对象，但是Observer则是经过重重包装的对象，只有把每层包装的内容执行完之后最终才执行到原始的Observer对象的方法

        stringObservable.subscribeOn(Schedulers.io()) // 该方法只在第一次调用的时候有效，后面调用会被忽略掉
                        .observeOn(AndroidSchedulers.mainThread()) // observeOn连续调用时最后一次调用生效
                        .observeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.i(TAG, "onSubscribe called, thread = " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onNext(String s) {
                                Log.i(TAG, "thread = " + Thread.currentThread().getName() + ", value = " + s);
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
     * 测试不连续调用observeOn方法，分别执行线程切换
     */
    public static void testtDoInSubThreadAndCallObserveOnMethodDiscrete() {
        Observable<String> stringObservable = Observable.create(e -> {
            Log.i(TAG, "current thread = " + Thread.currentThread().getName());
            e.onNext("doInSubThreadTest, and observeOn call Discrete");
            e.onComplete();
        });

        // doOnNext 在调用onNext方法之前调用，对数据的一个处理
        stringObservable.subscribeOn(Schedulers.io()) // 该方法只在第一次调用的时候有效，后面调用会被忽略掉
                        .observeOn(AndroidSchedulers.mainThread()) // observeOn连续调用时最后一次调用生效
                        .doOnNext(s -> Log.i(TAG, "thread = " + Thread.currentThread().getName() + ", value = " + s));
    }
}
