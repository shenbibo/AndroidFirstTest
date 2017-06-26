package rxjava;

import android.app.ActivityThread;
import android.graphics.LinearGradient;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.sky.slog.Slog;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.operators.flowable.FlowableCreate;
import io.reactivex.internal.operators.flowable.FlowableObserveOn;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import io.reactivex.schedulers.Schedulers;

/**
 * [背压只有在异步时才有使用的意义]
 * [详述类的功能。]
 * Created by sky on 2017/6/26.
 */

public class RxJavaFlowableTest {
    public static final String TAG = RxJavaFlowableTest.class.getSimpleName();

    // BackpressureStrategy.DROP      // 当缓存池满时丢弃掉新的
    // BackpressureStrategy.BUFFER    // 缓存所有的事件，直到消费者消耗完所有的事件，注意可能导致OOM
    // BackpressureStrategy.ERROR     // 缓存的事件超出缓存池的大小，直接打印抛出异常到onError，如果观察者没有实现onError，则抛出运行时异常OnErrorNotImplementedException

    // BackpressureStrategy.LATEST    // 与Drop类似，不过总是会保证最后一个事件能够发送
    // BackpressureStrategy.MISSING   // 既不缓存也不丢弃，把事件丢给其调用链的下级来处理


    /**
     * ERROR背压策略
     */
    @Test
    public void errorBackpressureTest() {
        final Subscription[] sp = new Subscription[1];

        Flowable.create((FlowableOnSubscribe<Integer>) e -> {
            // 默认缓存128个事件如果超出缓存的事件的范围就会抛出异常，
            //io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
            //at io.reactivex.internal.operators.flowable.FlowableCreate$ErrorAsyncEmitter.onOverflow(FlowableCreate.java:411)
            //at io.reactivex.internal.operators.flowable.FlowableCreate$NoOverflowBaseAsyncEmitter
            // .onNext(FlowableCrea.java:377)
            //at rxjava.RxJavaFlowableTest.lambda$errorBackpressureTest$0(RxJavaFlowableTest.java:35)

            // 同样的如果下游处理事件的速度慢于上层发送事件的速率，那么当事件累计到缓存的阈值时，也会抛出上述crash
            // 注意Error 策略是将溢出的异常抛出到下游的onError的（如果下游有onError对应的接口的话）
            for (int i = 0; i < 500; i++) {
                e.onNext(i);
                Slog.t(TAG).i("onNext i = " + i);
            }

//            SystemClock.sleep(1000);
//            for (int i = 71; i < 300; i++) {
//                e.onNext(i);
//                Slog.t(TAG).i("onNext i = " + i);
//            }
            e.onComplete();
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()/*Schedulers.newThread()*/)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        // 注意如果观察者没有请求任何事件，下游是收不到任何事件的，但是并不影响事件本身的执行
                        // 当值为Long.MAX_VALUE时，其实就是表示下游接收所有的事件
//                        sp[0] = s;
                        s.request(100);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Slog.t(TAG).i("integer = %d", integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Slog.t(TAG).e(t, "MissingBackpressureException");
                    }

                    @Override
                    public void onComplete() {
                        Slog.t(TAG).i("onComplete");
                    }
                });

//        sp[0].request(Long.MAX_VALUE);

        SystemClock.sleep(50000);
    }

    /**
     * ERROR背压策略
     */
    @Test
    public void errorBackpressureSubscriberNotDealOnErrorTest() {
        // 观察者没有实现onError抛出
//        io.reactivex.exceptions.OnErrorNotImplementedException: create: could not emit value due to lack of requests
//        at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
//        at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
//        at io.reactivex.internal.subscribers.LambdaSubscriber.onError(LambdaSubscriber.java:76)
//        at io.reactivex.internal.operators.flowable.FlowableObserveOn$BaseObserveOnSubscriber.checkTerminated(FlowableObserveOn.java:207)
//        at io.reactivex.internal.operators.flowable.FlowableObserveOn$ObserveOnSubscriber.runAsync(FlowableObserveOn.java:392)
//        at io.reactivex.internal.operators.flowable.FlowableObserveOn$BaseObserveOnSubscriber.run(FlowableObserveOn.java:176)
//        at io.reactivex.android.schedulers.HandlerScheduler$ScheduledRunnable.run(HandlerScheduler.java:109)
//        at android.os.Handler.handleCallback(Handler.java:751)
//        at android.os.Handler.dispatchMessage(Handler.java:95)
//        at android.os.Looper.loop(Looper.java:154)
//        at android.app.ActivityThread.main(ActivityThread.java:6119)
//        at java.lang.reflect.Method.invoke(Native Method)
//        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886)
//        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776)
//        Caused by: io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
//        at io.reactivex.internal.operators.flowable.FlowableCreate$ErrorAsyncEmitter.onOverflow(FlowableCreate.java:411)
//        at io.reactivex.internal.operators.flowable.FlowableCreate$NoOverflowBaseAsyncEmitter.onNext(FlowableCreate.java:377)
//        at rxjava.RxJavaFlowableTest.lambda$errorBackpressureSubscriberNotDealOnErrorTest$1(RxJavaFlowableTest.java:108)

        Flowable.create((FlowableOnSubscribe<Integer>) e -> {
            for (int i = 0; i < 70; i++) {
                e.onNext(i);
                Slog.t(TAG).i("onNext i = " + i);
            }

//            SystemClock.sleep(1000);
            for (int i = 71; i < 300; i++) {
                e.onNext(i);
                Slog.t(TAG).i("onNext i = " + i);
            }

            e.onComplete();
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()/*Schedulers.newThread()*/)
                .subscribe(integer -> Slog.t(TAG).i("integer = %d", integer));

        SystemClock.sleep(5000);
    }

}
