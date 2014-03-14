package com.example.demo_highlights.slidingmenu.fragment;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class TabSwitcher extends FrameLayout{

	private static final String tag="TabSwitcher";
	private Context context;
	private String[] texts;
	private int selectedPosition=0;
	private int oldPosition=selectedPosition;
	private ImageView iv;
	private LinearLayout.LayoutParams params;
	private LinearLayout layout;
	private int iv_width;
	private TextView[] tvs;
	private int[] mDrawable;
	private int[] mDrawableImage;
	private PopupWindow popupWindow;
	private TextView mTextView;
	private int mPostionDiff = 0;
	private ViewPager mPager;
	public TabSwitcher(Context context) {
		super(context);
		init();
	}
	public TabSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setPager(ViewPager pager) {
		mPager = pager;
	}
	
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
                bitmap.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
        final float roundPx = pixels;  
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
    } 
	
	private void init(){
		context=getContext();
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		
//		Drawable avatarDrawable = context.getResources().getDrawable(R.drawable.background);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) avatarDrawable;  
//        Bitmap bitmap = bitmapDrawable.getBitmap();  
//        BitmapDrawable roundDrawable = new BitmapDrawable(toRoundCorner(bitmap, 5)); 
		
//		setBackgroundDrawable(roundDrawable);
		mDrawable = new int[]{R.drawable.calender_tab_month, R.drawable.calender_tab_day, R.drawable.calender_tab_agenda, R.drawable.calender_tab_week};
		mDrawableImage = new int[] {R.drawable.tab_month_drawable, R.drawable.tab_day_drawable, R.drawable.tab_agenda_drawable, R.drawable.tab_week_drawable};
	}
	
	  protected void initPopWindow() {  
	        LayoutInflater inflater = LayoutInflater.from(context);  
	        View view = inflater.inflate(R.layout.pup_item, null);  
	        popupWindow = new PopupWindow(findViewById(R.id.mainLayout), iv.getWidth() * 4, iv.getHeight() * 4);  
	        popupWindow.setContentView(view);  
	        mTextView = (TextView) view.findViewById(R.id.textview); 
	        popupWindow.setOutsideTouchable(true);  
	        popupWindow.setFocusable(false);  
	        popupWindow.showAsDropDown(iv);
	          
	    }  
	
	 public void setPosition(int position) {
			selectedPosition = position;
			if(tvs[selectedPosition].isClickable()){
				tvs[oldPosition].setClickable(true);
				tvs[selectedPosition].setClickable(false);
				doAnimation();
				
				oldPosition=selectedPosition;
				if(onItemClickLisener!=null){
					onItemClickLisener.onItemClickLisener(tvs[selectedPosition], selectedPosition);
				}

			}
	 }
	  
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
			texts=getResources().getStringArray(R.array.htc_tab_val);/*new String[]{"月", "日", "日程", "周"}*/;
		tvs=new TextView[texts.length];

	}

	OnClickListener listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			selectedPosition=(Integer)v.getTag();
			if(tvs[selectedPosition].isClickable()){
				tvs[oldPosition].setClickable(true);
				tvs[selectedPosition].setClickable(false);
				doAnimation();
				
				oldPosition=selectedPosition;
				if(onItemClickLisener!=null){
					onItemClickLisener.onItemClickLisener(v, selectedPosition);
				}
				if (mPager != null) {
					mPager.setCurrentItem(selectedPosition);

				}
			}
			
		}
		
	};
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(selectedPosition>texts.length-1){
			throw new IllegalArgumentException("The selectedPosition can't be > texts.length.");
		}
		layout=new LinearLayout(context);
		params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,this.getMeasuredHeight());
		params.weight=1;
		params.gravity=Gravity.CENTER_VERTICAL;
		for(int i=0;i<texts.length;i++){
			TextView child=new TextView(context);
			child.setTag(i);
			child.setText(texts[i]);
			child.setTextSize(16);
			child.setTextColor(/*Color.WHITE*/getResources().getColorStateList(mDrawable[i]));
			child.setGravity(Gravity.CENTER|Gravity.BOTTOM);
			child.setOnClickListener(listener);
			Drawable drawable= getResources().getDrawable(mDrawableImage[i]);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			child.setCompoundDrawables(null,drawable,null,null);
			
			if(i==selectedPosition){
				child.setClickable(false);
			}else{
				child.setClickable(true);
			}
			tvs[i]=child;
			layout.addView(child,params);
		}
		
		oldPosition=selectedPosition;
		//
		iv_width=this.getMeasuredWidth()/texts.length;
//		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(iv_width,LinearLayout.LayoutParams.FILL_PARENT); 
		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(iv_width,this.getMeasuredHeight());
//		p.leftMargin=selectedPosition*iv_width;
		iv=new ImageView(context);
//		iv.setScaleType(ScaleType.FIT_XY);
		iv.setBackgroundResource(R.drawable.slider);
		iv.setPadding(0, 10, 0, 10);
//		iv.setOnTouchListener(touchListener);
		iv.setClickable(true);
		this.addView(iv,p);
		this.addView(layout,params);
	}
	
	private void doAnimation(){
//		LayoutParams lp = (LayoutParams) iv.getLayoutParams();
//		lp.leftMargin = 0;
//		iv.setLayoutParams(lp);
//		iv.layout(selectedPosition*iv_width, iv.getTop(),
//	        		selectedPosition*iv_width+iv.getWidth(), iv.getBottom());
		TranslateAnimation animation = new TranslateAnimation(oldPosition*iv_width,selectedPosition*iv_width, 0, 0);  
		mPostionDiff = oldPosition-selectedPosition;
		animation.setInterpolator(new LinearInterpolator());  
		animation.setDuration(400);
		animation.setFillBefore(true); 
		animation.setFillAfter(true); 
//		animation.setFillEnabled(true);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
//				iv.layout(selectedPosition*iv_width, iv.getTop(),
//		        		selectedPosition*iv_width+iv.getWidth(), iv.getBottom());
/*				LayoutParams lp = (LayoutParams) iv.getLayoutParams();
				lp.leftMargin = (0)*iv.getWidth();
				iv.setLayoutParams(lp);*/

//				iv.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
/*				iv.layout((selectedPosition + mPostionDiff2)*iv_width, iv.getTop(),
		        		selectedPosition*iv_width+iv.getWidth() + mPostionDiff3 * iv_width, iv.getBottom());*/
				

//				iv.layout((selectedPosition)*iv_width, iv.getTop(),
//		        		(selectedPosition + 1)*iv_width, iv.getBottom());
//				LayoutParams lp = (LayoutParams) iv.getLayoutParams();
//				lp.leftMargin = /*(selectedPosition)*iv.getWidth()*/0;
//				iv.setLayoutParams(lp);
//				iv.setVisibility(View.VISIBLE);
//				iv.clearAnimation();
				LayoutParams lp = (LayoutParams) iv.getLayoutParams();
				lp.leftMargin = /*(selectedPosition)*iv.getWidth()*/0;
				iv.setLayoutParams(lp);
//				iv.setVisibility(View.VISIBLE);
			}
		});
		iv.startAnimation(animation);  
		

	}
	
	private OnItemClickLisener onItemClickLisener;
	public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
		this.onItemClickLisener = onItemClickLisener;
	}
	public interface OnItemClickLisener{
		void onItemClickLisener(View view,int position);
	}
	
	public void setTexts(String[] texts) {
		this.texts = texts;
	}
//	public void setSelectedPosition(int selectedPosition) {
//		this.selectedPosition = selectedPosition;
//	}
	
	OnTouchListener touchListener=new OnTouchListener(){
		int temp[] = new int[]{0, 0};
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int action = event.getAction();
			int x = (int)event.getRawX()-getLeft();
//			int x = (int)event.getX();
//			Log.i(tag, "x======="+x);
//            int y = (int)event.getRawY();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				temp[0] = (int)event.getX();  
//                temp[1] = (int)(y-v.getTop());
                v.postInvalidate();
			break;
			case MotionEvent.ACTION_MOVE:
				int left = x - temp[0];
                int right = left + v.getWidth();  
                int top=0;//y - temp[1];
                int bottom=top+v.getHeight();
                if (left < 0){//�߽��ж�
                    left = 0;
                    right = left + v.getWidth();
                }

                if (right > getMeasuredWidth()){
                    right = getMeasuredWidth();
                    left = right - v.getWidth();
                }

                /*if (top < 0) {  
                    top = 0;  
                    bottom = top + v.getHeight();  
                }  

                if (bottom > getMeasuredHeight()) {  
                    bottom = getMeasuredHeight();  
                    top = bottom - v.getHeight();  
                }*/
                v.layout(left, top, right, bottom);
                v.postInvalidate();
                
                if (popupWindow == null) {
                	initPopWindow();
                } else {
                	popupWindow.showAsDropDown(iv);
                }
                if (mTextView != null) {
                    selectedPosition = Math.round(1.0F*iv.getLeft()/iv_width);
                	mTextView.setText(texts[selectedPosition]);
                }
			break;
			case MotionEvent.ACTION_UP:
				tvs[oldPosition].setClickable(true);
				setBestPosition();
				if(oldPosition!=selectedPosition){
					if(onItemClickLisener!=null){
						onItemClickLisener.onItemClickLisener(tvs[selectedPosition], selectedPosition);
					}
				}
				oldPosition=selectedPosition;
			    tvs[selectedPosition].setClickable(false);
			    if (popupWindow != null) {
			    	popupWindow.dismiss();
			    }
			break;
			}
			return false;
		}
		
	};
	private void setBestPosition() {
		int left = iv.getLeft();
        selectedPosition = Math.round(1.0F*left/iv_width);
        int toPosition = selectedPosition*iv_width;
        iv.layout(selectedPosition*iv_width, iv.getTop(),
        		selectedPosition*iv_width+iv.getWidth(), iv.getBottom());
        TranslateAnimation animation = new TranslateAnimation(left-toPosition,0,0,0);
        animation.setInterpolator(new LinearInterpolator());  
        animation.setDuration(400);
		animation.setFillAfter(true);  
        iv.startAnimation(animation);
//        iv.invalidate();
        
    }
	
	public interface MyTabChangeListener {
		public void onTabChanged(int position);
	}
	
}
