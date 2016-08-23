package recyclerview.test.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidfirsttest.R;

import download.test.utils.HttpUtils;

/**
 * 
 * 
 * @author sWX284798
 * @date 
 * @version
 * @since
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder>
{
    private static final String TAG = "RVAdapter";
    
    private List<String> list = new ArrayList<String>();
    
    private Context mContext;
    
    /**
     * 
     */
    public RVAdapter(List<String> list, Context context)
    {
        this.list = list;
        mContext = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, null));
        return holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(RVAdapter.MyViewHolder holder, int position)
    {
        holder.tv.setText(list.get(position));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount()
    {
        
        return list.size();
    }
    
    class MyViewHolder extends ViewHolder
    {
        
        public TextView tv; 

        /**
         * @param itemView
         */
        public MyViewHolder(View itemView)
        {
            super(itemView);
            //if(itemView instanceof TextView)
            {
                tv = (TextView)itemView.findViewById(R.id.tv);
            }
            //else
            {
               Log.d(TAG, "itemView != null");
            }
            
            
        }
        
    }



}
