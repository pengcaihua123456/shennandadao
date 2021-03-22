package com.liqi.demo.slide_navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.liqi.demo.R;
import com.liqi.fragment.BaseFragment;
import com.liqi.slidenavigation.ExploreViewPagerFragment;
import com.liqi.slidenavigation.SlidingShowOrientationEnum;

import java.util.ArrayList;

/**
 * 滑动导航界面
 * <p>
 * MVC模式.
 * 注意看注释和类注释。
 * </p>
 * <p>
 * Created by LiQi on 2017/12/8.
 */

public class TestSlideNavigationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_slide_navigation_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, getExploreViewPagerFragment()).commit();
    }

    /**
     * 获取适配好的片段管理对象
     *
     * @return
     */
    private ExploreViewPagerFragment getExploreViewPagerFragment() {
        //要展示的页面集合
        ArrayList<Class<? extends BaseFragment>> clazzList = new ArrayList<>();
        clazzList.add(TestSlideNavigationFragment.class);
        clazzList.add(TestSlideNavigationFragment.class);
        clazzList.add(TestSlideNavigationFragment.class);
        clazzList.add(TestSlideNavigationFragment.class);
        clazzList.add(TestSlideNavigationFragment.class);

        String[] titleNames=new String[]{"界面6","界面7","界面8","界面9","界面10"};

        //此处请点进去看调用方法注释
        return ExploreViewPagerFragment
                .newInstance()
                //单个添加
                .addFragment("界面1",TestSlideNavigationFragment.class)
                .addFragment("界面2",TestSlideNavigationFragment.class)
                .addFragment("界面3",TestSlideNavigationFragment.class)
                .addFragment("界面4",TestSlideNavigationFragment.class)
                .addFragment("界面5",TestSlideNavigationFragment.class)
                .addFragment("界面6",TestSlideNavigationFragment.class)
                .addFragment("界面7",TestSlideNavigationFragment.class)
                .addFragment("界面8",TestSlideNavigationFragment.class)
                .addFragment("界面9",TestSlideNavigationFragment.class)
                .addFragment("界面10",TestSlideNavigationFragment.class)
                //批量添加
                //.addFragment(titleNames, clazzList)
                .setViewpagerCacheLimit(clazzList.size())
                .setTextTitleSize(18)
                .setSlidingShowOrientation(SlidingShowOrientationEnum.BOTTOM)
                .setPadding(0,30,0,30)
                .setTextTitleSizeCoarsening(true)
                .setSlidingTabStripImage(
                        R.drawable.store_title_image_mr,
                        R.drawable.store_title_image_xz)
                .setTextColorSelect(R.color.blue_to_gray_s_c_p);
    }

}
