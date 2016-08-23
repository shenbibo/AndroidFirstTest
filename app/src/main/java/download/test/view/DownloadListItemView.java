package download.test.view;

import com.example.androidfirsttest.R;

import download.test.bean.DownloadFileInfo;
import download.test.bean.DownloadFileInfo.DownloadStatus;
import download.test.control.DownloadManager;
import download.test.viewbeans.BaseBean;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月24日 下午4:11:41
 * @version
 * @since
 */
public class DownloadListItemView extends BaseView
{
    
    private TextView downloadSpeed;
    
    private Button downloadControlButton;
    
    private DownloadFileInfo info;
    
    private ProgressBar downloadProgressBar;
    
    private TextView appName;
    
    private TextView appSize;
    
    /**
     * 
     */
    public DownloadListItemView(Context context)
    {
        super(context);
        
    }

    /**
     * 
     * 填充对应的View并绑定相应的数据
     * {@inheritDoc}
     */
    @Override
    public ViewGroup fillChildView(ViewGroup rootView)
    {
        rootView = super.fillChildView(rootView);
        View view = LayoutInflater.from(mContext).inflate(R.layout.download_listview_item_layout, null);
        initViews(view);
        rootView.addView(view);
        return rootView;
    }
    
    private void initViews(View rootView)
    {
        downloadSpeed = (TextView) rootView.findViewById(R.id.downloadSpeed);
        downloadControlButton = (Button) rootView.findViewById(R.id.downloadControlBtn);
        downloadProgressBar = (ProgressBar) rootView.findViewById(R.id.downloadprogressBar);
        appName = (TextView) rootView.findViewById(R.id.appName);
        appSize = (TextView) rootView.findViewById(R.id.appSize);
        setViewsControlEvent();
    }

    private void setViewsControlEvent()
    {
        downloadControlButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                controlButtonClickEvent(info.downloadStatus);
            }
        });
        
        //长按item则取消下载
        containerView.setOnLongClickListener(new OnLongClickListener()
        {
            
            @Override
            public boolean onLongClick(View v)
            {      
                int status = info.getStatus();
                if(status == DownloadStatus.DOWNLOADING 
                        || status == DownloadStatus.DOWNLOAD_WAIT
                        || status == DownloadStatus.DOWMLOAD_PAUSE
                        || status == DownloadStatus.DOWNLOAD_READY)
                {
                    DownloadManager.getInstance().cancelDownloadTask(info);
                }
                return true;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnClickListener(ViewEventListener listener)
    {
        super.setOnClickListener(listener);        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(BaseBean bean)
    {
        
        super.setData(bean);        
        if(bean instanceof DownloadFileInfo)
        {
            info = (DownloadFileInfo) bean;
            downloadSpeed.setText(DownloadFileInfo.covertIntegerSpeedToString(info.getDownloadSpeed()));
            downloadProgressBar.setProgress(info.downloadProgress);
            appName.setText(info.appName);
            appSize.setText(info.appSize);
            setButtonStatus(info.downloadStatus);
        }
    }
    
    private void controlButtonClickEvent(int status)
    {
        switch (status)
        {
            //启动新的下载
            case DownloadStatus.DOWNLOAD:  
                DownloadManager.getInstance().startNewDownloadTask(info);                
                break;
             
            //从暂停进入下载   
            case DownloadStatus.DOWMLOAD_PAUSE:
                DownloadManager.getInstance().resumeDownloadTask(info);
                break;
            //暂停下载
            case DownloadStatus.DOWNLOADING:
                DownloadManager.getInstance().pauseDownloadTask(info);
                break;
            default:
                break;
        }
    }
    
    private void setButtonStatus(int status)
    {
        String str = null;
        switch(status)
        {
            case DownloadStatus.DOWNLOAD:
                str = mContext.getResources().getString(R.string.download);
                break;
            case DownloadStatus.DOWNLOADING:
                //str = mContext.getResources().getString(R.string.downloading);
                str = info.getProgress() + "%";
                break;
            case DownloadStatus.DOWNLOAD_WAIT:
                str = mContext.getResources().getString(R.string.download_wait);
                break;
            case DownloadStatus.DOWNLOADED:
                str = mContext.getResources().getString(R.string.downloaded);
                break;
            case DownloadStatus.DOWNLOAD_FAILED:
                str = mContext.getResources().getString(R.string.download_fail);
                break;
            case DownloadStatus.DOWNLOAD_CANCEL:
                str = mContext.getResources().getString(R.string.download_cancel);
                break;
            case DownloadStatus.DOWMLOAD_PAUSE:
                str = mContext.getResources().getString(R.string.download_pause);
                break;
            case DownloadStatus.DOWNLOAD_READY:
                str = mContext.getResources().getString(R.string.download_ready);
                break;
            default:
                str = status + "";
                break;
               
        }
        downloadControlButton.setText(str);
    }
}
