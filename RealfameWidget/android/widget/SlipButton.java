package android.widget;

//import android.animation.Animator;
//import android.animation.Animator.AnimatorListener;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
//import com.android.internal.R;
import com.android.inputmethod.pinyin.R;

/** @hide */
public class SlipButton extends View/* implements
		ValueAnimator.AnimatorUpdateListener, AnimatorListener*/ {
	private final String TAG = "SlipButton";
	private final int FULL_DURATION = 100;
	private Drawable mOpen;
	private Drawable mClose;
	private Drawable mBackground;
	private Drawable mSlip;
	private Drawable mSlipDown;
	private Drawable mLeftSide;
	private Drawable mRightSide;
	private Drawable mOverlayer;
	private Context mContext;
	private Resources mResource;

	private float mScale;
	private float mDownX;
	private float mSlipDownX;
	private float mSlipX;
	private int mSlipWidth;
	private int mIntrinsicWidth;
	private int mIntrinsicHeight;

	private Path mPath;

	private boolean mButtonDown;
	private boolean mIsAnimating = false;
	private boolean mChecked = false;
	private boolean mNeedClip = false;

	private OnCheckedChangeListener mListener;

	//private ObjectAnimator mLeftMove;
	//private ObjectAnimator mRightMove;

	private GestureDetector mGestureDetector;

	public SlipButton(Context context) {
		this(context, null, 0);
	}

	public SlipButton(Context context, OnCheckedChangeListener listener) {
		this(context, null, 0);
		mListener = listener;
	}

	public SlipButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		TypedArray a =
            context.obtainStyledAttributes(
                    attrs, R.styleable.SlipButton, defStyle, 0);
		
		mResource = mContext.getResources();
		
		mOpen = a.getDrawable(R.styleable.SlipButton_on_image);
		if (mOpen == null) {
        	mOpen = mResource.getDrawable(R.drawable.on_image);
        }
		
		mClose = a.getDrawable(R.styleable.SlipButton_off_image);
        if (mClose == null) {
        	mClose = mResource.getDrawable(R.drawable.off_image);
        }
        
        mSlip = a.getDrawable(R.styleable.SlipButton_thumb_normal);
		if (mSlip == null) {
        	mSlip = mResource.getDrawable(R.drawable.slip_btn);
        }
		
		mSlipDown = a.getDrawable(R.styleable.SlipButton_thumb_hight);
		if (mSlipDown == null) {
        	mSlipDown = mResource.getDrawable(R.drawable.slip_btn_down);
        }
    mOverlayer = a.getDrawable(R.styleable.SlipButton_overlayer);    
    if(mOverlayer == null){
			mOverlayer = mResource.getDrawable(R.drawable.slip_overlayer_default);
		}
		mLeftSide = mResource.getDrawable(R.drawable.left_side);
		mRightSide = mResource.getDrawable(R.drawable.right_side);
		
		mBackground = mResource.getDrawable(R.drawable.slip_background);
		init();
	}

	public SlipButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	private void init() {
		//setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mIntrinsicWidth = mOpen.getIntrinsicWidth();
		mIntrinsicHeight = mOpen.getIntrinsicHeight();
		
		mPath = new Path();
		mGestureDetector = new GestureDetector(
				new GestureDetector.OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						// TODO Auto-generated method stub
						//setupAnimators();
						if (mChecked) {
							//mLeftMove.start();
							mChecked = false;
							if (mListener != null) {
								mListener.onCheckedChanged(SlipButton.this,
										false);
							}
						} else {
							//mRightMove.start();
							mChecked = true;
							if (mListener != null) {
								mListener.onCheckedChanged(SlipButton.this,
										true);
							}
						}
						if (mButtonDown) {
							mButtonDown = false;
							mDownX = 0;
							mSlipDownX = 0;
						}
						return true;
					}

					@Override
					public void onShowPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		setFocusable(true);
		setClickable(true);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		float hScale = 1.0f;
		float vScale = 1.0f;

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mIntrinsicWidth) {
			hScale = (float) widthSize / (float) mIntrinsicWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED
				&& heightSize < mIntrinsicHeight) {
			vScale = (float) heightSize / (float) mIntrinsicHeight;
		}

		mScale = Math.min(hScale, vScale);

		/*
		setMeasuredDimension(super.resolveSizeAndState(
				(int) (mIntrinsicWidth * mScale), widthMeasureSpec, 0),
				super.resolveSizeAndState((int) (mIntrinsicHeight * mScale),
						heightMeasureSpec, 0));
		*/
		mSlipWidth = (int) (mScale * mSlip.getIntrinsicWidth());

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		resetDrawablesBounds(mChecked);
		//setupAnimators();
		setupClips();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(mNeedClip){
			canvas.save();
		  canvas.clipPath(mPath);
		}
		mOpen.draw(canvas);
		mClose.draw(canvas);
		mBackground.draw(canvas);
		if(mNeedClip){
			canvas.restore();
		}
		mOverlayer.draw(canvas);
		if (mButtonDown) {
			mSlipDown.draw(canvas);
		} else {
			mSlip.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mIsAnimating) {
			return true;
		}
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		getParent().requestDisallowInterceptTouchEvent(true);
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (mSlip.getBounds().contains((int) x, (int) y)) {
				mButtonDown = true;
				mDownX = x;
				mSlipDownX = (mSlip.getBounds().left + mSlip.getBounds().right) / 2;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			if (mButtonDown) {
				resetTouchEventState();
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mButtonDown) {
				trackDrawablesBounds((int) x);
			}
			break;
		}

		invalidate();
		return true;

	}

	private void resetDrawablesBounds(boolean on_off) {
		int height = (int) (mScale * mOpen.getIntrinsicHeight());
		mLeftSide.setBounds(-1,0,(int)(mScale * mLeftSide.getIntrinsicWidth()) - 1,height);
		mRightSide.setBounds(getWidth() - (int)(mScale * mRightSide.getIntrinsicWidth()) + 1,0,getWidth() + 1,height);
		if (!on_off) {
			mSlip.setBounds(0, 0, mSlipWidth, height);
			mSlipX = mSlipWidth / 2;
		} else {
			mSlip.setBounds(getWidth() - mSlipWidth, 0, getWidth(), height);
			mSlipX = (getWidth() - mSlipWidth / 2);
		}
		setDrawBoundsBySlip();
	}

	private void trackDrawablesBounds(int nowX) {
		mSlipX = (int) (nowX - mDownX + mSlipDownX);
		if (mSlipX < mSlipWidth / 2) {
			mSlipX = mSlipWidth / 2;
		} else if (mSlipX > getWidth() - mSlipWidth / 2) {
			mSlipX = getWidth() - mSlipWidth / 2;
		}
		if (mSlipX - mSlipWidth / 2 >= 0
				&& mSlipX + mSlipWidth / 2 <= getWidth()) {
			mSlip.setBounds((int) (mSlipX - mSlipWidth / 2), 0,
					(int) (mSlipX + mSlipWidth / 2), mSlip.getBounds().bottom);
		}
		setDrawBoundsBySlip();
	}
	private void animateDrawablesBounds(){
		if (mSlipX < mSlipWidth / 2) {
			mSlipX = mSlipWidth / 2;
		} else if (mSlipX > getWidth() - mSlipWidth / 2) {
			mSlipX = getWidth() - mSlipWidth / 2;
		}
		if (mSlipX - mSlipWidth / 2 >= 0
				&& mSlipX + mSlipWidth / 2 <= getWidth()) {
			mSlip.setBounds((int) (mSlipX - mSlipWidth / 2), 0,
					(int) (mSlipX + mSlipWidth / 2), mSlip.getBounds().bottom);
		}
		setDrawBoundsBySlip();
	}

	private void setDrawBoundsBySlip() {
		int width = getWidth();
		int height = (int) (mScale * mOpen.getIntrinsicHeight());
		int openLeft = 0;
		int openRight = 0;
		int slipLeft = 0;
		int slipRight = 0;
		int closeLeft = 0;
		int closeRight = 0;

		slipLeft = mSlip.getBounds().left;
		slipRight = mSlip.getBounds().right;
		openLeft = slipRight - width;
		openRight = openLeft + width;
		closeLeft = slipLeft;
		closeRight = closeLeft + width;

		mBackground.setBounds(0, 0, width, height);
		mOpen.setBounds(openLeft, 0, openRight, height);
		mClose.setBounds(closeLeft, 0, closeRight, height);
		mOverlayer.setBounds(0,0,width,height);
		mSlipDown.setBounds(mSlip.getBounds());
	}

	private void resetTouchEventState() {
		//setupAnimators();
		if (getSlipx() >= getWidth() / 2) {
			if (!mChecked) {
				mChecked = true;
				if (mListener != null) {
					mListener.onCheckedChanged(SlipButton.this, true);
				}
			}
			//mRightMove.start();
		} else {
			if (mChecked) {
				mChecked = false;
				if (mListener != null) {
					mListener.onCheckedChanged(SlipButton.this, false);
				}
			}
			//mLeftMove.start();
		}
		mButtonDown = false;
		mDownX = 0;
		mSlipDownX = 0;
	}

	public void setSlipx(float x) {
		mSlipX = x;
		animateDrawablesBounds();
	}

	public float getSlipx() {
		return mSlipX;
	}

	/*
	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		// TODO Auto-generated method stub
		invalidate();
	}

	private void setupAnimators() {
		int leftDuration = (int) ((getSlipx() - mSlipWidth / 2)
				/ (getWidth() - mSlipWidth) * FULL_DURATION);
		int rightDuration = (int) ((getWidth() - mSlipWidth / 2 - getSlipx())
				/ (getWidth() - mSlipWidth) * FULL_DURATION);
		mLeftMove = ObjectAnimator.ofFloat(this, "slipx", mSlipX,(mSlipWidth / 2));
		mLeftMove.setDuration(leftDuration);
		mLeftMove.addUpdateListener(this);
		mLeftMove.addListener(this);
		mRightMove = ObjectAnimator.ofFloat(this, "slipx",mSlipX,
				(getWidth() - mSlipWidth / 2));
		mRightMove.setDuration(rightDuration);
		mRightMove.addUpdateListener(this);
		mRightMove.addListener(this);
	}

	@Override
	public void onAnimationStart(Animator animation) {
		// TODO Auto-generated method stub
		mIsAnimating = true;
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		// TODO Auto-generated method stub
		mIsAnimating = false;
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub
		mIsAnimating = false;
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub
	}
	*/
	
	public boolean getChecked() {
		return mChecked;
	}

	public void setChecked(boolean checked) {
/*		if (mLeftMove != null && mLeftMove.isRunning()) {
			mLeftMove.cancel();
		}
		if (mRightMove != null && mRightMove.isRunning()) {
			mRightMove.cancel();
		}
*/		mChecked = checked;
		resetDrawablesBounds(mChecked);
		invalidate();
	}

	public interface OnCheckedChangeListener {
		public abstract void onCheckedChanged(SlipButton slipbutton, boolean isChecked);
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mListener = listener;
	}

	public OnCheckedChangeListener getOnCheckedChangeListener() {
		return mListener;
	}

	private void setupClips() {
		mPath.reset();
		mPath.addCircle(mSlipWidth / 2, mSlipWidth / 2, mSlipWidth / 2,	Path.Direction.CCW);
		mPath.addCircle(getWidth() - mSlipWidth / 2, mSlipWidth / 2, mSlipWidth / 2,	Path.Direction.CCW);
		mPath.addRect(mSlipWidth / 2, 0, getWidth() - mSlipWidth / 2,
		getHeight(), Path.Direction.CCW);
	}
	
	public void setOnImage(int res_id){
		mOpen = mResource.getDrawable(res_id);		
		setDrawBoundsBySlip();
		invalidate();
	}
	
	public void setOverlayImage(int res_id){
		mOverlayer = mResource.getDrawable(res_id);		
		setDrawBoundsBySlip();
		invalidate();
	}
	
	public void setNeedClip(boolean if_need){
		mNeedClip = if_need;
		invalidate();
	}
}
