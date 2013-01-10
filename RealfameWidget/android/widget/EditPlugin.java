package android.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.android.music.R;
/** @hide */
public class EditPlugin extends Button implements OnClickListener, OnTouchListener{
	private static final String TAG = "EditPlugin";
	public static final int STATE_INIT = 0;
	public static final int STATE_EDIT = 1;
	public static final int STATE_DONE = 2;
	
	public static final int DEFAULT_ANIMATION_STYLE = 0;
	public static final int PART_ANIMATION_STYLE = 1;
	public static final int NO_ANIMATION_STYLE = 2;
	
	public static long DURATION = 200;
	public static final int SD_GAP = 210;
	public int DISTANCE = 38;
	
	private Context mContext = null;
	private int mState;
	private int mInitState;
	private int mAnimationStyle;
	
	private StateParams mStateParams;

	private float mDensity;
	private boolean playAnimation;
	private boolean isDeletehiding; 
	
	private View mView;
	private OnEditListener mListener;
  /** @hide */
	public EditPlugin(Context context) {
        this(context, null);
        init(context);
    }
    /** @hide */
    public EditPlugin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    /** @hide */
    public EditPlugin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    /** @hide */
    private void init(Context context){
    	mContext = context; 
    	mStateParams = new StateParams();
    	
    	mStateParams.mEditSrc = R.drawable.btn_edit;
    	mStateParams.mEditText = R.string.edit;
    	
    	mStateParams.mDoneSrc = R.drawable.btn_done;
    	mStateParams.mDoneText = R.string.done;

    	DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
    	mDensity = dm.density;
    	DISTANCE = (int)dpToPixel(DISTANCE);
    	
    	setGravity(Gravity.CENTER);
    	
    	mAnimationStyle = DEFAULT_ANIMATION_STYLE;
    	
    	mInitState = STATE_INIT;
    	
    	resetState();
		
    	setOnClickListener(this);
    }
    /** @hide */
    public void setView(View view){
    	mView = view;
    	
    	if(mView != null){
    		mView.setOnTouchListener(this);
    	}
    }
    /** @hide */
    public void setState(int state){
    	mState = state;
    	mInitState = state;
    	
    	if(mState == STATE_EDIT){
	    	setText(mStateParams.mDoneText);
			setBackgroundResource(mStateParams.mDoneSrc);
    	}else{
    		setText(mStateParams.mEditText);
			setBackgroundResource(mStateParams.mEditSrc);
    	}
    	
		if(mListener != null){
			mListener.onStateChanged(mState);
		}
    }
    /** @hide */
    public int getState(){
    	return mState;
    }
    /** @hide */
    public void setAnimtionStyle(int style){
    	mAnimationStyle = style;
    }
    /** @hide */
    public void setEditButton(int src, int textId){
    	mStateParams.mEditSrc = (src == 0?R.drawable.btn_edit:src);
    	mStateParams.mEditText = (textId == 0?R.string.edit:textId);
    }
    /** @hide */
    public void setDoneButton(int src, int textId){
    	mStateParams.mDoneSrc = (src == 0?R.drawable.btn_done:src);
    	mStateParams.mDoneText = (textId == 0?R.string.done:textId);
    }
    /** @hide */
    public void bindView(ViewGroup view, int position, OnEditListener listener){
    	mListener = listener;
    	
    	SwitchButton switchButton = null;
    	DeleteButton deleteButton = null;
    	
    	Object tag = view.getTag();
    	if(tag instanceof Holder){
    		Holder holder = (Holder)tag;
    		if(holder.mSwitchButton == null){
    			holder.mSwitchButton = (SwitchButton)view.findViewWithTag(SwitchButton.TAG);
    		}
    		switchButton = holder.mSwitchButton;
    		
    		if(holder.mDeleteButton == null){
    			holder.mDeleteButton = (DeleteButton)view.findViewWithTag(DeleteButton.TAG);
    		}
    		deleteButton = holder.mDeleteButton;
    	}else{
    		switchButton = (SwitchButton)view.findViewWithTag(SwitchButton.TAG);
    		deleteButton = (DeleteButton)view.findViewWithTag(DeleteButton.TAG);
    	}

		if(switchButton == null){
			throw new NullPointerException("Have not a SwitchButton");
		}
		
		switchButton.setView(view);
		switchButton.setPosition(position);
		switchButton.setListener(listener);
		switchButton.setEditPlugin(this);
    	
		if(deleteButton == null){
			throw new NullPointerException("Have not a deleteButton");
		}
		
		deleteButton.setView(view);
		deleteButton.setPosition(position);
		deleteButton.setListener(listener);
		deleteButton.setEditPlugin(this);
		
    	if(mState == STATE_EDIT){
    		
    		switchButton.setVisibility(View.VISIBLE);
    		deleteButton.setVisibility(View.GONE);
    		
			if(mAnimationStyle == DEFAULT_ANIMATION_STYLE){
	    		bindAnimation(view);
	    	}

    		if(mAnimationStyle != NO_ANIMATION_STYLE){
    			switchButton.bindAnimation(mState);
    		}
    		
    	}else if(mState == STATE_DONE){
    		if(deleteButton.getVisibility() == View.VISIBLE){
		    	switchButton.hideDeleteButton(false);
	    	}
    		
			if(mAnimationStyle != NO_ANIMATION_STYLE){
		    	switchButton.bindAnimation(mState);
    		}
			
    		if(mAnimationStyle == DEFAULT_ANIMATION_STYLE){
    			bindAnimation(view);
    		}
    		
    		switchButton.setVisibility(View.GONE);
    		deleteButton.setVisibility(View.GONE);
    	}else{
    		if(mAnimationStyle != NO_ANIMATION_STYLE){
    			clearAnimation(view);
    		}
    	}
    }
    /** @hide */
    public void resetState(){
    	SwitchButton.resetState();//modified by rancaihe, 20120928
    	
    	if(mState == mInitState){
    		if(mState == STATE_EDIT){
    	    	setText(mStateParams.mDoneText);
    			setBackgroundResource(mStateParams.mDoneSrc);
        	}else{
        		setText(mStateParams.mEditText);
    			setBackgroundResource(mStateParams.mEditSrc);
        	}
    		return;
    	}
    	mState = mInitState;
    	playAnimation = false;
    	
    	if(mState == STATE_EDIT){
	    	setText(mStateParams.mDoneText);
			setBackgroundResource(mStateParams.mDoneSrc);
    	}else{
    		setText(mStateParams.mEditText);
			setBackgroundResource(mStateParams.mEditSrc);
			
			if(mListener != null){
				mListener.onRefresh(mView);
			}
    	}
    	
		if(mListener != null){
			mListener.onStateChanged(mState);
		}
    }
    /** @hide */
    public void translate(View v, int fromX, int toY){
    	translate(v, fromX, toY, DURATION);
    }
    /** @hide */
    public void translate(View v, int fromX, int toY, long duration){
    	TranslateAnimation anim ;
    	if(playAnimation){
    		anim = new TranslateAnimation(fromX, toY, 0, 0);
    		anim.setDuration(duration);
    	}else{
    		anim = new TranslateAnimation(toY, toY, 0, 0);
    		anim.setDuration(0);
    	}
    	anim.setFillEnabled(true);
    	anim.setFillAfter(true);
    	
    	v.startAnimation(anim);
    }
    /** @hide */
    public void translate(View v){
    	TranslateAnimation anim;
    	
    	int left = 0;//v.getLeft(); fix bug SSIXTEEN-411, rancaihe, 20121010
    	if(mState == STATE_EDIT){
    		translate(v, left, left + DISTANCE);
    	}else{
    		if(playAnimation){
    			translate(v, left + DISTANCE, left);
    		}else{
    			v.clearAnimation();
    		}
    	}
    }
    
    public void setDuration(long duration){
    	DURATION = duration;
    }
    
    public long getDuration(){
    	return DURATION;
    }
    
    public void setDistance(int distance){
    	DISTANCE = distance;
    }
    
    /** @hide */
    private void bindAnimation(ViewGroup v){
    	for(int i = 0; i < v.getChildCount(); i++){
    		View view = v.getChildAt(i);
    		
    		if(!(view instanceof SwitchButton) && !(view instanceof DeleteButton)){
    			translate(view);
    		}
    	}
    }
    
    /** @hide */
    public void clearAnimation(ViewGroup v){
    	if(mAnimationStyle != NO_ANIMATION_STYLE){
	    	for(int i = 0; i < v.getChildCount(); i++){
	    		View child = v.getChildAt(i);
	    		child.clearAnimation();
	    		
	    		if(child instanceof SwitchButton){
	    			child.setVisibility(View.GONE);
	    		}
	    	
		    	if(child instanceof DeleteButton){
		    		child.setVisibility(View.GONE);
	    		}
	    	}
    	}
    }
    /** @hide */
    public static interface OnEditListener{
    	void onStateChanged(int state);
    	void onSwitch(View v, int state);
    	void onDelete(View v, int position);
    	boolean onRefresh(View v);
    	boolean onViewTouch(View v, MotionEvent event);
    }
    /** @hide */
    private float dpToPixel(float dp){
    	return dp * mDensity;
    }
    /** @hide */
    private class StateParams{
    	int mEditSrc;
    	int mEditText;
    	
    	int mDoneSrc;
    	int mDoneText;
    }
  /** @hide */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		playAnimation = true;
		if(mState == STATE_EDIT){
			mState = STATE_DONE;
			
			setText(mStateParams.mEditText);
			setBackgroundResource(mStateParams.mEditSrc);
		}else{
			mState = STATE_EDIT;
			setText(mStateParams.mDoneText);
			setBackgroundResource(mStateParams.mDoneSrc);
		}
		if(mListener != null){
			mListener.onStateChanged(mState);
			mListener.onRefresh(mView);
		}
	}

	public static interface Listener{
		/** @hide */
    	void setView(View view);
    	/** @hide */
    	void setPosition(int position);
    	/** @hide */
    	void setListener(OnEditListener listener);
    	/** @hide */
    	void setEditPlugin(EditPlugin plugin);
    }
	/** @hide */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(!hasFocus){
			resetState();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		playAnimation = false;

		View view = SwitchButton.getDeleteView();
		if(view != null || isDeletehiding){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				isDeletehiding = true;
				SwitchButton switchButton =(SwitchButton)view.findViewWithTag(SwitchButton.TAG);
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
		
		return false;
	}
	
	public static void hideDeleteButton(){
		View view = SwitchButton.getDeleteView();
		if(view != null){
			SwitchButton switchButton =(SwitchButton)view.findViewWithTag(SwitchButton.TAG);
			switchButton.hideDeleteButton(false);
		}
	}
	
	public void setPlayAnimation(boolean play){
		playAnimation = play;
	}
	
	public boolean getPlayAnimation(){
		return playAnimation;
	}
	
	public static class Holder{
		SwitchButton mSwitchButton;
		DeleteButton mDeleteButton;
	}
}
