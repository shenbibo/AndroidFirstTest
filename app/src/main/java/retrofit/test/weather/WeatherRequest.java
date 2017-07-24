package retrofit.test.weather;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sky.slog.Slog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import retrofit.common.HttpUtils;
import retrofit.common.RequestBean;
import retrofit.common.SimpleHttpCallback;
import retrofit.common.annotation.QueryField;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/6/28.
 */

public class WeatherRequest {
    public static final String TAG = "RetrofitBasicTest";
    public static final String WEATHER_KEY = "b06e0a9a06024ea0b09c4053b905b508";
    public static final String BASE_URL = "https://api.heweather.com/v5/";   // baseUr必须要以/结尾

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


    public static void getCityInfo() {
        ApiServer server = retrofit.create(ApiServer.class);
        Observable<Root> observable = server.getCityInfo("深圳", WEATHER_KEY);

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<Root>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(Root heWeather5) {
                          Slog.t(TAG).i(heWeather5);
                      }

                      @Override
                      public void onError(Throwable e) {
                          Slog.t(TAG).e(e);
                      }

                      @Override
                      public void onComplete() {
                          Slog.t(TAG).i("getCityInfo complete");
                      }
                  });
    }

    //    public static void getCityInfo1(){
    //        ApiServer server = retrofit.create(ApiServer.class);
    //        Observable<HeWeather5> observable = server.getCityInfo1("深圳", WEATHER_KEY);
    //
    //        observable.subscribeOn(Schedulers.io())
    //                  .observeOn(AndroidSchedulers.mainThread())
    //                  .subscribe(new Observer<HeWeather5>() {
    //                      @Override
    //                      public void onSubscribe(Disposable d) {
    //
    //                      }
    //
    //                      @Override
    //                      public void onNext(HeWeather5 heWeather5) {
    //                          Slog.t(TAG).i(heWeather5);
    //                      }
    //
    //                      @Override
    //                      public void onError(Throwable e) {
    //                          Slog.t(TAG).e(e);
    //                      }
    //
    //                      @Override
    //                      public void onComplete() {
    //                          Slog.t(TAG).i("getCityInfo1 complete");
    //                      }
    //                  });
    //    }

    public static void getCityInfo2() {
        ApiServer server = retrofit.create(ApiServer.class);
        Observable<Root> observable = server.getCityInfo2();

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<Root>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(Root heWeather5s) {
                          Slog.t(TAG).i(heWeather5s);
                      }

                      @Override
                      public void onError(Throwable e) {
                          Slog.t(TAG).e(e);
                      }

                      @Override
                      public void onComplete() {
                          Slog.t(TAG).i("getCityInfo2 complete");
                      }
                  });
    }

    // 注意使用该方式也不可以，如果method的值为"search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508"
    // 那么？会被URL统一化为：%3F
    public static void getCityInfo3() {
        ApiServer server = retrofit.create(ApiServer.class);
        // 指定全部的URL也是可以的
        Observable<Root> observable = server.getCityInfo3("https://api.heweather" +
                                                                  ".com/v5/search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508");

        // 只给出部分的URL也是可以的，只要设置了正确的baseUrl
        //        Observable<Root> observable = server.getCityInfo3("search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508");
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<Root>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(Root heWeather5s) {
                          Slog.t(TAG).i(heWeather5s);
                      }

                      @Override
                      public void onError(Throwable e) {
                          Slog.t(TAG).e(e);
                      }

                      @Override
                      public void onComplete() {
                          Slog.t(TAG).i("getCityInfo3 complete");
                      }
                  });
    }

    public static void getCityInfo4_1() {
        ApiServer server = retrofit.create(ApiServer.class);
        // 指定全部的URL也是可以的
        //        Observable<Root> observable = server.getCityInfo4("https://api.heweather.com/v5/search", "深圳",
        // "b06e0a9a06024ea0b09c4053b905b508");

        // 只给出部分的URL也是可以的，只要设置了正确的baseUrl，同时不完整的url 要符合 xxx/xxx/xxx.....
        // 注意search前不能加'/'，否则其上一层的path会被忽略,url也可以是
        Observable<Root> observable = server.getCityInfo4("search", "深圳", "b06e0a9a06024ea0b09c4053b905b508");

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<Root>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(Root heWeather5s) {
                          Slog.t(TAG).i(heWeather5s);
                      }

                      @Override
                      public void onError(Throwable e) {
                          Slog.t(TAG).e(e);
                      }

                      @Override
                      public void onComplete() {
                          Slog.t(TAG).i("getCityInfo4 complete");
                      }
                  });
    }

    // 注意使用该方式也不可以，如果method的值为"search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508"
    // 那么？会被URL统一化为：%3F
    public static void getCityInfo5() {
        ApiServer server = retrofit.create(ApiServer.class);
        Observable<ResponseBody> observable = server.getCityInfo5();

        observable.subscribeOn(Schedulers.io())
                  .map(responseBody -> {
                      Gson gson = new Gson();
                      return gson.fromJson(responseBody.string(), Root.class);
                  })
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<Root>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(Root heWeather5s) {
                          Slog.t(TAG).i(heWeather5s.toString());
                      }

                      @Override
                      public void onError(Throwable e) {
                          Slog.t(TAG).e(e);
                      }

                      @Override
                      public void onComplete() {
                          Slog.t(TAG).i("getCityInfo3 complete");
                      }
                  });
    }

    public static void getCityInfo4() {
        // 此处返回0，不会包含Object对象的参数
        //        Slog.t(TAG).i("requestBean.getInterfaces = " + RequestBean.class.getInterfaces().length);
        RequestBean requestBean = new City();
        HttpUtils.get(requestBean, new SimpleHttpCallback<Root>() {
            @Override
            public void onSuccess(Root root) {
                Slog.t(TAG).i(root);
            }

            @Override
            public void onFailure(Throwable e) {
                Slog.t(TAG).e(e);
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
            //            ResponseBody
            return response;
        }
    }

    public static class City extends RequestBean {
        @QueryField
        private String city = "深圳";

        @QueryField
        private String key = "b06e0a9a06024ea0b09c4053b905b508";

        public City() {
            super();
            method = "search";
        }
    }

}
