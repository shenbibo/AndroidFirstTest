package retrofit;

import io.reactivex.disposables.Disposable;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/7/4.
 */

public interface HttpCallback<T> {
    void onStart(Disposable disposable);

    void onSuccess(T t);

    void onFailure(Throwable e);
}

