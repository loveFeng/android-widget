package com.ql.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demo_highlights.R;

public class TabSwitcher extends FrameLayout{

	private static final String tag="TabSwitcher";
	private Context context;
	private String[] texts;
	private int arrayId;
	private int selectedPosition=0;
	private int oldPosition=selectedPosition;
	private ImageView iv;
	private LinearLayout.LayoutParams params;
	private LinearLayout layout;
	private int iv_width;
	private TextView[] tvs;
	public TabSwitcher(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	public TabSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.i(tag, "--------------TabSwitcher2---------------------");
		init();
		TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.custom);  
		arrayId=a.getResourceId(R.styleable.custom_arrayId, 0);
//		selectedPosition=a.getInt(R.styleable.custom_selectedPosition, 0);
        a.recycle();
	}
	private void init(){
		context=getContext();
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		setBackgroundResource(R.drawable.tabswitcher_long);
		
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		Log.i(tag, "--------------onFinishInflate---------------------");
		if(arrayId!=0){
			texts=getResources().getStringArray(arrayId);
		}else{
			texts=new String[]{};
		}
		tvs=new TextView[texts.length];
	}

	OnClickListener listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			selectedPosition=(Integer)v.getTag();
			if(tvs[selectedPosition].isClickable()){
				tvs[oldPosition].setClickable(true);
				tvs[selectedPosition].setClickable(false);
				doAnimation();
				
				oldPosition=selectedPosition;
				if(onItemClickLisener!=null){
					onItemClickLisener.onItemClickLisener(v, selectedPosition);
				}
			}
			
		}
		
	};
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i("tag", "---------------onSizeChanged--------------------");
		if(selectedPosition>texts.length-1){
			throw new IllegalArgumentException("The selectedPosition can't be > texts.length.");
		}
		layout=new LinearLayout(context);
		params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,this.getMeasuredHeight());//Ϊ�˾�����ʾ����
		params.weight=1;
		params.gravity=Gravity.CENTER_VERTICAL;
		for(int i=0;i<texts.length;i++){
			TextView child=new TextView(context);
			child.setTag(i);
			child.setText(texts[i]);
			child.setTextSize(16);
			child.setTextColor(Color.BLACK);
			child.setGravity(Gravity.CENTER);
			child.setOnClickListener(listener);
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
		iv_width=this.getMeasuredWidth()/texts.length;//����ImageView�Ŀ�
//		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(iv_width,LinearLayout.LayoutParams.FILL_PARENT);
		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(iv_width,this.getMeasuredHeight());
//		p.leftMargin=selectedPosition*iv_width;//��Ч����ΪFrameLayout����������Ͻǡ�
		iv=new ImageView(context);
//		iv.setImageResource(R.drawable.tabswitcher_short);
//		iv.setScaleType(ScaleType.FIT_XY);
		iv.setBackgroundResource(R.drawable.tabswitcher_short);
		iv.setOnTouchListener(touchListener);
		iv.setClickable(true);
		this.addView(iv,p);
		this.addView(layout,params);
	}
	
	private void doAnimation(){
//		TranslateAnimation animation = new TranslateAnimation(oldPosition*iv_width, selectedPosition*iv_width, 0, 0);  
		iv.layout(selectedPosition*iv_width, iv.getTop(),
	        		selectedPosition*iv_width+iv.getWidth(), iv.getBottom());//�ܹؼ�
		TranslateAnimation animation = new TranslateAnimation((oldPosition-selectedPosition)*iv_width, 0, 0, 0);  
		animation.setInterpolator(new LinearInterpolator());  
		animation.setDuration(400);  
		animation.setFillAfter(true);  
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
			int x = (int)event.getRawX()-getLeft();//����϶�������Ļ�����
//			int x = (int)event.getX();//����֣�Ϊʲô����x�ͻᡰ���أ�
//			Log.i(tag, "x======="+x);
//            int y = (int)event.getRawY();//ֻ��ˮƽ�϶�������y������Ҫ
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
//                Log.i(tag, "left==="+left);
//                Log.i(tag, "right==="+right);
                v.layout(left, top, right, bottom);
                v.postInvalidate();
			break;
			case MotionEvent.ACTION_UP:
				tvs[oldPosition].setClickable(true);
				setBestPosition();
				if(oldPosition!=selectedPosition){//�ص�
					if(onItemClickLisener!=null){
						onItemClickLisener.onItemClickLisener(tvs[selectedPosition], selectedPosition);
					}
				}
				oldPosition=selectedPosition;//
			    tvs[selectedPosition].setClickable(false);
			break;
			}
			return false;
		}
		
	};
	/**
	 * ������ͣ��λ��
	 */
	private void setBestPosition() {
		int left = iv.getLeft();
        selectedPosition = Math.round(1.0F*left/iv_width);//��������
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
}
