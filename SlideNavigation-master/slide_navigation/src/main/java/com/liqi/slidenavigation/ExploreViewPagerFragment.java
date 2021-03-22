package com.liqi.slidenavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.liqi.Logger;
import com.liqi.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 给ViewPeager每个页面赋值
 *
 * @author Liqi
 */
public class ExploreViewPagerFragment extends BaseViewPagerFragment {
    // 存储fragment对象的集合
    private ArrayList<ThisViewpagerFragmentObj> fragmentObjList;

    private ExploreViewPagerFragment() {
        if (null == fragmentObjList) {
            fragmentObjList = new ArrayList<>();
        }
    }

    public static ExploreViewPagerFragment newInstance() {
        return new ExploreViewPagerFragment();
    }

    /**
     * 设置当前那个页面显示
     *
     * @param index 页面索引值
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setViewpagerIndxe(int index) {
        this.mIndex = index;
        return this;
    }

    /**
     * 设置viewpager一次性加载几个对象
     *
     * @param cacheLimit 一次性加载数量
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setViewpagerCacheLimit(int cacheLimit) {
        this.mCacheLimit = cacheLimit;
        return this;
    }

    /**
     * 设置滑块默认图片和滑动时的图片
     *
     * @param backgroundId 默认图片
     * @param slidingBlock 滑动图片
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setSlidingTabStripImage(int backgroundId,
                                                            int slidingBlock) {
        this.mBackgroundId = backgroundId;
        this.mSlidingBlock = slidingBlock;
        return this;
    }

    /**
     * 设置滑块字体颜色选择器
     *
     * @param colorSelect 滑块字体颜色选择器
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setTextColorSelect(int colorSelect) {
        this.mTextColroSelect = colorSelect;
        return this;
    }

    /**
     * 设置标题滑块是否需要跟着手势滑动效果
     *
     * @param slidingTag true是滑动，false为不滑动。默认为true
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setSlidingTag(boolean slidingTag) {
        this.mSlidingTag = slidingTag;
        return this;
    }

    /**
     * 设置是否title字体选中后是否需要变大效果
     *
     * @param textSizeTag true是变大false为不变大。默认为false
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setTextTitleSizeTag(boolean textSizeTag) {
        this.mTextSizeTag = textSizeTag;
        return this;
    }

    /**
     * 设置选中的title字体是否变粗
     *
     * @param textTitleSizeCoarsening true是变粗false为不变粗。默认为false
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setTextTitleSizeCoarsening(boolean textTitleSizeCoarsening) {
        isTextTitleSizeCoarsening = textTitleSizeCoarsening;
        return this;
    }

    /**
     * 此对象实例化之后就初始化ViewPager片段页面
     */
    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        if (!fragmentObjList.isEmpty()) {
            for (int i = 0; i < fragmentObjList.size(); i++) {
                ThisViewpagerFragmentObj fragmentObj = fragmentObjList.get(i);
                Bundle bundle = new Bundle();
                bundle.putInt(i + "", i);
                adapter.addTab(fragmentObj.titleName, (BaseFragment) Fragment.instantiate(getContext(), fragmentObj.clazz.getName(), bundle));
            }
        } else {
            Logger.e("ExploreViewPagerFragment", "ExploreViewPagerFragment对象中fragmentObjList展示集合没有赋值对象,请调用setFragmentObjList()赋值");
        }
    }

    /**
     * 设置标题字体尺寸
     *
     * @param textSize 字体尺寸
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setTextTitleSize(int textSize) {
        this.mTextSize = textSize;
        return this;
    }

    /**
     * 设置标题放大字体尺寸
     *
     * @param textZoomInSize 标题放大字体尺寸
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setTextTitleZoomInSize(int textZoomInSize) {
        this.mTextZoomInSize = textZoomInSize;
        return this;
    }

    /**
     * 单个添加要显示的fragment界面和fragment标题名字
     *
     * @param titleName     fragment标题名字
     * @param fragmentClass 要显示的fragment界面class
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment addFragment(String titleName, Class<? extends BaseFragment> fragmentClass) {
        ThisViewpagerFragmentObj fragmentObj = new ThisViewpagerFragmentObj();
        fragmentObj.clazz = fragmentClass;
        fragmentObj.titleName = titleName;
        fragmentObjList.add(fragmentObj);
        return this;
    }

    /**
     * 设置滑动控件显示控件方位枚举。默认顶部
     *
     * @param showOrientationEnum 滑动控件显示控件方位枚举
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setSlidingShowOrientation(SlidingShowOrientationEnum showOrientationEnum) {
        mShowOrientationEnum = showOrientationEnum;
        return this;
    }

    /**
     * 设置滑块独立控件内边距。
     * <p>
     * 设置的值不能为负数,设置值单位为px.
     * <p>
     *
     * @param left   左边内边距.默认值-1
     * @param top    顶部内边距.默认值-1
     * @param right  右边内边距.默认值-1
     * @param bottom 底部内边距.默认值-1
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
        return this;
    }

    /**
     * 批量添加要显示的fragment界面和fragment标题名字
     *
     * @param titleName         fragment标题名字数组
     * @param fragmentClassList 要显示的fragment界面class集合
     * @return ExploreViewPagerFragment
     */
    public ExploreViewPagerFragment addFragment(String[] titleName, List<Class<? extends BaseFragment>> fragmentClassList) {
        if (null != fragmentClassList && !fragmentClassList.isEmpty()) {

            if (null != titleName) {
                for (int i = 0; i < fragmentClassList.size(); i++) {
                    // 显示的fragment对象不能大于标题名字的数组
                    if (i < titleName.length) {
                        ThisViewpagerFragmentObj fragmentObj = new ThisViewpagerFragmentObj();
                        fragmentObj.clazz = fragmentClassList.get(i);
                        fragmentObj.titleName = titleName[i];
                        fragmentObjList.add(fragmentObj);
                    }
                }
            }
        }
        return this;
    }

    /**
     * 运行当前显示界面捆绑的fragment显示方法
     */
    @Override
    public void onShow() {
        if (null != mViewPageInfo) {
            if (null != mViewPageInfo.mBaseFragment) {
                mViewPageInfo.mBaseFragment.onShow();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fragmentObjList != null && !fragmentObjList.isEmpty()) {
            fragmentObjList.clear();
            fragmentObjList = null;
        }
    }

    /**
     * fragment存储定义对象
     *
     * @author Liqi
     */
    static class ThisViewpagerFragmentObj {
        String titleName;
        Class<? extends BaseFragment> clazz;
    }
}
