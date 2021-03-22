package glidetest.yuedong.com.myapplication;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollerLayout extends ViewGroup {

    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;

    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;

    /**
     * 手机按下时的屏幕坐标
     */
    private float mXDown;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float mXMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mXLastMove;

    /**
     * 界面可滚动的左边界
     */
    private int leftBorder;

    /**
     * 界面可滚动的右边界
     */
    private int rightBorder;

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        /***
         * 这个值在后面将用于判断当前用户的操作是否是拖动。
         */
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    /***
     * viewGroup的测量方式
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
            }
            // 初始化左右边界值
            leftBorder = getChildAt(0).getLeft();//第一个控件的左边
            rightBorder = getChildAt(getChildCount() - 1).getRight();//最后一个控件的右边
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();//相对屏幕的距离
                mXLastMove = mXDown;
                Log.d("peng","onInterceptTouchEvent   ACTION_DOWN ");

                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    Log.w("peng","diff > mTouchSlop"+mXMove+"mXLastMove:"+mXLastMove);
                    return true;
                }
                Log.d("peng","onInterceptTouchEventACTION_MOVE ");

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     *
     * getScrollX();
     * 当前View视图左上角坐标与View视图初始位置x轴方向上的距离。
     *
     *
     * 那么当我们把事件拦截掉之后，就会将事件交给ScrollerLayout的onTouchEvent()方法来处理
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                mXMove = event.getRawX();//触摸点距离x的距离
                Log.i("peng","onTouchEvent_ACTION_MOVE"+mXMove);

                //mXLastMove  开始拖动的距离
                //mXMove  移动的距离
                int scrolledX = (int) (mXLastMove - mXMove);
                //不添加会滑出去
                if (getScrollX() + scrolledX < leftBorder) {
                    scrollTo(leftBorder, 0);//   左边-------左边边界条件,移动
                    Log.w("peng","scrollTo left"+leftBorder+"scrolledX="+scrolledX+"getScrollX();"+getScrollX());
                    return true;
                } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);//-------右边边界条件
                    Log.w("peng","scrollTo right"+rightBorder);
                    return true;
                }

                scrollBy(scrolledX, 0);//移动的差值就是滚动的距离
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                /**
                 * 回弹，滚动到哪个位置
                 */
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                Log.d("peng","onTouchEventACTION_UP");
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手滑动停止
     */
    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            Log.d("peng","computeScroll");
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());//回弹的效果,会移动
            invalidate();
        }
    }
}

