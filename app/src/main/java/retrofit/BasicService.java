package retrofit;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.*;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/7/3.
 */

public interface BasicService<T> {

    /** 抽象方法 */
    @GET("{method}")
     Observable<T> get(@Path("method") String method, @QueryMap Map<String, String> queryMap,
                          @HeaderMap Map<String, String> headMap);
}
