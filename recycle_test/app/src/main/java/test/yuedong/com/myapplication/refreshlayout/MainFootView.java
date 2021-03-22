package test.yuedong.com.myapplication.refreshlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import test.yuedong.com.myapplication.R;
import test.yuedong.com.myapplication.refreshlayout.inner.IBottomView;
import test.yuedong.com.myapplication.refreshlayout.util.DensityUtil;


public class MainFootView extends TextView implements IBottomView {

    public static final int DEFAULT_SIZE = 50; //dp


    public MainFootView(Context context) {
        this(context, null);
    }

    public MainFootView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int default_size = DensityUtil.dp2px(context, DEFAULT_SIZE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, default_size, Gravity.CENTER);
        setLayoutParams(params);
        setText(getContext().getString(R.string.sroll_to_read_info));
        setTextSize(12);
        setTextColor(Color.parseColor("#999999"));
        setGravity(Gravity.CENTER);
    }


    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxHeadHeight, float headHeight) {
        setText(getContext().getString(R.string.sroll_to_read_info));
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        setText(getContext().getString(R.string.to_read_info));
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void reset() {

    }
}
