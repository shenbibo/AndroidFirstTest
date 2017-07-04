package retrofit;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sky.slog.Slog;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
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
                                                             .addConverterFactory(GsonConverterFactory.create())
                                                             .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                             .build();

    public static <T> void get(RequestBean requestBean, HttpCallback<T> httpCallback) {
        BasicService service = retrofit.create(BasicService.class);
        Observable<ResponseBody> observable1 = service.get(requestBean.getMethod(),
                                                           requestBean.getQueryMap(), requestBean.getHeadsMap());
        //        Slog.t(TAG).i("observable1.class = " + observable1.getClass() + ", paramter = " +
        // observable1.getClass()
        // .getTypeParameters()[0].getName());
        // 注意只有当前对象确实是一个接口的class的时isInterface()才返回true，其他即使是实现接口的匿名类isInterface()方法也返回false
        Slog.t(TAG).i("HttpCallback.CLASS IS interface = " + HttpCallback.class.isInterface()); // 返回true

        Slog.t(TAG).i(", isInteface = " + httpCallback.getClass().isInterface()
                              + ", name = " + httpCallback.getClass().getName());  // 返回false
        Class<?> beanClass;
        beanClass = (Class<?>) ((ParameterizedType) (httpCallback.getClass().getGenericSuperclass())).getActualTypeArguments()[0];

        // 获取泛型的实际参数
        Slog.t(TAG).i("beanClass = " + beanClass.getName());
        // 此处在运行时强制类型转换为什么不报错，
        // 是不是因为编程泛型时，发生了类型擦除，导致最终可以
        //        ArrayList<String> list = new ArrayList<>();
        //        ArrayList<MyPesponse> integers = new ArrayList<>();
        //        list = (ArrayList<String>)integers;

        observable1.subscribeOn(Schedulers.io()).map(responseBody -> {
            Gson gson = new Gson();
            return (T) (gson.fromJson(responseBody.string(), beanClass));
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                Observable.fromArray(d).observeOn(AndroidSchedulers.mainThread()).subscribe(httpCallback::onStart);
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

            }
        });
    }


    public static class TestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Slog.t(TAG).i(request.toString());
            Response response = chain.proceed(request);
            Slog.t(TAG).i(response.toString());

            //            Slog.t(TAG).json(response.body().string());
            return response;
        }
    }
}
