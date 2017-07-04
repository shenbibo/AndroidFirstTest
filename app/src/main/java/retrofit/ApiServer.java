package retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.*;

/**
 * [测试器]
 * [详述类的功能。]
 * Created by sky on 2017/6/28.
 */
public interface ApiServer {
    //https://api.heweather.com/v5/search?city=yourcity&key=yourkey

    // 使用查询参数的方式
    @GET("search")
    Observable<Root> getCityInfo(@Query("city") String city, @Query("key") String key);

    // 搜索不允许使用动态的占位符功能
    // 使用占位符的方式
//    @GET("search?city={city}&key={key}")
//    Observable<HeWeather5> getCityInfo1(@Path("city") String city, @Path("key") String key);

    // 注意使用该方式也不可以，如果method的值为"search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508"
    // 那么？会被URL统一化为：%3F
    // 或者  @Path(value = "method", encoded = true)
    //
    @GET("{method}")
    Observable<Root> getCityInfo3(@Path("method") String method);

    // 使用完整后缀
    @GET("search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508")
    Observable<Root> getCityInfo2();

//    // 使用完整后缀
//    @GET("search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508")
//    Observable<Root> getCityInfo3(@QueryMap);

    // 使用完整后缀
    @GET("search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508")
    Observable<ResponseBody> getCityInfo5();
}
