package com.baidu.rocket;

import java.util.List;

import com.example.demo_highlights.R;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class RocketService extends Service {
	protected static final int SHOW = 1;
	protected static final int HIDE = 2;
	private WindowManager wm;
	private ActivityManager am;
	private ImageView iv;
	private WindowManager.LayoutParams params;
	private AnimationDrawable rocketAnimation;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW:
				iv.setVisibility(View.VISIBLE);
				break;

			case HIDE:
				iv.setVisibility(View.INVISIBLE);
				break;
			}
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.rocket);
		iv.setOnTouchListener(new OnTouchListener() {
			int startX ;
			int startY ;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					params.x+=dx;
					params.y+=dy;
					wm.updateViewLayout(iv, params);
					
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					if(params.y>300&&params.x>100&&params.x<300){
						Toast.makeText(getApplicationContext(), "׼��������", 0).show();
						Intent intent = new Intent(RocketService.this,MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					break;
				}
				return true;
			}
		});
		rocketAnimation = (AnimationDrawable) iv.getBackground();
		params = new LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(iv, params);
		rocketAnimation.start();
		super.onCreate();

		new Thread() {
			public void run() {
				while (true) {
					List<RunningTaskInfo> infos = am.getRunningTasks(1000);
					String packname = infos.get(0).topActivity.getPackageName();
					System.out.println(packname);
					// ���ӵ�ǰ�û������Ľ��档
					Message msg = Message.obtain();
					if ("com.android.launcher".equals(packname)) {
						msg.what = SHOW;
					} else {
						msg.what = HIDE;
					}
					handler.sendMessage(msg);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		wm.removeView(iv);
	}
}
