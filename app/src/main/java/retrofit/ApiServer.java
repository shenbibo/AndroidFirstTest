package retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.*;

import static android.R.attr.value;

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

    // 使用完整后缀
    @GET("search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508")
    Observable<Root> getCityInfo2();

    // 搜索不允许使用动态的占位符功能
    // 使用占位符的方式
    //    @GET("search?city={city}&key={key}")
    //    Observable<HeWeather5> getCityInfo1(@Path("city") String city, @Path("key") String key);

    // 注意使用该方式也不可以，如果method的值为"search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508"
    // 那么？会被URL统一化为：%3F
    // 即使@Path(value = "method", encoded = true)传入的参数中的？还是会被转换为%3F
    // 奇怪不论encoded是true还是false，都不生效，？符号一定会被转义， + 都不会被重新编码
    //        @GET("{method}")
    //        Observable<Root> getCityInfo3(@Path(value = "method", encoded = true) String method);
    // 注意在path注解指定的参数中不能传入'/ ? '等字符否则会被自动转义
    // ？？？？？？？？？？？？？？？？？？？？？？
    //    @GET("/search/{function}")
    //    Observable<Root> getCityInfo3(@Path("function") String method);

    // 如果传入的是非完整的URL，那最终的URL = baseUrl + method
    @GET
    Observable<Root> getCityInfo3(@Url String method);

    // 测试@url和@query之间的组合
    @GET
    Observable<Root> getCityInfo4(@Url String method, @Query("city") String city, @Query("key") String key);

    //    // 使用完整后缀
    //    @GET("search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508")
    //    Observable<Root> getCityInfo3(@QueryMap);

    // 使用完整后缀
    @GET("search?city=深圳&key=b06e0a9a06024ea0b09c4053b905b508")
    Observable<ResponseBody> getCityInfo5();
}
