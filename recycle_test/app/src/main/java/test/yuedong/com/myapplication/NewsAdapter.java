package test.yuedong.com.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyDeviceBean.DeviceInfo> list;


    public NewsAdapter( Context context) {
        this.context = context;
    }

    public NewsAdapter(List<MyDeviceBean.DeviceInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setData(List<MyDeviceBean.DeviceInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(context).inflate(R.layout.item_device_info, null, false);
        RecyclerView.ViewHolder holder = null;
        holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).title.setText(list.get(position).device_name);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title, time, source;

    public MyViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
    }
}