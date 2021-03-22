package coor.example.mac.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AuthorViewHolder> {

    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.item_list, parent, false);
        AuthorViewHolder viewHolder = new AuthorViewHolder(childView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    @Override
    public void onBindViewHolder(AuthorViewHolder holder, int position) {

    }

    class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView mNickNameView;
        TextView mMottoView;

        public AuthorViewHolder(View itemView) {
            super(itemView);

            mNickNameView = (TextView) itemView.findViewById(R.id.tv_title);
            mMottoView = (TextView) itemView.findViewById(R.id.tv_desc);

        }
    }
}