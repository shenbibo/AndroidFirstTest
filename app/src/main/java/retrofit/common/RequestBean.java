package retrofit.common;

import retrofit.common.annotation.IgnoreField;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/7/4.
 */

public abstract class RequestBean {
    @IgnoreField
    protected String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
