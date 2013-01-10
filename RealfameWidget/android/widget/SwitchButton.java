/*
 * create by rancaihe, modified at 2012-12-10
 */
package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.ImageView.ScaleType;

import com.android.music.R;
/** @hide */
public class SwitchButton extends FrameLayout implements EditPlugin.Listener, OnClickListener, AnimationListener{
	public static final String TAG = "SwitchButton";
	
	public static final int STATE_NORMAL = 0;
	public static final int STATE_DELETE = 1;
	
	private int mState = STATE_NORMAL;
	View mView;
	private int mPosition;
	private EditPlugin.OnEditListener mListener;
	private EditPlugin mEditPlugin;
	private DeleteButton mDeleteButton;
	private static View mDeleteView = null;
	private View mTouchView;
	private boolean isDeletehiding; 
	private ImageView mIndicator;
	
	public SwitchButton(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	/** @hide */
	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	/** @hide */
	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	/** @hide */
	private void init(){
		setTag(TAG);
		setBackgroundResource(R.drawable.list_plugin_edit);
		
		setOnClickListener(this);

        addIndicatorView();
	}

    //add by rancaihe, 20120807
	private void addIndicatorView(){
		mIndicator = new ImageView(getContext());
		mIndicator.setImageResource(R.drawable.list_plugin_edit_indicator);
		mIndicator.setScaleType(ScaleType.CENTER);
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);
		
		addView(mIndicator, lp);
	}

	/** @hide */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mState == STATE_NORMAL){
			if(mDeleteView != null){
				SwitchButton switchButton =(SwitchButton)mDeleteView.findViewWithTag(TAG);
				switchButton.hideDeleteButton();
				return;
			}
			mState = STATE_DELETE;

			showDeleteButton();
		}else{
			mState = STATE_NORMAL;
	
			hideDeleteButton();
		}
	}
	/** @hide */
	private void showDeleteButton(){
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.delete_indicator_on);
		animation.setFillEnabled(true);
		mIndicator.startAnimation(animation);
		
		if(mDeleteButton == null){
			mDeleteButton = (DeleteButton)mView.findViewWithTag(DeleteButton.TAG);
			mDeleteButton.setPosition(mPosition);
			mDeleteButton.setListener(mListener);
			mDeleteButton.setEditPlugin(mEditPlugin);
	  }
		if(mDeleteButton != null){
			mDeleteButton.setVisibility(View.VISIBLE);
			
			Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.delete_button_on);
			anim.setFillEnabled(true);
			mDeleteButton.startAnimation(anim);
			mDeleteButton.setClickable(true);
			
			mDeleteView = mView;

            if(mListener != null){
			    mListener.onSwitch(mView, mState);
		    }
		}
	}
	/** @hide */
	public void hideDeleteButton(){
		hideDeleteButton(true);
	}
	
	public void hideDeleteButton(boolean playAnimation){
		if (playAnimation){
		    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.delete_indicator_off);
		    animation.setFillEnabled(true);
		    mIndicator.startAnimation(animation);
		}else{
			mIndicator.clearAnimation();
		}
		
		if(mDeleteButton != null){
			if (playAnimation){
			    Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.delete_button_off);
			    anim.setFillEnabled(true);
			    mDeleteButton.startAnimation(anim);
			}else{
				mDeleteButton.clearAnimation();
			}
			
			mDeleteButton.setVisibility(View.GONE);  
			mDeleteButton.setClickable(false);
			
			mDeleteView = null;
		}
		
		mState = STATE_NORMAL;
		
		if(mListener != null){
			mListener.onSwitch(mView, mState);
		}
	}
	
	/** @hide */
	public void bindAnimation(int state){
		if(mEditPlugin == null){
			return;
		}
		boolean playAnimation = mEditPlugin.getPlayAnimation();
		if(!playAnimation){
			clearAnimation();
		}else{
			if(state == EditPlugin.STATE_EDIT){
				Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.delete_indicator_show);
				anim.setFillEnabled(true);
				anim.setDuration(mEditPlugin.getDuration());
				startAnimation(anim);
			}else if(state == EditPlugin.STATE_DONE){
				Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.delete_indicator_hide);
				anim.setFillEnabled(true);
				anim.setDuration(mEditPlugin.getDuration());
				anim.setAnimationListener(this);
				startAnimation(anim);
			}
		}
	}
	
	/** @hide */
	public static void resetState(){
		if(mDeleteView != null){
			SwitchButton switchButton =(SwitchButton)mDeleteView.findViewWithTag(TAG);
			DeleteButton deleteButton =(DeleteButton)mDeleteView.findViewWithTag(DeleteButton.TAG);
			
			deleteButton.clearAnimation();
			deleteButton.setVisibility(View.GONE);  
			/*
			Animation anim = AnimationUtils.loadAnimation(switchButton.mContext, R.anim.delete_button_off);
			anim.setFillEnabled(true);
			anim.setDuration(0);
			deleteButton.startAnimation(anim);*/
			
			deleteButton.setClickable(false);
			
			switchButton.reset();
		}
	}
	
	private void reset(){
			mDeleteView = null;
			isDeletehiding = false;
			mState = STATE_NORMAL;
			if(mEditPlugin != null){
				mEditPlugin.setPlayAnimation(false);
			}
			mIndicator.clearAnimation();
	}
	/** @hide */
	public void setTouchView(View v){
		mTouchView = v;
		mTouchView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(mDeleteView != null || isDeletehiding){
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						isDeletehiding = true;
						SwitchButton switchButton =(SwitchButton)mDeleteView.findViewWithTag(TAG);
						switchButton.hideDeleteButton();
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						isDeletehiding = false;
						break;
					}
					
					return true;
				}

				return mListener.onViewTouch(v, event);
			}
		});
	}

    public static View getDeleteView(){
			return mDeleteView;
	}

  /** @hide */
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAnimationEnd");
		if(mEditPlugin != null){
			mEditPlugin.clearAnimation((ViewGroup)mView);
		}
	}
  /** @hide */
	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
  /** @hide */
	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		mView = view;
	}
  /** @hide */
	@Override
	public void setPosition(int position) {
		// TODO Auto-generated method stub
		mPosition = position;
	}
  /** @hide */
	@Override
	public void setListener(EditPlugin.OnEditListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
	}
	/** @hide */
	@Override
	public void setEditPlugin(EditPlugin plugin){
		mEditPlugin = plugin;
	}
}
