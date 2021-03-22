package test.yuedong.com.myapplication.recycleview;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import test.yuedong.com.myapplication.R;
import test.yuedong.com.myapplication.base.NEImageView;
import test.yuedong.com.myapplication.base.ReleaseAble;
import test.yuedong.com.myapplication.bitmap.ResBitmapCache;


/**
 * Created by virl on 15/11/23.
 */
public class LoadMoreView extends FrameLayout implements/* RefreshLoadMoreRecyclerView.LoadMoreViewCallBack,*/ ReleaseAble {//
    public LoadMoreView(Context context) {
        super(context);

        imgLoading = new NEImageView(context);
        imgLoading.setNeBitmap(ResBitmapCache.instance().loadRes(getResources(), R.drawable.img_loading));
        if (sAnimation == null) {
            sAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            sAnimation.setDuration(kImgRotateDuration);
            sAnimation.setRepeatCount(Animation.INFINITE);
            sAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            sAnimation.setFillAfter(true);
            sAnimation.setRepeatMode(Animation.RESTART);
        }
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(imgLoading, params);
        setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.load_more_margin_bottom));
    }

    private static RotateAnimation sAnimation;
    private static final long kImgRotateDuration = 1000;
    private NEImageView imgLoading;

//    @Override
//    public void startLoadMore() {
//        if (sAnimation != null) {
//            imgLoading.startAnimation(sAnimation);
//        }
//    }
//
//    @Override
//    public void stopLoadMore() {
//        imgLoading.clearAnimation();
//    }

    @Override
    public void release() {
        if (sAnimation != null) {
            sAnimation.cancel();
            sAnimation = null;
        }
    }
}