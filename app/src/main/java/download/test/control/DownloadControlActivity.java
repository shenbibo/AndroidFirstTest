package download.test.control;

import javax.crypto.spec.GCMParameterSpec;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.androidfirsttest.R;
import com.gc.test.GCtest;
import com.gc.test.view.GCtestAtivity;

import download.test.bean.DataProvider;
import download.test.bean.DownloadFileInfo;
import download.test.utils.BroadcastConstants.DownloadAirConstants;
import download.test.utils.TestDataUtils;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月24日 上午10:50:51
 * @version
 * @since
 */
public class DownloadControlActivity extends Activity
{

    private Button downloadAllBtn;
    
    private Button pauseAllBtn;
    
    private ListView downloadListView;
    
    private DataProvider<DownloadFileInfo> provider;
    
    private ListAdapter adapter;
    
    
    private boolean isPauseAllTask = true;
    
   private boolean isDownloadAllTask = true;
    
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent != null)
            {
                String action = intent.getAction();
                if(action != null)
                {
                    if(action.equals(DownloadAirConstants.PROGRESS_ACTION))
                    {
                        adapter.notifyDataSetChanged();
                    }
                    else if(action.equals(DownloadAirConstants.STATUS_ACTION))
                    {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);

        setContentView(R.layout.download_control_activity_layout);
        initViews();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadAirConstants.PROGRESS_ACTION);
        filter.addAction(DownloadAirConstants.STATUS_ACTION);
        registerReceiver(receiver, filter, null, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy()
    {
        
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    
    private void initViews()
    {
        downloadAllBtn = (Button) findViewById(R.id.downlodAll);
        
        pauseAllBtn = (Button) findViewById(R.id.pauseAll);
        
        downloadListView = (ListView) findViewById(R.id.dowloadListView);
        
        provider = new DataProvider<DownloadFileInfo>(this);
        
        TestDataUtils.fillProvider(provider);
        
        adapter = new ListAdapter(this, provider);
        
        downloadListView.setAdapter(adapter);
        
        setViewControlEvents();
    }
    
    private void setViewControlEvents()
    {
        downloadAllBtn.setText("下载全部");
        downloadAllBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if(isDownloadAllTask)
                {
                    DownloadManager.getInstance().startAllTask(provider.getDataList());
                    isDownloadAllTask = false;
                    downloadAllBtn.setText("全部取消");
                }
                else
                {
                    DownloadManager.getInstance().cancelAllTask(provider.getDataList());
                    isDownloadAllTask = true;
                    downloadAllBtn.setText("下载全部");
                }
            }
        });
        
        pauseAllBtn.setText("全部暂停");
        pauseAllBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if(isPauseAllTask)
                {
                    DownloadManager.getInstance().pauseAllDownloadingTask(provider.getDataList());
                    isPauseAllTask = false;
                    pauseAllBtn.setText("恢复全部");
                }
                else
                {
                    DownloadManager.getInstance().resumeAllPauseTask(provider.getDataList());
                    isPauseAllTask = true;
                    pauseAllBtn.setText("全部暂停");
                }
            }
        });        
    }
     
    /**
     * {@inheritDoc}
     */
    @Override
    public void finish()
    {
        
        super.finish();
        //该方法必须要放在startActivity或finish方法之后
        overridePendingTransition(R.anim.set_animation_test, R.anim.set_animation_test);
        GCtestAtivity.getActivityObjStatus();

    }
   
    
}
