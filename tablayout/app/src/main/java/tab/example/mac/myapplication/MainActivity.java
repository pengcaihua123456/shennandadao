package tab.example.mac.myapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    TabLayout tabLayout;
    ViewPager viewPager;
    private Fragment[] mFragmentArrays = new Fragment[8];
    private String[] mTabTitles = new String[8];

    private  void initView(){
        tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        viewPager=(ViewPager)findViewById(R.id.view_pager);

        mFragmentArrays[0]=new TabFragment();
        mFragmentArrays[1]=new TabFragment();
        mFragmentArrays[2]=new TabFragment();
        mFragmentArrays[3]=new TabFragment();
        mFragmentArrays[4]=new TabFragment();
        mFragmentArrays[5]=new TabFragment();
        mFragmentArrays[6]=new TabFragment();
        mFragmentArrays[7]=new TabFragment();

        mTabTitles[0]="推荐";
        mTabTitles[1]="关注";
        mTabTitles[2]="深圳";
        mTabTitles[3]="国际";
        mTabTitles[4]="财经";
        mTabTitles[5]="职场";
        mTabTitles[6]="法制";
        mTabTitles[7]="漫画";

        MyViewPagerAdapter myViewPagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myViewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);//绑定viewpager和Tablayout

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE); //滚动模式 ,可左右滚动,类今日头条

    }



    final class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {//标题的现实
            return mTabTitles[position];
        }
    }


}
