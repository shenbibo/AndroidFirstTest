package retrofit.common;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
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
import retrofit.common.annotation.FormField;
import retrofit.common.annotation.HeaderField;
import retrofit.common.annotation.IgnoreField;
import retrofit.common.annotation.QueryField;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/7/4.
 */

public class HttpUtils {
    public static final String TAG = "RetrofitBasicTest";

    public static void setBaseUrl(String baseUrl) {
        retrofit = retrofit.newBuilder().baseUrl(baseUrl).build();
    }

    public static void setRetrofit(Retrofit retrofit) {
        HttpUtils.retrofit = retrofit;
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        retrofit = retrofit.newBuilder().client(okHttpClient).build();
    }

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient().newBuilder()
                                                                         .addInterceptor(new TestInterceptor())
                                                                         .connectTimeout(30, TimeUnit.SECONDS)
                                                                         .readTimeout(30, TimeUnit.SECONDS)
                                                                         .build();


    private static Retrofit retrofit = new Retrofit.Builder().client(OK_HTTP_CLIENT)
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
            requestAndParseResponse(httpCallback, observable, beanClass);
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private static <T> void requestAndParseResponse(final HttpCallback<T> httpCallback,
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
                             httpCallback.onStart(new RxCancellable(d));
                         }

                         @Override
                         public void onNext(T t) {
                             httpCallback.onSuccess(t);
                         }

                         @Override
                         public void onError(Throwable e) {
                             if (e instanceof HttpException) {
                                 e = new RxHttpException((HttpException) e);
                             }
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
                                requestBean.getHeaders(),
                                FieldUtils.parseFields(requestBean, QueryField.class));
    }

    private static Observable<ResponseBody> createPostBodyObservable(RequestBean requestBean) {
        return getService().postBody(requestBean.getMethod(),
                                     requestBean.getHeaders(),
                                     requestBean);
    }

    private static Observable<ResponseBody> createPostFormObservable(RequestBean requestBean) {
        return getService().postForm(requestBean.getMethod(),
                                     requestBean.getHeaders(),
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
            if (request.body() != null) {
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
        Gson gson = new Gson();
        return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
    }

    private static class GsonExclusionStrategy implements ExclusionStrategy {
        private static final List<Class<? extends Annotation>> EXCLUDE_ANNOTATIONS =
                Arrays.asList(IgnoreField.class, FormField.class, HeaderField.class, QueryField.class);

        private static final List<String> EXCLUDE_FIELD_NAME =
                Arrays.asList("method", "headerMap");

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            // 忽略指定字段名称的字段
            if (EXCLUDE_FIELD_NAME.contains(f.getName())) {
                return true;
            }

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
    //        Observable.create(e -> requestAndParseResponse(httpCallback, getObservable, beanClass))
    //                  .subscribeOn(AndroidSchedulers.mainThread())
    //                  .subscribe();
    //    }

}
