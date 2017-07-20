package retrofit;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sky.slog.Slog;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import retrofit.annotation.FormField;
import retrofit.annotation.HeaderField;
import retrofit.annotation.IgnoreField;
import retrofit.annotation.QueryField;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/7/4.
 */

public class HttpUtils {
    public static final String TAG = "RetrofitBasicTest";
    public static final String BASE_URL = "https://api.heweather.com/v5/";

    public static void setBaseUrl(String baseUrl) {
        retrofit = retrofit.newBuilder().baseUrl(baseUrl).build();
    }

    public static void setRetrofit(Retrofit retrofit) {
        HttpUtils.retrofit = retrofit;
    }

    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                                                 .addInterceptor(new TestInterceptor())
                                                                 .connectTimeout(60, TimeUnit.SECONDS)
                                                                 .readTimeout(60, TimeUnit.SECONDS)
                                                                 .build();


    private static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                                                             .client(okHttpClient)
                                                             .addConverterFactory(GsonConverterFactory.create(createGson()))
                                                             .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                             .build();

    public static <T> void get(RequestBean requestBean, HttpCallback<T> httpCallback) {
        request(createGetObservable(requestBean), httpCallback);
    }

    public static <T> void postBody(RequestBean requestBean, HttpCallback<T> httpCallback) {
        request(createPostBodyObservable(requestBean), httpCallback);
    }

    public static <T> void postForm(RequestBean requestBean, HttpCallback<T> httpCallback) {
        request(createPostFormObservable(requestBean), httpCallback);
    }

    private static <T> void request(Observable<ResponseBody> observable, HttpCallback<T> httpCallback) {
        // 直接启动子线程执行
        Observable.create(e -> {
            Class<?> beanClass = getHttpCallbackParamsType(httpCallback);
            requestAndResponse(httpCallback, observable, beanClass);
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private static <T> void requestAndResponse(final HttpCallback<T> httpCallback,
                                               Observable<ResponseBody> getObservable,
                                               Class<?> beanClass) {
        getObservable.subscribeOn(Schedulers.io())
                     .unsubscribeOn(Schedulers.io())
                     .map(responseBody -> {
                         Slog.t(TAG).d("have receiver the response body");
                         if (beanClass.isAssignableFrom(responseBody.getClass())) {
                             //noinspection unchecked
                             return (T) responseBody;
                         }

                         Gson gson = new Gson();
                         return (T) (gson.fromJson(responseBody.string(), beanClass));
                     })
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(new Observer<T>() {
                         @Override
                         public void onSubscribe(Disposable d) {
                             httpCallback.onStart(d);
                         }

                         @Override
                         public void onNext(T t) {
                             httpCallback.onSuccess(t);
                         }

                         @Override
                         public void onError(Throwable e) {
                             httpCallback.onFailure(e);
                         }

                         @Override
                         public void onComplete() {
                            Slog.t(TAG).i("request complete");
                         }
                     });
    }

    private static Observable<ResponseBody> createGetObservable(RequestBean requestBean) {
        return getService().get(requestBean.getMethod(),
                                FieldUtils.parseFields(requestBean, HeaderField.class),
                                FieldUtils.parseFields(requestBean, QueryField.class));
    }

    private static Observable<ResponseBody> createPostBodyObservable(RequestBean requestBean) {
        return getService().postBody(requestBean.getMethod(),
                                     FieldUtils.parseFields(requestBean, HeaderField.class),
                                     requestBean);
    }

    private static Observable<ResponseBody> createPostFormObservable(RequestBean requestBean) {
        return getService().postForm(requestBean.getMethod(),
                                     FieldUtils.parseFields(requestBean, HeaderField.class),
                                     FieldUtils.parseFields(requestBean, FormField.class));
    }

    private static BasicService getService() {
        return retrofit.create(BasicService.class);
    }

    private static <T> Class<?> getHttpCallbackParamsType(HttpCallback<T> httpCallback) {
        Class<?> beanClass;
        ParameterizedType type = (ParameterizedType) httpCallback.getClass().getGenericSuperclass();
        beanClass = (Class<?>) type.getActualTypeArguments()[0];
        return beanClass;
    }


    public static class TestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Slog.t(TAG).i(request.toString());
            Slog.t(TAG).i(request.headers().toString());
            if(request.body() != null){
                Slog.t(TAG).i(request.body().toString());
                if (request.body() instanceof FormBody) {
                    Slog.t(TAG).i("isFormBody");
                }
            }

            Response response = chain.proceed(request);
            Slog.t(TAG).i(response.toString());
            return response;
        }
    }

    private static Gson createGson() {
        return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
    }

    private static class GsonExclusionStrategy implements ExclusionStrategy {
        private static final List<Class<? extends Annotation>> EXCLUDE_ANNOTATIONS =
                Arrays.asList(IgnoreField.class, FormField.class, HeaderField.class, QueryField.class);

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            Collection<? extends Annotation> annotations = f.getAnnotations();
            for (Annotation annotation : annotations) {
                if (EXCLUDE_ANNOTATIONS.contains(annotation.getClass())) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }


    //    // TODO 后面考虑使用统一的一个工具类来管理取消网络请求操作，如RxLifecycle框架
    //    // TODO 或者将onStart方法的调用不关注其是在哪个线程调用的，曲调切换到主线程的步骤
    //    private static <T> void startOnMainThread(HttpCallback<T> httpCallback,
    //    Observable<ResponseBody> getObservable, Class<?> beanClass) {
    //        // 强制切换到主线程发起，保证onSubscribe回调也在主线程运行
    //        Observable.create(e -> requestAndResponse(httpCallback, getObservable, beanClass))
    //                  .subscribeOn(AndroidSchedulers.mainThread())
    //                  .subscribe();
    //    }

    //    public static <T> void get(RequestBean requestBean, HttpCallback<T> httpCallback) {
    //        Observable<ResponseBody> observable1 = createGetObservable(requestBean);
    //        //        Slog.t(TAG).i("observable1.class = " + observable1.getClass() + ", paramter = " +
    //        // observable1.getClass()
    //        // .getTypeParameters()[0].getName());
    //        // 注意只有当前对象确实是一个接口的class的时isInterface()才返回true，其他即使是实现接口的匿名类isInterface()方法也返回false
    //        Slog.t(TAG).i("HttpCallback.CLASS IS interface = " + HttpCallback.class.isInterface()); // 返回true
    //
    //        Slog.t(TAG).i(", isInteface = " + httpCallback.getClass().isInterface()
    //                              + ", name = " + httpCallback.getClass().getName());  // 返回false
    //        Class<?> beanClass;
    //        beanClass = (Class<?>) ((ParameterizedType) (httpCallback.getClass().getGenericSuperclass()))
    // .getActualTypeArguments()[0];
    //
    //        // 获取泛型的实际参数
    //        Slog.t(TAG).i("beanClass = " + beanClass.getName());
    //        // 此处在运行时强制类型转换为什么不报错，
    //        // 是不是因为编程泛型时，发生了类型擦除，导致最终可以
    //        //        ArrayList<String> list = new ArrayList<>();
    //        //        ArrayList<MyPesponse> integers = new ArrayList<>();
    //        //        list = (ArrayList<String>)integers;
    //
    //        observable1.subscribeOn(Schedulers.io()).map(responseBody -> {
    //            Gson gson = new Gson();
    //            return (T) (gson.fromJson(responseBody.string(), beanClass));
    //        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
    //            @Override
    //            public void onSubscribe(Disposable d) {
    //                Slog.t(TAG).s(false).th(true).i("onSubscribe called!!");
    //                Observable.fromArray(d)
    //                          .observeOn(AndroidSchedulers.mainThread())
    //                          .subscribe(httpCallback::onStart);
    //            }
    //
    //            @Override
    //            public void onNext(T t) {
    //                httpCallback.onSuccess(t);
    //            }
    //
    //            @Override
    //            public void onError(Throwable e) {
    //                httpCallback.onFailure(e);
    //            }
    //
    //            @Override
    //            public void onComplete() {
    //
    //            }
    //        });
    //    }
}
