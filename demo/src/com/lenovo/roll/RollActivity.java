package com.lenovo.roll;


import com.example.demo_highlights.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class RollActivity extends Activity {
	private View view;
	private Button btn;
	private PopupWindow mPopupWindow;
	private View[] btns;
	
	private PopupWindow menu;
	 
	private LayoutInflater inflater;
	 
	private View layout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_popwindow);
//		LinearLayout layout=(LinearLayout) view.findViewById(R.id.layout_main);
//		//���ñ���ͼƬ��ת180
//		Bitmap mBitmap=setRotate(R.drawable.bg_kuang);
//		BitmapDrawable drawable=new BitmapDrawable(mBitmap);
//		layout.setBackgroundDrawable(drawable);
        
        btn=(Button) this.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow(btn);
//				menu.showAtLocation(/*btn*/v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0); //设置在屏幕中的显示位置
			}
        	
        });

        initPopupWindow(R.layout.popwindow);
        initMenu();

    }
    
	//实例化PopupWindow创建菜单
	 
	private void initMenu(){
	 
	 
	//获取LayoutInflater实例
	 
	inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
	 
	//获取弹出菜单的布局
	 
	layout = inflater.inflate(R.layout.popwindow_quit,null);
	 
	//设置popupWindow的布局
	 
	menu = new PopupWindow(layout,  WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
	menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_frame));
	menu.setOutsideTouchable(true);
	menu.setAnimationStyle(android.R.style.Animation_Dialog);
	menu.update();
	menu.setTouchable(true);
	menu.setFocusable(true);
	
	LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.layout_menu_quit);
	linearLayout.setGravity(Gravity.CENTER);
	
	linearLayout.setOnClickListener(new View.OnClickListener(
			) {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			
		}
	});
	 
	}
    
	private void initPopupWindow(int resId){
		LayoutInflater mLayoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
	    view = mLayoutInflater.inflate(resId, null);
	        
		mPopupWindow = new PopupWindow(view, 400,LayoutParams.WRAP_CONTENT);
//		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//��������background������ʧ
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_frame));
		mPopupWindow.setOutsideTouchable(true);
		
		//�Զ��嶯��
//		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		//ʹ��ϵͳ����
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		
		btns=new View[3];
		btns[0]=view.findViewById(R.id.btn_0);
		btns[1]=view.findViewById(R.id.btn_1);
		btns[2]=view.findViewById(R.id.btn_2);
		btns[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
				mPopupWindow.dismiss();
				menu.showAtLocation(btn, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0); //设置在屏幕中的显示位置
			}
		});
		btns[1].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
			}
		});
		btns[2].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
			}
		});
	}
	private void showPopupWindow(View view) {
		if(!mPopupWindow.isShowing()){
//			mPopupWindow.showAsDropDown(view,0,0);
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		}
	}
	public Bitmap setRotate(int resId) {
		Matrix mFgMatrix = new Matrix();
		Bitmap mFgBitmap = BitmapFactory.decodeResource(getResources(), resId);
		mFgMatrix.setRotate(180f);
		return mFgBitmap=Bitmap.createBitmap(mFgBitmap, 0, 0, 
				mFgBitmap.getWidth(), mFgBitmap.getHeight(), mFgMatrix, true);
	}
	
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    
	    if(!menu.isShowing() && keyCode == KeyEvent.KEYCODE_MENU){
	    	 
	    	menu.showAtLocation(btn, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0); //设置在屏幕中的显示位置
	    	 
	    	}else{
	    	 
//	    	menu.dismiss();
	    	 
	    	}
//	    if (keyCode == KeyEvent.KEYCODE_BACK) {
//	    	if (menu.isShowing()) {
//	    		menu.dismiss();
//	    	}
//	    }
	    	 
//	    	if(keyCode == KeyEvent.KEYCODE_BACK&&menu.isShowing()){
//	    	 
//	    	menu.dismiss();
//	    	 
//	    	}
	    
	    return false;  
	}  
}