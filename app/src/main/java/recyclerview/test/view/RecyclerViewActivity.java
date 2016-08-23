package recyclerview.test.view;

import java.util.ArrayList;
import java.util.List;

import recyclerview.test.adapter.RVAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.androidfirsttest.R;

/**
 * 测试android5.0新引入的RecycleView
 * 
 * @author sWX284798
 * @date 2015年9月11日 下午3:55:31
 * @version
 * @since
 */
public class RecyclerViewActivity extends ActionBarActivity
{

    private RecyclerView rv;
    
    private List<String> list = new ArrayList<String>();
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reyclerview);
        
        initData();
        initView();
        

        
    }
    
    private void initView()
    {
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RVAdapter(list, this));
    }
    
    
    private void initData()
    {
        for(int i = 'A'; i < 'z' + 1; i++)
        {
            list.add("" + (char)i);
        }
    }
}
