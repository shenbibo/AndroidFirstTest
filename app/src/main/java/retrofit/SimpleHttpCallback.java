package retrofit;



import io.reactivex.disposables.Disposable;

public abstract class SimpleHttpCallback<T> implements HttpCallback<T>{
    @Override
    public void onStart(Disposable disposable){}

    @Override
    public void onFailure(Throwable e){}
}
