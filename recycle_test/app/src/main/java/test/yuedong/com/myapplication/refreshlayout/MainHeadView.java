package test.yuedong.com.myapplication.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;

import test.yuedong.com.myapplication.R;


public class MainHeadView extends HeadView {


    public MainHeadView(Context context) {
        super(context, null);
    }

    public MainHeadView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MainHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getResId(){
        return R.layout.layout_loading_main;
    }

}
