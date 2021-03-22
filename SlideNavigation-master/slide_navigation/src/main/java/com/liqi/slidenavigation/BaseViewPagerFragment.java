package com.liqi.slidenavigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.liqi.fragment.BaseFragment;

/**
 * 带导航条的基类
 */
public abstract class BaseViewPagerFragment extends BaseFragment implements ViewPageFragmentAdapter.OnViewPageAdapterPageSelectedListener {

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected ViewGroup mTabsLayout;
    // 要展示的索引
    protected int mIndex = -1,
    // 设置viewpager缓存多少个
    mCacheLimit = 0;
    // 默认滑块背景图
    protected int mBackgroundId = R.drawable.sliding_tab_strip_background,
    // 默认滑动背景图
    mSlidingBlock = R.drawable.image_sliding_block;

    // 设置title颜色选择器ID
    protected int mTextColroSelect = R.color.selector_slide_title;
    //PagerSlidingTabStrip对象是否可以滑动,默认可以滑动
    protected boolean mSlidingTag = true;
    //PagerSlidingTabStrip对象title文本内容字体选择效果是否需要放大,默认是不放大
    protected boolean mTextSizeTag = false;
    //默认字体尺寸
    protected int mTextSize = 14;
    //默认放大字体尺寸
    protected int mTextZoomInSize = 16;
    //左边内边距.默认值-1
    protected int mPaddingLeft = -1,
    //顶部内边距.默认值-1
    mPaddingTop = -1,
    //右边内边距.默认值-1
    mPaddingRight = -1,
    //底部内边距.默认值-1
    mPaddingBottom = -1;
    protected boolean isTextTitleSizeCoarsening;
    //用户选择页面对象
    protected ViewPageInfo mViewPageInfo;
    //滑动控件显示控件方位枚举。默认顶部
    protected SlidingShowOrientationEnum mShowOrientationEnum = SlidingShowOrientationEnum.TOP;

    @Override
    public int setLiayoutId() {
        // TODO Auto-generated method stub
        return R.layout.base_viewpage_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        switch (mShowOrientationEnum) {
            case TOP:
                mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabstrip_top);
                view.findViewById(R.id.bottom_view).setVisibility(View.GONE);
                view.findViewById(R.id.tabstrip_bottom).setVisibility(View.GONE);
                break;
            case BOTTOM:
                mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabstrip_bottom);
                view.findViewById(R.id.top_view).setVisibility(View.GONE);
                view.findViewById(R.id.tabstrip_top).setVisibility(View.GONE);
                break;
        }
        mTabStrip.setBackgroundResource(mBackgroundId);
        mTabStrip.setSlidingBlockDrawable(getResources().getDrawable(
                mSlidingBlock));
        mTabStrip.slidingTag = this.mSlidingTag;
        mTabStrip.textSizeTag = this.mTextSizeTag;
        mTabStrip.setTextSize(mTextSize);
        mTabStrip.setTextZoomInSize(mTextZoomInSize);
        mTabStrip.setTextTitleSizeCoarsening(isTextTitleSizeCoarsening);
        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);
        mTabsAdapter.setPageSelectedListener(this);
        mTabsAdapter.mTextColroSelect = mTextColroSelect;
        mTabsAdapter.mTextSize = mTextSize;
        mTabsAdapter.mPaddingLeft=mPaddingLeft;
        mTabsAdapter.mPaddingTop=mPaddingTop;
        mTabsAdapter.mPaddingRight=mPaddingRight;
        mTabsAdapter.mPaddingBottom=mPaddingBottom;
        mViewPager.setOffscreenPageLimit(mCacheLimit);// 设置ViewPare一次性加载几个页面。
        // 回调方法给子类调用
        onSetupTabAdapter(mTabsAdapter);
        mTabsAdapter.notifyDataSetChanged();
        if (mIndex != -1) {
            // 设置要展示的页面索引值
            mViewPager.setCurrentItem(mIndex < 0 ? 0 : mIndex, true);
        }
        mTabsLayout = mTabStrip.getTabsLayoutTest();
        // 监听事件(点击之后隐藏红点)
        /*
         * mTabStrip.setOnClickTabListener(new OnClickTabListener() {
		 * 
		 * @Override public void onClickTab(View tab, int index) { View
		 * view=data.getChildAt(3); TextView viewText =(TextView) view;
		 * viewText.setText("哈哈"); } });
		 */
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == mViewPageInfo) {
            mViewPageInfo = mTabsAdapter.getInitListIndexData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mTabsAdapter) {
            mTabsAdapter.clear();
            mTabsAdapter = null;
        }
        mTabStrip = null;
        mTabsLayout = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mViewPager.getCurrentItem());
    }

    @Override
    public void onPageSelected(ViewPageInfo viewPageInfo) {
        mViewPageInfo = viewPageInfo;
    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}