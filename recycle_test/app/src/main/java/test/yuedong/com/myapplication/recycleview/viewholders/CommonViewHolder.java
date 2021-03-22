package test.yuedong.com.myapplication.recycleview.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by LaiXiaodong
 * Date ： 2016/9/14.
 */

public abstract class CommonViewHolder  extends RecyclerView.ViewHolder{

    protected View itemView;
    protected Context context;

    public CommonViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        getView(itemView);
    }

    public abstract void getView(View itemView);
}
