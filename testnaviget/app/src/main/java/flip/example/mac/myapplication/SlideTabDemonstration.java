package flip.example.mac.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客地址：http://blog.csdn.net/u012814441
 * SlideTab控件使用演示
 * Created by Edward on 2016/6/16.
 */
public class SlideTabDemonstration extends FragmentActivity {
    private ViewPager viewPager;
    private SlideTabView slideTab;
    private FragmentAdapter fragmentAdapter;
    //ViewPage选项卡页面列表
    private List<Fragment> fragments;
    private String[] title = {"Edward", "EdwardSlideTab"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_tab_demonstration);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        slideTab = (SlideTabView) findViewById(R.id.slide_tab);

        ArrayList<BaseTabEntity> baseTabEntities=new ArrayList<>();
        baseTabEntities.add(new TabEntity("健康运动"));
        baseTabEntities.add(new TabEntity("跑步"));
        baseTabEntities.add(new TabEntity("徒步"));
        baseTabEntities.add(new TabEntity("大哥"));
        baseTabEntities.add(new TabEntity("天哪"));


        fragments = new ArrayList<>();
        for (int i = 0; i < baseTabEntities.size(); i++) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", 'A' + i);
            //将值传递给TabFragment页面
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);
        slideTab.setViewPager(viewPager, baseTabEntities);
    }
}
