package com.liqi.demo.slide_navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.liqi.demo.R;
import com.liqi.fragment.BaseFragment;

/**
 * 滑动导航片段界面
 * <p>
 * 自己看类注释。
 * </p>
 * Created by LiQi on 2017/12/8.
 */

public class TestSlideNavigationFragment extends BaseFragment {
    private TextView textView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int index = getData();
        index++;
        textView = $(R.id.textView);
        textView.setText(index + "" + index + "" + index + ">>>>界面");
    }

    //如果要让BaseFragment内部去监听点击事件，那么请在控件里面设置android:clickable="true"
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                textView.setText(textView.getText().toString().trim() + "\n点击事件触发了\n");
                break;
        }
    }

    @Override
    public int setLiayoutId() {
        return R.layout.test_home_assembly_view_fragment;
    }

}
