/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo_highlights.slidingmenu.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater.Factory;

import com.example.demo_highlights.IJettyToast;
import com.example.demo_highlights.R;
import com.example.demo_highlights.slidingmenu.fragment.LeftFragment;
import com.example.demo_highlights.slidingmenu.fragment.RightFragment;
import com.example.demo_highlights.slidingmenu.fragment.ViewPageFragment;
import com.example.demo_highlights.slidingmenu.fragment.ViewPageFragment.MyPageChangeListener;
import com.example.demo_highlights.slidingmenu.view.SlidingMenu;
import com.example.myapp_menusetting.DemoSettingActivity;


public class SlidingActivity extends FragmentActivity {
	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	ViewPageFragment viewPageFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		init();
		initListener();
		
        //add virtual menu
        try {  
            getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));  
        }catch (NoSuchFieldException e) {  
            // Ignore since this field won't exist in most versions of Android  
        }catch (IllegalAccessException e) {  
            Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);  
        }  
      //add end virtual menu

	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(
				R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);

		rightFragment = new RightFragment();
		t.replace(R.id.right_frame, rightFragment);

		viewPageFragment = new ViewPageFragment();
		t.replace(R.id.center_frame, viewPageFragment);
		t.commit();
	}

	private void initListener() {
		viewPageFragment.setMyPageChangeListener(new MyPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(viewPageFragment.isFirst()){
					mSlidingMenu.setCanSliding(true,false);
				}else if(viewPageFragment.isEnd()){
					mSlidingMenu.setCanSliding(false,true);
				}else{
					mSlidingMenu.setCanSliding(false,false);
				}
			}
		});
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}
	
	public void closeSlidingMenu() {
		if (mSlidingMenu.isShowSlidingView()) {
			mSlidingMenu.closeSlidingView();
		}
	}
	
	protected void setMenuBackground(){
	     
//	    Log.d(TAG, "开始设置菜单的的背景");
	    getLayoutInflater().setFactory( new Factory() {

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		 if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ){
			//上面这句Android123提醒大家不能有改动，目前来看对于原生android目前这个packageName还没有变化
				        
			 try {
				 LayoutInflater f = getLayoutInflater();
				 final View view = f.createView( name, null, attrs );  //尝试创建我们自己布局

				 new Handler().post( new Runnable() {
					 public void run () {
						 view.setBackgroundResource( R.drawable.bpush_top_bg);
					 }
				 } );
				 return view;
			 	}
			 	catch ( InflateException e ) {}
			 	catch ( ClassNotFoundException e ) {}
				}
			
		 		return null;
	    		}
	    });
	    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_setting_main, menu);
//        setMenuBackground();
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		

        
        switch (item.getItemId()) {
        case R.id.menu1:
        	
        		            Intent intent = new Intent("android.provider.Telephony.SECRET_CODE",
        		                    Uri.parse("android_secret_code://" + "4636"));
        		            sendBroadcast(intent);

        	
            return true;
        case R.id.menu2:
            return true;
        case R.id.menu3:
            return true;
        case R.id.menu4:
            return true;
        case R.id.menu5:
            final Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
            settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(settings);
            return true;
        case R.id.menu6:
            final Intent demo_settings = new Intent(SlidingActivity.this, DemoSettingActivity.class);
            startActivity(demo_settings);
            return true;
    }
		return super.onOptionsItemSelected(item);
	}

	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    // TODO Auto-generated method stub  
	    if(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	    	
	    		if (mSlidingMenu.isShowSlidingView()) {
	    			mSlidingMenu.closeSlidingView();
	    			return false;
	    		}
	    	
	           exitBy2Click();      //调用双击退出函数  
	       }  
	    
	    return false;  
	}  
	/** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  
	  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
//	        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
	        IJettyToast.showQuickToast(SlidingActivity.this, R.string.twice_quit);
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else {  
	        finish();  
//	        System.exit(0);  
	    }  
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
//		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}  
	
}
