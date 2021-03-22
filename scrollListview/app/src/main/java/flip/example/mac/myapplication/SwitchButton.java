package flip.example.mac.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by mundane on 2017/4/4
 */

public class SwitchButton extends View implements View.OnClickListener {

	private float switchViewStrockWidth;
	private int   switchViewBgColor;
	private int   switchViewBallColor;
	private Paint mBallPaint;
	private Paint mBgPaint;
	private int   mViewHeight;
	private int   mViewWidth;
	private int   mStrokeRadius;
	private float mSolidRadius;
	private RectF mBgStrokeRectF;
	private int   BALL_X_RIGHT;

	public SwitchButton(Context context) {
		this(context, null);
	}

	public SwitchButton(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchView, defStyleAttr, R.style.def_switch_view);
		int indexCount = typedArray.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
				case R.styleable.SwitchView_switch_bg_color:
					switchViewBgColor = typedArray.getColor(attr, Color.BLACK);
					break;
				case R.styleable.SwitchView_switch_ball_color:
					switchViewBallColor = typedArray.getColor(attr, Color.BLACK);
					break;
			}
		}
		typedArray.recycle();
		initData();
	}

	private int greyColor;
	private int greenColor;
	private void initData() {

		greyColor = switchViewBgColor;
		greenColor = Color.parseColor("#1AAC19");

		mBallPaint = createPaint(switchViewBallColor, 0, Paint.Style.FILL, 0);
		mBgPaint = createPaint(switchViewBgColor, 0, Paint.Style.FILL, 0);
		mCurrentState = State.CLOSE;
		setOnClickListener(this);
	}

	private float mSwitchBallx;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mViewHeight = h;
		mViewWidth = w;

		//	默认描边宽度是控件宽度的1/30, 比如控件宽度是120dp, 描边宽度就是4dp
		switchViewStrockWidth = w * 1.0f / 30;

		mStrokeRadius = mViewHeight / 2;
		mSolidRadius = (mViewHeight - 2 * switchViewStrockWidth) / 2;
		BALL_X_RIGHT = mViewWidth - mStrokeRadius;


		mSwitchBallx = mStrokeRadius;
		mBgStrokeRectF = new RectF(0, 0, mViewWidth, mViewHeight);

	}

	private static final int DEF_H = 60;
	private static final int DEF_W = 120;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int measureWidth;
		int measureHeight;

		switch (widthMode) {
			case MeasureSpec.UNSPECIFIED:
			case MeasureSpec.AT_MOST://wrap_content
				measureWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_W, getResources().getDisplayMetrics());
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
				break;
			case MeasureSpec.EXACTLY:
				break;
		}

		switch (heightMode) {
			case MeasureSpec.UNSPECIFIED:
			case MeasureSpec.AT_MOST://wrap_content
				measureHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_H, getResources().getDisplayMetrics());
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
				break;
			case MeasureSpec.EXACTLY:
				break;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawSwitchBg(canvas);
		drawSwitchBall(canvas);
	}

	private void drawSwitchBall(Canvas canvas) {
		canvas.drawCircle(mSwitchBallx, mStrokeRadius, mSolidRadius, mBallPaint);
	}

	private void drawSwitchBg(Canvas canvas) {
		canvas.drawRoundRect(mBgStrokeRectF, mStrokeRadius, mStrokeRadius, mBgPaint);
	}

	private Paint createPaint(int paintColor, int textSize, Paint.Style style, int lineWidth) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(paintColor);
		paint.setStrokeWidth(lineWidth);
		paint.setDither(true);//设置防抖动
		paint.setTextSize(textSize);
		paint.setStyle(style);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		return paint;
	}

	private enum State {
		OPEN, CLOSE
	}

	private State mCurrentState;

	public interface OnCheckedChangeListener {
		void onCheckedChanged(SwitchButton buttonView, boolean isChecked);
	}

	private OnCheckedChangeListener mOnCheckedChangeListener;

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		this.mOnCheckedChangeListener = listener;
	}

	@Override
	public void onClick(View v) {
		mCurrentState = (mCurrentState == State.CLOSE ? State.OPEN : State.CLOSE);
		//绿色	#1AAC19
		//灰色	#999999
		if (mCurrentState == State.CLOSE) {
			animate(BALL_X_RIGHT, mStrokeRadius, greenColor, greyColor);
		} else {
			animate(mStrokeRadius, BALL_X_RIGHT, greyColor, greenColor);
		}
		if (mOnCheckedChangeListener != null) {
			if (mCurrentState == State.OPEN) {
				mOnCheckedChangeListener.onCheckedChanged(this, true);
			} else {
				mOnCheckedChangeListener.onCheckedChanged(this, false);
			}
		}
	}

	private void animate(int from, int to, int startColor, int endColor) {
		ValueAnimator translate = ValueAnimator.ofFloat(from, to);
		translate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mSwitchBallx = ((float) animation.getAnimatedValue());
				postInvalidate();
			}
		});

		ValueAnimator color = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
		color.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				switchViewBgColor = ((int) animation.getAnimatedValue());
				mBgPaint.setColor(switchViewBgColor);
				postInvalidate();
			}
		});

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(translate, color);
		animatorSet.setDuration(200);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				setClickable(false);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				setClickable(true);
			}
		});
		animatorSet.start();
	}
}
