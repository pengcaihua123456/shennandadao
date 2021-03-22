package test.yuedong.com.myapplication.recycleview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by LaiXiaodong
 * Date ï¼š 2016/6/22.
 */
public class ScrollSpeedLinearLayoutManager extends LinearLayoutManager {
    public ScrollSpeedLinearLayoutManager(Context context) {
        super(context);
    }

    private static final float MILLISECONDS_PER_INCH = 50f;

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return ScrollSpeedLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {//抓获异常，防止越界（recycleView 自动Bug）
            super.onLayoutChildren(recycler, state);
        } catch (Throwable e) {

        }
    }
}
