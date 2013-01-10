package android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
//import android.widget.SearchView.OnCloseListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnCloseListener;

import android.widget.TextView;

import com.android.sdcardapkscan.R;

public class SearchBar extends RelativeLayout implements OnTouchListener{
	private static String TAG = "SearchBar";
	
	private Context mContext;
	private OnQueryTextListener mOnQueryChangeListener;
	private OnSearchModeChangedListener mOnSearchModeChangedListener;
	private OnCloseListener mOnCloseListener;
	private EditText mQueryTextView;
	private CharSequence mOldQueryText;
	private View mCloseButton;
	private Button mCancelButton;
	private View mSearchBarView;
	private boolean mIsSearchState = false;
	private ListView mListView;
	private View mViewParent;
	private boolean mHasEffects;
	private List<View> mHeaderViewList;
	private TextView mTextView;
	private SearchView mSearchView;
	private View mTitleView;
	private View mEffect;
	private Bitmap mListViewCache = null;

	public interface OnQueryTextListener {
		boolean onQueryTextChange(String newText);
	}
	
	public interface OnSearchModeChangedListener {
    	void onSearchModeChanged( boolean isSeachMode);
   }	

	public SearchBar(Context context) {
		this(context, null);
	}

	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mHeaderViewList = new ArrayList<View>();
		mSearchView = new SearchView(mContext);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.search_bar_button, this, true);
		setBackgroundResource(R.drawable.searchbar_bg);
        mTextView = (TextView)findViewById(R.id.empty);
     
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mListView == null) {
					throw new IllegalArgumentException("must call setListView");
				}
				if (mViewParent == null) {
					throw new IllegalArgumentException(
							"must call setViewParent");
				}
				if (mListView.getCount() > (mListView
						.getHeaderViewsCount() + mListView.getFooterViewsCount())) {
					mIsSearchState = true;
					if (mOnSearchModeChangedListener != null) {
						mOnSearchModeChangedListener
								.onSearchModeChanged(mIsSearchState);
					}
					setEntryAndExitSearchViewAnimation(mIsSearchState);
				}
			}
		});
	}
	
    private void setEntryAndExitSearchViewAnimation(boolean is_search_view){
    	int title_height = (int)getResources().getDimension(R.dimen.ip_title_bar_height); 
    	if(is_search_view){
    		TranslateAnimation ScreenAnimation = new TranslateAnimation(0, 0, 0, -title_height);
    		ScreenAnimation.setDuration(100);
    		ScreenAnimation.setFillAfter(true);
    		ScreenAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					
					mViewParent.clearAnimation();
					int count = mListView.getHeaderViewsCount();
					mHeaderViewList.clear();
					for (int i = 0; i < count; i++) {
						mHeaderViewList.add(mListView.getChildAt(i));						
					}
					mListView.removeHeaderView(SearchBar.this);
					((ViewGroup)mViewParent).addView(mSearchView, 0);
					mQueryTextView.requestFocus();
					if (mTitleView != null){
					    mTitleView.setVisibility((mViewParent instanceof RelativeLayout)?View.INVISIBLE:View.GONE);
					}
					
					new Handler().post(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mSearchView.setListBlackBackground(true, false);	
							mSearchView.showSoftKeyboard();
						}
						
					});
					
				}
			});
    		mViewParent.startAnimation(ScreenAnimation);	

    	}else{
       		TranslateAnimation ScreenAnimation = new TranslateAnimation(0, 0, -title_height, 0);
       		ScreenAnimation.setDuration(100);
       		ScreenAnimation.setFillAfter(true);
    		mViewParent.startAnimation(ScreenAnimation);
    		if (mTitleView != null){
			     mTitleView.setVisibility(View.VISIBLE);
    		}
    		mSearchView.setListBlackBackground(false, true);
    	}
    }
    
	public void setOnQueryTextListener(OnQueryTextListener listener) {
		mOnQueryChangeListener = listener;
	}
	
	public void setOnSearchModeChangedListener(OnSearchModeChangedListener listener) {
		mOnSearchModeChangedListener = listener;
	}	

	public void setListView(ListView listView) {
		mListView = listView;
		mListView.setOnTouchListener(this);
	}

	public void setParenView(View parent) {
		mViewParent = parent;
	}
	
	public void setTitleBarView(View titleView){
		mTitleView = titleView;
	}
	
	public void setSearchBarBackgroundResource(int background){
		setBackgroundResource(background);	
	}

	public void setCancelButton(int backgroud, int textId, int textAppearence){
		if (backgroud != 0){
			mCancelButton.setBackgroundResource(backgroud);
		}		
		if (textId != 0){
		    mCancelButton.setText(textId);
		}
		if (textAppearence != 0){
			mCancelButton.setTextAppearance(mContext, textAppearence);
		}						
	}
	
	public void setHasEffects(boolean hasEffects){
		mHasEffects = hasEffects;
	}
	
	public void setSearcbBarRightMargin(int margin){
		mTextView.setWidth(margin);
	}
	
	public boolean isSearchMode(){
		return mIsSearchState;
	}

	public void onExitSearchMode(){
		mSearchView.onCancelClicked();
	}
	
    @Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v == mListView) {
			mSearchView.hideSoftKeyboard();
        }
		return false;
	}
	private class SearchView extends RelativeLayout {

		public SearchView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.search_bar_layout, this, true);
			
			setBackgroundDrawable(SearchBar.this.getBackground());
			mCloseButton = findViewById(R.id.search_close_btn);
			mCloseButton.setOnClickListener(mOnClickListener);

			mCancelButton = (Button)findViewById(R.id.button1);
			mCancelButton.setOnClickListener(mOnClickListener);

			mQueryTextView = (EditText) findViewById(R.id.search_src_text);
			mQueryTextView.addTextChangedListener(mTextWatcher);
			mQueryTextView.setOnFocusChangeListener(mFocusChangeListener);				
			mEffect = findViewById(R.id.effect);	
			mEffect.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onCancelClicked();
				}
			});
		}

		private OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean focused) {
				if (focused) {
					// The query box got focus, show the input method
					 showSoftKeyboard();
				} else {
					 hideSoftKeyboard();
				}
			}
		};

		private final OnClickListener mOnClickListener = new OnClickListener() {

			public void onClick(View v) {
				if (v == mCloseButton) {
					onCloseClicked();
				} else if (v == mCancelButton) {
					onCancelClicked();
				}
			}
		};

		private TextWatcher mTextWatcher = new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start,
					int before, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int after) {
				SearchView.this.onTextChanged(s);
			}

			public void afterTextChanged(Editable s) {
			}
		};

		private void onCloseClicked() {
			CharSequence text = mQueryTextView.getText();
			if (!TextUtils.isEmpty(text)) {
				mQueryTextView.setText("");
				mQueryTextView.requestFocus();
				mCloseButton.setVisibility(View.GONE);
			}
		}

		private void onCancelClicked() {
			mIsSearchState = false;		
			int count = mListView.getHeaderViewsCount();
			for (int i = 0; i < count; i++) {
				mListView.removeHeaderView(mListView.getChildAt(i));
			}
			count = mHeaderViewList.size();			
			for (int i = 0; i < count; i++){
				mListView.addHeaderView((View)mHeaderViewList.get(i));
			}	
			mHeaderViewList.clear();
			mQueryTextView.clearFocus();
			mQueryTextView.setText("");
			((ViewGroup)mViewParent).removeView(mSearchView);
			if (mOnSearchModeChangedListener != null){
			    mOnSearchModeChangedListener.onSearchModeChanged(mIsSearchState);
			}
			if (mOnQueryChangeListener != null){
				mOnQueryChangeListener.onQueryTextChange("");
			}
			setEntryAndExitSearchViewAnimation(mIsSearchState);
		}

		private void onTextChanged(CharSequence newText) {
			CharSequence text = mQueryTextView.getText();
			boolean hasText = !TextUtils.isEmpty(text);
     		mCloseButton.setVisibility(hasText ? VISIBLE : GONE);
     		setListBlackBackground(!hasText,false);
			if (mOnQueryChangeListener != null
					&& !TextUtils.equals(newText, mOldQueryText)
					&& hasText) {
				mOnQueryChangeListener.onQueryTextChange(newText.toString());
			}
			mOldQueryText = newText.toString();
		}

		private void showSoftKeyboard() {
			// Hide soft keyboard, if visible
			InputMethodManager inputMethodManager = (InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager
					.showSoftInput(mQueryTextView, 0);
		}

		private void hideSoftKeyboard() {
			// Hide soft keyboard, if visible
			InputMethodManager inputMethodManager = (InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(mQueryTextView.getWindowToken(), 0);
		}

		public void setListBlackBackground(boolean is_show, boolean is_exit){
			mEffect.setVisibility(GONE);
			try{
				if (is_show) {
					if (mListViewCache == null || mListViewCache.isRecycled()){
						mListViewCache = getViewBitmap(mListView);
						BitmapDrawable bg = new BitmapDrawable(mListViewCache);
						findViewById(R.id.black_bg).setBackgroundDrawable(bg);
					}
					mEffect.setVisibility(VISIBLE);
					if(mViewParent instanceof RelativeLayout){
						mListView.setVisibility(GONE);
					}
				} else {
					mEffect.setVisibility(GONE);
					if (is_exit && !mListViewCache.isRecycled()){
						mListViewCache.recycle();
						mListViewCache = null;
					}
					if(mViewParent instanceof RelativeLayout){
						mListView.setVisibility(VISIBLE);
					}
	               mListView.destroyDrawingCache();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		Bitmap getViewBitmap(View v) {
	        v.clearFocus();
	        v.setPressed(false);

	        boolean willNotCache = v.willNotCacheDrawing();
	        v.setWillNotCacheDrawing(false);

	        // Reset the drawing cache background color to fully transparent
	        // for the duration of this operation
	        int color = v.getDrawingCacheBackgroundColor();
	        v.setDrawingCacheBackgroundColor(0);
	        //float alpha = v.getAlpha();
	        //v.setAlpha(1.0f);

	        if (color != 0) {
	            v.destroyDrawingCache();
	        }
	        v.buildDrawingCache();
	        Bitmap cacheBitmap = v.getDrawingCache();
	        if (cacheBitmap == null) {
	            Log.e(TAG, "failed getViewBitmap(" + v + ")", new RuntimeException());
	            return null;
	        }

	        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

	        // Restore the view
	        v.destroyDrawingCache();
	        //v.setAlpha(alpha);
	        v.setWillNotCacheDrawing(willNotCache);
	        v.setDrawingCacheBackgroundColor(color);

	        return bitmap;
	    }
	}

}
