package rxjava.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.androidfirsttest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/4/24.
 */

public class RxJavaTestActivity extends AppCompatActivity {

    @BindView(R.id.rx_basic_test_button)
    Button rxBasicTestButton;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.button5)
    Button button5;
    @BindView(R.id.button6)
    Button button6;
    @BindView(R.id.button7)
    Button button7;
    @BindView(R.id.button8)
    Button button8;
    @BindView(R.id.button9)
    Button button9;
    @BindView(R.id.button10)
    Button button10;
    @BindView(R.id.button11)
    Button button11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rx_basic_test_button)
    public void onRxBasicTest(){
        RxJavaBaseTest.testFullObserver();
        RxJavaBaseTest.testJustCreateSimpleObservableWithFullObserver();
        RxJavaBaseTest.testObserverOnlyOnNext();
        RxJavaBaseTest.testObserverOnNextAndOnError();
        RxJavaBaseTest.testCancelObserverWhenOnSubscribeCalled();
    }
}
