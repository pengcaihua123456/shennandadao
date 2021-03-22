package com.liqi.slidenavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.liqi.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * ViewPager页面切换赋值适配器
 *
 * @author LiQi
 */
class ViewPageFragmentAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    private final ArrayList<ViewPageInfo> MTABS = new ArrayList<>();
    // 设置title颜色选择器ID
    int mTextColroSelect,
    //字体大小
    mTextSize,
    //左边内边距
    mPaddingLeft,
    //顶部内边距
    mPaddingTop,
    //右边内边距
    mPaddingRight,
    //底部内边距
    mPaddingBottom;
    private PagerSlidingTabStrip mPagerStrip;
    private Context mContext;
    private ViewPager mViewPager;
    private OnViewPageAdapterPageSelectedListener mPageSelectedListener;

    ViewPageFragmentAdapter(FragmentManager fm,
                            PagerSlidingTabStrip pageStrip, ViewPager pager) {
        super(fm);
        mContext = pager.getContext();
        mPagerStrip = pageStrip;
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mPagerStrip.setViewPager(mViewPager);
        mPagerStrip.setOnPageChangeListener(this);
    }

    void addTab(String title, @NonNull BaseFragment baseFragment) {
        ViewPageInfo info = new ViewPageInfo(title, baseFragment);
        MTABS.add(info);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (ViewPageInfo viewPageInfo : MTABS) {
            TextView v = (TextView) LayoutInflater.from(mContext).inflate(
                    R.layout.sliding_tab_item, null);
            v.setText(viewPageInfo.mTitle);
            v.setTextSize(mTextSize);
            v.setTextColor(mContext.getResources().getColorStateList(mTextColroSelect));
            if (mPaddingLeft != -1 ||
                    mPaddingTop != -1 ||
                    mPaddingRight != -1 ||
                    mPaddingBottom != -1) {
                v.setPadding(mPaddingLeft >= 0 ? mPaddingLeft : 0, mPaddingTop >= 0 ? mPaddingTop : 0,
                        mPaddingRight >= 0 ? mPaddingRight : 0, mPaddingBottom >= 0 ? mPaddingBottom : 0);
            }
            // 需要在此处动态添加标题图片
            mPagerStrip.addTab(v);
        }
    }

    void clear() {
        if (!MTABS.isEmpty()) {
            MTABS.clear();
        }
        mContext = null;
        mPagerStrip = null;
        mViewPager = null;
    }

    @Override
    public int getCount() {
        return MTABS.size();
    }

    @Override
    public BaseFragment getItem(int position) {
        ViewPageInfo info = MTABS.get(position);
        info.mBaseFragment.setData(position);
        return info.mBaseFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MTABS.get(position).mTitle;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Log.e("ViewPaer切换", "ViewPaer切换001"+state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        //Log.e("ViewPaer切换", "ViewPaer切换002"+position);
    }

    @Override
    public void onPageSelected(int position) {
        ViewPageInfo viewPageInfo = MTABS.get(position);
        viewPageInfo.mBaseFragment.onShow();
        if (null != mPageSelectedListener) {
            mPageSelectedListener.onPageSelected(viewPageInfo);
        }
        //Log.e("ViewPaer切换", "ViewPaer切换003"+position);
    }

    ViewPageInfo getInitListIndexData() {
        if (!MTABS.isEmpty()) {
            return MTABS.get(0);
        }
        return null;
    }

    void setPageSelectedListener(OnViewPageAdapterPageSelectedListener pageSelectedListener) {
        mPageSelectedListener = pageSelectedListener;
    }

    interface OnViewPageAdapterPageSelectedListener {
        void onPageSelected(ViewPageInfo viewPageInfo);
    }
}