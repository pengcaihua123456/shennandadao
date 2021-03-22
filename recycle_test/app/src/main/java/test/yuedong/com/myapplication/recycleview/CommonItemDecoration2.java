package test.yuedong.com.myapplication.recycleview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by virl on 15/11/29.
 */
public class CommonItemDecoration2 extends RecyclerView.ItemDecoration {

    private int itemSpace;
    private int top;
    private int bottom;
    public CommonItemDecoration2(int itemSpace, int top, int bottom) {
        this.itemSpace = itemSpace;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if(position == 0) {
            outRect.set(0, top, 0, itemSpace);
        } else if(position == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, bottom);
        } else {
            outRect.set(0, 0, 0, itemSpace);
        }
    }
}
