package top.daxianwill.greendaodemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by admin on 2018/5/14.
 */

public class MyAdapter extends BaseAdapter {

    private List<User> mUserList;
    private Context mContext;

    MyAdapter(Context context,List<User> list) {
        mContext = context;
        mUserList = list;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        @SuppressLint("ViewHolder")
        View mView = layoutInflater.inflate(R.layout.list_item,parent,false);
//        TextView mName = mView.findViewById(R.id.tv_name);
//        TextView mId = mView.findViewById(R.id.tv_id);
//        mName.setText(mUserList.get(position).getName());
//        mId.setText(mUserList.get(position).getId().toString());
        return mView;
    }

}
