package com.liqi.slidenavigation;


import com.liqi.fragment.BaseFragment;

public final class ViewPageInfo {

    //public final String tag;
    public final BaseFragment mBaseFragment;
    // public final Class<? extends BaseFragment> clss;
    //  public final Bundle args;
    public final String mTitle;

    //    public ViewPageInfo(String _title, String _tag, Class<? extends BaseFragment> _class, Bundle _args) {
//        title = _title;
//        tag = _tag;
//        clss = _class;
//        args = _args;
//    }
    public ViewPageInfo(String title, BaseFragment baseFragment) {
        mTitle = title;
        mBaseFragment = baseFragment;
    }
}
