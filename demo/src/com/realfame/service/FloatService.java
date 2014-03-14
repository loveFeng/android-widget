package com.realfame.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;

import com.example.demo_highlights.R;
import com.feng.onekeylockscreen.Dar;
import com.krislq.floating2.FloatView;
import com.krislq.floating2.FloatApplication;
//import com.dream.myqiyi.BaseApp;
import com.way.app.Application;

public class FloatService extends Service implements OnClickListener{

//	WindowManager wm = null;
//	WindowManager.LayoutParams wmParams = null;
//	View view;
	
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;
	private FloatView floatView = null;
	private DevicePolicyManager devicePolicyManager=null;
	private SharedPreferences mShare;
//	private float mTouchStartX;
//	private float mTouchStartY;
//	private float x;
//	private float y;
//	int state;
//	TextView tx1;
//	TextView tx;
//	ImageView iv;
//	private float StartX;
//	private float StartY;
//	int delaytime=1000;
	@Override
	public void onCreate() {
		devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		mShare = getSharedPreferences("Setting", 0);
		createView();
		super.onCreate();
	}

	private void createView() {
		floatView = new FloatView(getApplicationContext());
		floatView.setOnClickListener(this);
		floatView.setImageResource(R.drawable.suspended/*ic_launcher*/); // 锟斤拷锟斤拷虻サ锟斤拷锟斤拷源锟斤拷icon锟斤拷锟斤拷锟斤拷示
		floatView.setAlpha(80);
		// 锟斤拷取WindowManager
		windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		// 锟斤拷锟斤拷LayoutParams(全锟街憋拷锟斤拷锟斤拷锟斤拷夭锟斤拷锟�
		windowManagerParams = ((Application) getApplication()).getMywmParams();

		windowManagerParams.type = LayoutParams.TYPE_PHONE; // 锟斤拷锟斤拷window type
		windowManagerParams.format = PixelFormat.RGBA_8888; // 锟斤拷锟斤拷图片锟斤拷式锟斤拷效锟斤拷为锟斤拷锟斤拷透锟斤拷
		// 锟斤拷锟斤拷Window flag
		windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 注锟解，flag锟斤拷值锟斤拷锟斤拷为锟斤拷
		 * 锟斤拷锟斤拷锟絝lags锟斤拷锟皆碉拷效锟斤拷锟斤拷同锟斤拷锟斤拷锟斤拷
		 * 锟斤拷锟斤拷纱锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷魏锟斤拷录锟�同时锟斤拷影锟斤拷锟斤拷锟斤拷锟铰硷拷锟斤拷应锟斤拷
		 * LayoutParams.FLAG_NOT_TOUCH_MODAL 锟斤拷影锟斤拷锟斤拷锟斤拷锟铰硷拷
		 * LayoutParams.FLAG_NOT_FOCUSABLE  锟斤拷锟缴聚斤拷
		 * LayoutParams.FLAG_NOT_TOUCHABLE 锟斤拷锟缴达拷锟斤拷
		 */
		// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷辖牵锟斤拷锟斤拷诘锟斤拷锟斤拷锟斤拷
		windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP; 
		// 锟斤拷锟斤拷幕锟斤拷锟较斤拷为原锟姐，锟斤拷锟斤拷x锟斤拷y锟斤拷始值
		windowManagerParams.x = /*0*/mShare.getInt("x", 0);
		windowManagerParams.y = /*0*/mShare.getInt("y", 0);
		// 锟斤拷锟斤拷锟斤拷诔锟斤拷锟斤拷锟斤拷
		windowManagerParams.width = LayoutParams.WRAP_CONTENT;
		windowManagerParams.height = LayoutParams.WRAP_CONTENT;
		// 锟斤拷示myFloatView图锟斤拷
		windowManager.addView(floatView, windowManagerParams);
		
//		mShare.edit().putBoolean("use_suspend_icon", true).commit();
	}
	
/*	private void createView() {
		SharedPreferences shared = getSharedPreferences("float_flag",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putInt("float", 1);
		editor.commit();
		// 锟斤拷取WindowManager
		wm = (WindowManager) getApplicationContext().getSystemService("window");
		// 锟斤拷锟斤拷LayoutParams(全锟街憋拷锟斤拷锟斤拷锟斤拷夭锟斤拷锟�
		wmParams = ((MyApplication) getApplication()).getMywmParams();
		wmParams.type = 2002;
		wmParams.flags |= 8;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷辖锟�
		// 锟斤拷锟斤拷幕锟斤拷锟较斤拷为原锟姐，锟斤拷锟斤拷x锟斤拷y锟斤拷始值
		wmParams.x = 0;
		wmParams.y = 0;
		// 锟斤拷锟斤拷锟斤拷诔锟斤拷锟斤拷锟斤拷
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.format = 1;
		
		wm.addView(view, wmParams);

		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// 锟斤拷取锟斤拷锟斤拷锟侥伙拷锟斤拷锟疥，锟斤拷锟斤拷锟斤拷幕锟斤拷锟较斤拷为原锟斤拷
				x = event.getRawX();
				y = event.getRawY() - 25; // 25锟斤拷系统状态锟斤拷锟侥高讹拷
				Log.i("currP", "currX" + x + "====currY" + y);// 锟斤拷锟斤拷锟斤拷息
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					state = MotionEvent.ACTION_DOWN;
					StartX = x;
					StartY = y;
					// 锟斤拷取锟斤拷锟絍iew锟斤拷锟斤拷辏拷锟斤拷源锟絍iew锟斤拷锟较斤拷为原锟斤拷
					mTouchStartX = event.getX();
					mTouchStartY = event.getY();
					Log.i("startP", "startX" + mTouchStartX + "====startY"
							+ mTouchStartY);// 锟斤拷锟斤拷锟斤拷息
					break;
				case MotionEvent.ACTION_MOVE:
					state = MotionEvent.ACTION_MOVE;
					updateViewPosition();
					break;

				case MotionEvent.ACTION_UP:
					state = MotionEvent.ACTION_UP;

					updateViewPosition();
					showImg();
					mTouchStartX = mTouchStartY = 0;
					break;
				}
				return true;
			}
		});

		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent serviceStop = new Intent();
				serviceStop.setClass(FloatService.this, FloatService.class);
				stopService(serviceStop);
			}
		});

	}*/

//	public void showImg() {
//		if (Math.abs(x - StartX) < 1.5 && Math.abs(y - StartY) < 1.5
//				&& !iv.isShown()) {
//			iv.setVisibility(View.VISIBLE);
//		} else if (iv.isShown()) {
//			iv.setVisibility(View.GONE);
//		}
//	}

//	private Handler handler = new Handler();
//	private Runnable task = new Runnable() {
//		public void run() {
//			// TODO Auto-generated method stub
//			dataRefresh();
//			handler.postDelayed(this, delaytime);
//			wm.updateViewLayout(view, wmParams);
//		}
//	};

//	public void dataRefresh() {
//		tx.setText("" + memInfo.getmem_UNUSED(this) + "KB");
//		tx1.setText("" + memInfo.getmem_TOLAL() + "KB");
//	}

//	private void updateViewPosition() {
//		// 锟斤拷锟铰革拷锟斤拷锟斤拷锟斤拷位锟矫诧拷锟斤拷
//		wmParams.x = (int) (x - mTouchStartX);
//		wmParams.y = (int) (y - mTouchStartY);
//		wm.updateViewLayout(view, wmParams);
//	}

	@Override
	public void onStart(Intent intent, int startId) {
//		Log.d("FloatService", "onStart");
//		setForeground(true);
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
//		handler.removeCallbacks(task);
		Log.d("FloatService", "onDestroy");
		if (floatView != null) {
			windowManager.removeView(floatView);
			floatView = null;
		}
//		mShare.edit().putBoolean("use_suspend_icon", false).commit();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		if (devicePolicyManager.isAdminActive(Dar.getCn(this))) {
			devicePolicyManager.lockNow();
		} else {
			Intent intent = new Intent("android.intent.action.oneKeyLock");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}	
}
