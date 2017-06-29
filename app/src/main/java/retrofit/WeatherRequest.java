package retrofit;

import android.content.pm.ApplicationInfo;

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
    public static final String BASE_URL = "https://api.heweather.com/v5/";

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
