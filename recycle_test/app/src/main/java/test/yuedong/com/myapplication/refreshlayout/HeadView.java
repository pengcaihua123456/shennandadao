package test.yuedong.com.myapplication.refreshlayout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import test.yuedong.com.myapplication.R;
import test.yuedong.com.myapplication.refreshlayout.inner.IHeaderView;
import test.yuedong.com.myapplication.refreshlayout.inner.OnAnimEndListener;

/**
 * Created by pc1 on 2017/10/18.
 */

public class HeadView extends FrameLayout implements IHeaderView {
    private static final char MSG_START = 0x00;
    private static final char MSG_STOP = 0x80;
    private ImageView imageView;
    private TextView textView;
    private String[] tips;
    private Random random;
    private int drawablesLength;
    private int[] drawables = new int[]{R.drawable.loading_action_new_1, R.drawable.loading_action_new_2,
            R.drawable.loading_action_new_3, R.drawable.loading_action_new_4, R.drawable.loading_action_new_5, R.drawable.loading_action_new_6,R.drawable.loading_action_new_7};

    public HeadView(Context context) {
        this(context, null);
    }

    public HeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView(context);
        random = new Random();
    }


    private void intView(Context context) {
        View rootView = View.inflate(context, getResId(), null);
        imageView = (ImageView) rootView.findViewById(R.id.loading_img);
        textView = (TextView) rootView.findViewById(R.id.loading_text);
        addView(rootView);
        drawablesLength = drawables.length;
    }

    public int getResId(){
        return R.layout.layout_loading;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (tips != null && tips.length > 0) {
            textView.setText(tips[random.nextInt() % tips.length]);
        }
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        if (mHandler != null)
            mHandler.sendEmptyMessage(MSG_START);
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        if (animEndListener != null)
            animEndListener.onAnimEnd();
        if (mHandler != null)
            mHandler.sendEmptyMessage(MSG_STOP);
    }

    @Override
    public void reset() {

    }

    @Override
    public void setTextContext(String[] tips) {
        this.tips = tips;
    }

    @Override
    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        this.removeAllViews();
    }

    private int index = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START:
                    imageView.setImageResource(drawables[index++]);
                    if (index >= drawablesLength) {
                        index = 0;
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_START, 150);
                    break;
                case MSG_STOP:
                    mHandler.removeCallbacksAndMessages(null);
                    index=0;
                    break;
            }
        }
    };
}
