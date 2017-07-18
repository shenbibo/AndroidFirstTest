package retrofit;

import java.util.HashMap;
import java.util.Map;

import retrofit.annotation.IgnoreField;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/7/4.
 */

public abstract class RequestBean {
    @IgnoreField
    protected String method;
//    protected Map<String, String> queryMap = new HashMap<>();
//    protected Map<String, String> headerMap = new HashMap<>();

    public RequestBean(){
//        init();
    }

//    private final void init(){
//        initQueryMap();
//        initHeadsMap();
//    }

//    protected abstract void initQueryMap();
    //
    //    protected abstract void initHeadsMap();
    //
    //    public Map<String, String> getQueryMap() {
    //        return queryMap;
    //    }
    //
    //    public void setQueryMap(Map<String, String> queryMap) {
    //        this.queryMap = queryMap;
    //    }
    //
    //    public Map<String, String> getHeaderMap() {
    //        return headerMap;
    //    }
    //
    //    public void setHeaderMap(Map<String, String> headerMap) {
    //        this.headerMap = headerMap;
    //    }
    //
    //    public Map<String, String> getFieldMap(){
    //        return null;
    //    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
