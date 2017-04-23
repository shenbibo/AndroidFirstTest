package rxbinding.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.androidfirsttest.R;
import com.jakewharton.rxbinding2.view.RxView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/4/23.
 */

public class RxBindActivity extends AppCompatActivity {
    private static final String TAG = "RxBindActivity";
    /**
     * 跳转其他的界面
     */
    private Button enterButton;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity_layout);
        initView();
        initListener();
    }

    private void initListener() {
        String s = "RxBinding Test";
        RxView.clicks(enterButton).subscribe(o -> Log.i(TAG, "this value = " + o));
        RxView.clicks(enterButton).subscribe(new Observer2());

        // 在java中当使用Object作为泛型的参数的时候，其是不能被赋值为等于其他类型的的如以下代码：
        // 这是因为在该种情况下实际类型为ArrayList<Object> 而不是Object,
        // 同理ArrayList<String> strings的实际类型为ArrayList<String>，这两者显然不是同一种类型。
        // ArrayList<Object> objects = new ArrayList<>();
        // ArrayList<String> strings = new ArrayList<>();
        // objects = strings;
    }


    private void initView() {
        enterButton = (Button) findViewById(R.id.skipButton);
    }

    private class Observer1 implements Subscriber<String> {

        @Override
        public void onSubscribe(Subscription s) {

        }

        @Override
        public void onNext(String s) {

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }
    }

    private class Observer2 implements io.reactivex.Observer<Object>{

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Object o) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
