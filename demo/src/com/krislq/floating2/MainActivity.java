package com.krislq.floating2;

import java.util.List;

import com.example.demo_highlights.R;
import com.feng.onekeylockscreen.Dar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realfame.service.FloatService;
/**
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 29, 2012
 * @version 1.0.0
 *
 */
public class MainActivity extends Activity  implements OnClickListener{

	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;
	private FloatView floatView = null;
	private SharedPreferences mShare;
	private CheckBox mCheckBox;
	private DevicePolicyManager devicePolicyManager=null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        requestWindowFeature(Window.FEATURE_NO_TITLE);//取锟斤拷锟斤拷锟斤拷锟�        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                      WindowManager.LayoutParams.FLAG_FULLSCREEN);//全锟斤拷
        setContentView(R.layout.activity_main_float_3);
        mShare = getSharedPreferences("Setting", 0);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        Intent intent = getIntent();
        
        if (intent != null && intent.getBooleanExtra("IsNeedBootShow", false)) {
//        	createView(); 
        	startService();
        	finish();
        }
        
        
        RelativeLayout re = (RelativeLayout) findViewById(R.id.start_lock_screen);
        re.setOnClickListener(this);
        ImageView im = (ImageView) re.getChildAt(1);
        if (devicePolicyManager.isAdminActive(Dar.getCn(this))) {
        	im.setImageResource(R.drawable.checkbox_on);
        } else {
        	im.setImageResource(R.drawable.checkbox_off);
        }
        LinearLayout lin = (LinearLayout)re.getChildAt(0);
        TextView tv1 = (TextView) lin.getChildAt(0);
        TextView tv2 = (TextView) lin.getChildAt(1);
        tv1.setText(R.string.start_lock_screen);
        tv2.setVisibility(View.GONE);
        
        re = (RelativeLayout) findViewById(R.id.use_suspend_icon);
        re.setOnClickListener(this);
        im = (ImageView) re.getChildAt(1);
        if (/*mShare.getBoolean("use_suspend_icon", false)*/isServiceRunning(this, "com.realfame.service.FloatService")) {
        	im.setImageResource(R.drawable.checkbox_on);
        } else {
        	im.setImageResource(R.drawable.checkbox_off);
        }
        lin = (LinearLayout)re.getChildAt(0);
        tv1 = (TextView) lin.getChildAt(0);
        tv2 = (TextView) lin.getChildAt(1);
        tv1.setText(R.string.use_suspend_icon);
        tv2.setVisibility(View.GONE);
        
        re = (RelativeLayout) findViewById(R.id.boot_start);
        re.setOnClickListener(this);
        im = (ImageView) re.getChildAt(1);
        if (mShare.getBoolean("IsNeedBootShow", false)) {
        	im.setImageResource(R.drawable.checkbox_on);
        } else {
        	im.setImageResource(R.drawable.checkbox_off);
        }
        lin = (LinearLayout)re.getChildAt(0);
        tv1 = (TextView) lin.getChildAt(0);
        tv2 = (TextView) lin.getChildAt(1);
        tv1.setText(R.string.boot_start);
        tv2.setVisibility(View.GONE);
        
        LinearLayout linnear = (LinearLayout) findViewById(R.id.send_shortcut);
        linnear.setOnClickListener(this);
        RelativeLayout rel = (RelativeLayout)linnear.getChildAt(0);
        tv1 = (TextView) rel.getChildAt(0);
        tv2 = (TextView) rel.getChildAt(1);
        tv1.setText(R.string.send_shortcut);
        tv2.setVisibility(View.GONE);
//       	createView(); 
//       	Button b = (Button) findViewById(R.id.button1);
//       	b.setOnClickListener(this);
//       	
//        b = (Button) findViewById(R.id.button2);
//       	b.setOnClickListener(this);
//       	
//        b = (Button) findViewById(R.id.button3);
//       	b.setOnClickListener(this);
//       	
//        b = (Button) findViewById(R.id.button4);
//       	b.setOnClickListener(this);
//       	
//       	mCheckBox = (CheckBox) findViewById(R.id.checkBox1);
//       	mCheckBox.setOnClickListener(this);
//       	setChecked();
    }
	

	
	public void setChecked() {
		mCheckBox.setChecked(mShare.getBoolean("IsNeedBootShow", false));
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}

	public void onDestroy() {
		super.onDestroy();
		// 锟节筹拷锟斤拷锟剿筹拷(Activity锟斤拷伲锟绞憋拷锟斤拷锟斤拷锟�//		windowManager.removeView(floatView);
		
//		if (floatView != null) {
//			windowManager.removeView(floatView);
//		}
	}
	
	private void setUseSuspend(Boolean bool) {
		RelativeLayout re = (RelativeLayout) findViewById(R.id.use_suspend_icon);
		ImageView im = (ImageView) re.getChildAt(1);
        if (bool) {
        	im.setImageResource(R.drawable.checkbox_on);
        } else {
        	im.setImageResource(R.drawable.checkbox_off);
        }
	}
	
	private void setIsBund(boolean bool) {
        RelativeLayout re = (RelativeLayout) findViewById(R.id.start_lock_screen);
        ImageView im = (ImageView) re.getChildAt(1);
        if (bool) {
        	im.setImageResource(R.drawable.checkbox_on);
        } else {
        	im.setImageResource(R.drawable.checkbox_off);
        }
	}

	private void updateUI(boolean force) {
        RelativeLayout re = (RelativeLayout) findViewById(R.id.start_lock_screen);
        ImageView im = (ImageView) re.getChildAt(1);
        if (force) {
	        if (devicePolicyManager.isAdminActive(Dar.getCn(this))) {
	        	im.setImageResource(R.drawable.checkbox_on);
	        } else {
	        	im.setImageResource(R.drawable.checkbox_off);
	        }
        }
        
        re = (RelativeLayout) findViewById(R.id.use_suspend_icon);
        im = (ImageView) re.getChildAt(1);
        if (/*mShare.getBoolean("use_suspend_icon", false)*/isServiceRunning(this, "com.realfame.service.FloatService")) {
        	im.setImageResource(R.drawable.checkbox_on);
        } else {
        	im.setImageResource(R.drawable.checkbox_off);
        }
        
        re = (RelativeLayout) findViewById(R.id.boot_start);
        im = (ImageView) re.getChildAt(1);
        if (mShare.getBoolean("IsNeedBootShow", false)) {
        	im.setImageResource(R.drawable.checkbox_on);
        } else {
        	im.setImageResource(R.drawable.checkbox_off);
        }
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateUI(true);
	}



	private void createView() {
		floatView = new FloatView(getApplicationContext());
		floatView.setOnClickListener(this);
		floatView.setImageResource(R.drawable.suspended/*ic_launcher*/); // 锟斤拷锟斤拷虻サ锟斤拷锟斤拷源锟斤拷icon锟斤拷锟斤拷锟斤拷示
		// 锟斤拷取WindowManager
		windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		// 锟斤拷锟斤拷LayoutParams(全锟街憋拷锟斤拷锟斤拷锟斤拷夭锟斤拷锟�		windowManagerParams = ((FloatApplication) getApplication()).getWindowParams();

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
	}
	
	private void addShortCut(String tName) {
		// 瀹夎鐨処ntent  
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		// 蹇嵎鍚嶇О  
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, tName);
		// 蹇嵎鍥炬爣鏄厑璁搁噸澶�		shortcut.putExtra("duplicate", false);

		Intent shortcutIntent = new Intent(/*Intent.ACTION_MAIN*/"android.intent.action.oneKeyLock");
//		shortcutIntent.putExtra("tName", tName);
//		shortcutIntent.setClassName("com.feng.onekeylockscreen", "com.feng.onekeylockscreen.LockActivity");
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		// 蹇嵎鍥炬爣  
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable./*ic_launcher*/one_key_lock_screen);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		// 鍙戦�骞挎挱  
		sendBroadcast(shortcut);
	}
	
	public boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			Log.e("zhengpangyong", "name:" + serviceList.get(i).service.getClassName());
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
	
	public void startService() {
    	Intent service = new Intent();
		service.setClass(MainActivity.this, FloatService.class);		
		startService(service);
	}
	
	public void stopService() {
    	Intent service = new Intent();
		service.setClass(MainActivity.this, FloatService.class);		
		stopService(service);
	}

    public void unBind(ComponentName componentName){
        if(componentName!=null){
           devicePolicyManager.removeActiveAdmin(componentName);
//           activeManager(componentName);
        }
       }
    
    private void activeManager(ComponentName componentName) { 
        //使用隐式意图调用系统方法来激活指定的设备管理器 
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN); 
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName); 
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.app_name)); 
        startActivity(intent); 
    } 
	
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_lock_screen:
			if (devicePolicyManager.isAdminActive(Dar.getCn(this))) {
//				devicePolicyManager.lockNow();
				unBind(Dar.getCn(this));
				setIsBund(false);
			} else {
//				Intent intent = new Intent("android.intent.action.oneKeyLock");
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//				startActivity(intent);
				activeManager(Dar.getCn(this));
				setIsBund(true);
			}
			updateUI(false);
			break;
		case R.id.send_shortcut:
			addShortCut(getString(R.string.shortcut_name));
			break;
		case R.id.use_suspend_icon:
//	       	createView(); 
			if (isServiceRunning(this, "com.realfame.service.FloatService")) {
	        	stopService();
	        	setUseSuspend(false);
//	        	mShare.edit().putBoolean("use_suspend_icon", false).commit();
			} else {
	        	startService();
	        	setUseSuspend(true);
//	        	mShare.edit().putBoolean("use_suspend_icon", true).commit();
			}
//	        if (mShare.getBoolean("use_suspend_icon", false)) {
//	        	stopService();
//	        	setUseSuspend(false);
//	        	mShare.edit().putBoolean("use_suspend_icon", false).commit();
//	        } else {
//	        	startService();
//	        	setUseSuspend(true);
//	        	mShare.edit().putBoolean("use_suspend_icon", true).commit();
//	        }
			break;
		case R.id.boot_start:
			mShare.edit().putBoolean("IsNeedBootShow", !mShare.getBoolean("IsNeedBootShow", false)).commit();
			updateUI(true);
			break;
			
		case R.id.button1:
//	       	createView(); 
			startService();
			break;
		case R.id.button2:
/*			if (floatView != null) {
				windowManager.removeView(floatView);
				floatView = null;
			} else {
				windowManager.removeView(new FloatView(getApplicationContext()));
			}*/
			stopService();
			break;
		case R.id.button3:
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
			break;
		case R.id.checkBox1:
			mShare.edit().putBoolean("IsNeedBootShow", !mShare.getBoolean("IsNeedBootShow", false)).commit();
			setChecked();
			break;
		case R.id.button4:
			addShortCut(/*"涓�敭閿�*/"一键锁");
			break;
		default:
//			Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
			if (devicePolicyManager.isAdminActive(Dar.getCn(this))) {
				devicePolicyManager.lockNow();
			} else {
				Intent intent = new Intent("android.intent.action.oneKeyLock");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(intent);
			}
			break;
		}
		
		

	}
}
