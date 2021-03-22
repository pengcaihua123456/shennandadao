package flip.example.mac.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 滑动Tab
 * Created by peng caihua
 */
public class SlideTabView extends HorizontalScrollView {
    private Context context;
    private ViewPager viewPager;
    /**
     * HorizontalScrollView类的子控件容器
     */
    private LinearLayout horizontalContainer;
    /**
     * Tab容器
     */
    private LinearLayout tabVerticalContainer;
    /**
     * 当前页面下标
     */
    private int currentPosition = 0;
    /**
     * 指示器，下划线及分隔线的油漆类
     */
    private Paint indicatorPaint, underlinePaint;
    /**
     * 下划线高度默认为5
     */
    private int underlineHeight = 10;
    /**
     * 指示器的高度默认为10
     */
    private int indicatorHeight = 10;
    /**
     * 当前偏移量
     */
    private float currentPositionOffset = 0;
    /**
     * 记录最近一次滚动的位置，默认为0
     */
    private int lastScrollX = 0;
    /**
     * 剩余滚动偏移量
     */
    private int remainOffset = 100;
    /**
     * 是否展开Tab，默认为展开
     */
    private boolean isExtendTab = false;
    /**
     * 布局参数变量
     */
    private LinearLayout.LayoutParams textViewLayoutParams;
    /**
     * 两个布局参数变量
     */
    private LinearLayout.LayoutParams defaultTabLayoutParams, expandedTabLayoutParams;
    /**
     * 游标的颜色，默认为浅蓝色
     */
    private int indicatorColor = Color.parseColor("#31b2f7");

    public SlideTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setFillViewport(true);
        init();
    }

    /**
     * 初始化
     */
    public void init() {

        indicatorPaint = new Paint();
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setColor(indicatorColor);
        //填充
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setStrokeWidth(indicatorHeight);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        underlinePaint = new Paint();
        underlinePaint.setAntiAlias(true);
        underlinePaint.setColor(Color.parseColor("#e8ebee"));
        underlinePaint.setStrokeWidth(underlineHeight);


        defaultTabLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT, 1.0f);

        textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(10+PADINGG, 10, 10+PADINGG, 10);

//        setWillNotDraw(false);
        //创建水平容器
        horizontalContainer = new LinearLayout(context);
        horizontalContainer.setOrientation(LinearLayout.HORIZONTAL);
        horizontalContainer.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(horizontalContainer);

        setBackgroundColor(Color.WHITE);
    }


    private static final int PADINGG=20;

    private ArrayList<BaseTabEntity> mTabEntitys = new ArrayList<>();

    /**
     * @param viewPager 用户传进来的ViewPager
     * @para
     */
    public void setViewPager(ViewPager viewPager, ArrayList<BaseTabEntity> tabEntitys) {
        this.viewPager = viewPager;

        addTab(tabEntitys);
        //设置ViewPager监听事件
        viewPager.addOnPageChangeListener(new SlideTabPageViewListener());
    }

    public void setTabData(ArrayList<BaseTabEntity> tabEntitys, ViewPager viewPager) {

        this.mTabEntitys.clear();
        this.mTabEntitys.addAll(tabEntitys);

        setViewPager(viewPager, tabEntitys);
    }

    /**
     * 添加Tab
     *
     * @param
     */
    private void addTab(ArrayList<BaseTabEntity> tabEntitys) {
        //清空所有控件
        horizontalContainer.removeAllViews();

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            //创建垂直容器，用来包裹住下面TextView
            tabVerticalContainer = new LinearLayout(context);
            tabVerticalContainer.setOrientation(LinearLayout.VERTICAL);
            tabVerticalContainer.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            //设置点击事件
            tabVerticalContainer.setOnClickListener(new ViewPagerClickListener(i));
            tabVerticalContainer.setVerticalGravity(Gravity.CENTER_VERTICAL);
            //将垂直LinearLayout容器放入水平LinearLayout容器中
            horizontalContainer.addView(tabVerticalContainer, isExtendTab ? expandedTabLayoutParams
                    : defaultTabLayoutParams);

            if (tabEntitys != null) {
                //创建标题
                TextView textViews = new TextView(context);
                textViews.setText(tabEntitys.get(i).getTabTitle());
                textViews.setTextSize(DimenUtil.dp2px(context, 8));
                textViews.setTextColor(Color.parseColor("#000000"));
                textViews.setSingleLine(true);
                tabVerticalContainer.addView(textViews, textViewLayoutParams);
            }
        }
    }

    private OnYDTabSelectListener mListener;

    /**
     * ViewPager点击事件
     */
    public class ViewPagerClickListener implements OnClickListener {
        private int index;

        public ViewPagerClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onTabSelect(index);
            }
            currentPosition = index;
//            viewPager.setCurrentItem(index);
        }
    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取当前Tab的左右两边的横坐标值
        View currentTabView = horizontalContainer.getChildAt(currentPosition);


        float currentTabLeftX = 0;
        float currentTabRightX = 0;
        if (currentTabView != null) {
            currentTabLeftX = currentTabView.getLeft();
            currentTabRightX = currentTabView.getRight();
        }

        int childCount = horizontalContainer.getChildCount();

        //ViewPager在滑动的过程中会重复调用onDraw方法。下面if语句的内容是用来计算游标的起点坐标和终点坐标
        if (currentPositionOffset > 0f && currentPosition < childCount - 1) {
            //获取下一个Tab左右两边的横坐标值
            View nextTab = horizontalContainer.getChildAt(currentPosition + 1);
            float nextTabLeftX = nextTab.getLeft();
            float nextTabRightX = nextTab.getRight();
            //计算起点
            currentTabLeftX = (currentPositionOffset * nextTabLeftX + (1f - currentPositionOffset)
                    * currentTabLeftX);
            //计算终点
            currentTabRightX = (currentPositionOffset * nextTabRightX + (1f - currentPositionOffset)
                    * currentTabRightX);
        }

        //绘制指示器
        drawIndicator(canvas, currentTabLeftX+PADINGG, currentTabRightX-PADINGG);
    }

    /**
     * 绘制游标
     *
     * @param canvas        SlideTab控件的画板
     * @param currentLeftX  标题控件的左坐标
     * @param currentRightX 标题控件的右坐标
     */
    private void drawIndicator(Canvas canvas, float currentLeftX, float currentRightX) {
        float indicatorMiddle = (indicatorPaint.getStrokeWidth() / 2);
        float indicatorY = getHeight() - indicatorMiddle;
        canvas.drawLine(currentLeftX, indicatorY, currentRightX, indicatorY, indicatorPaint);
    }

    /**
     * 滚动Tab
     *
     * @param position
     * @param offset
     */
    public void scrollToCurrentPosition(int position, int offset) {
        int currentOffsetX = horizontalContainer.getChildAt(position).getLeft() + offset;
        int startScrollX = currentOffsetX;

        if (position > 0 || offset > 0) {
            //remainOffset表示剩余偏移量
            startScrollX = currentOffsetX - remainOffset;
        }

        //如果位移发生变化，则滑动
        if (startScrollX != lastScrollX) {
            //更新最后一次滑动的距离
            lastScrollX = startScrollX;
            //horizontalContainer控件开始滑动
            scrollTo(startScrollX, 0);
        }
    }

    /**
     * 需要动态更新
     */
    public void notifyDataSetChanged() {


    }

    public void setCurrentTab(int currentTab) {


    }


    public void setOnTabSelectListener(OnYDTabSelectListener listener) {
        this.mListener = listener;
    }


    /**
     * ViewPager滚动监听事件
     */
    public class SlideTabPageViewListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;


            int offset=30;

            if(horizontalContainer!=null){
                try {
                    offset=(int)(positionOffset * (horizontalContainer)
                            .getChildAt(position).getWidth());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            scrollToCurrentPosition(position, offset);

            //重新绘制onDraw方法
            invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
        }
    }
}
