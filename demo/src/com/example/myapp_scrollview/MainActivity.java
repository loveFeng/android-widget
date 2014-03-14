package com.example.myapp_scrollview;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.example.demo_highlights.R;
import com.example.myapp_scrollview.SlowScrollView;

public class MainActivity extends Activity {
	private SlowScrollView scrollView1;
	private Button mButton;
	private Button mButton2;
	private Button mButtonUpdate;
	private int x = 540;
	private boolean mIsCalling = false;
	private Context mContext = this;
	
	public class PhoneReceiver extends BroadcastReceiver {
		 
		 @Override
		 public void onReceive(Context context, Intent intent) {
		 // System.out.println("action"+intent.getAction());
		  //如果是去电
		  if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
		     String phoneNumber = intent
		     .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//		     Log.d(TAG, "call OUT:" + phoneNumber); 
		   }else{
		   //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
		   //如果我们想要监听电话的拨打状况，需要这么几步 :
//		    * 第一：获取电话服务管理器TelephonyManager manager = this.getSystemService(TELEPHONY_SERVICE);
//		    * 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new MyPhoneStateListener(),
//		    * PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
//		    * 监听的状态改变事件，初次之外，还有很多其他事件哦。
//		    * 第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
//		    * 第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE


		   TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE); 
		   tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		   //设置一个监听器
		  }
		 }
		public PhoneStateListener listener=new PhoneStateListener(){
		 
		  @Override
		  public void onCallStateChanged(int state, String incomingNumber) {
		   //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
		   super.onCallStateChanged(state, incomingNumber);
		   switch(state){
		   case TelephonyManager.CALL_STATE_IDLE:
		   // System.out.println("挂断");
		    mIsCalling = false;
		    break;
		   case TelephonyManager.CALL_STATE_OFFHOOK:
		    //System.out.println("接听");
		    mIsCalling = true;
		    break;
		   case TelephonyManager.CALL_STATE_RINGING:
		  //  System.out.println("响铃:来电号码"+incomingNumber);
		    mIsCalling = true;
		    //输出来电号码
		    break;
		   }
		  }
		 };
		}
	private PhoneReceiver mPhoneReceiver = new PhoneReceiver();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slowscrollview);
        scrollView1 = (SlowScrollView) findViewById(R.id.scrollView1);
        mButton = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button20);
        mButtonUpdate = (Button) findViewById(R.id.button2);
        
        mButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scrollView1.slowSmoothScrollTo(0, 0, 3000);

			}
				
		});
        
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContentResolver cr = MainActivity.this.getContentResolver();
				
				Uri uri = Uri.parse("content://com.realfame.provider.MifiProvider/WifiAp");//com.yy.test.studentprovider/student
				
				ContentValues cValues = new ContentValues();
				cValues.put("ssid", "mifi");
				cValues.put("security", "WPA PSK");
				cValues.put("password", "12345678");
				cValues.put("maxconnections", 8);
				String[] args = {String.valueOf(1)};
				cr.update(uri, cValues, "_id=?",args);
				Toast.makeText(MainActivity.this, "更新数据", Toast.LENGTH_LONG).show();
			}
		});
        
        mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scrollView1.slowSmoothScrollTo(600, 0, 3000);
			}
		});

        Uri uri = Uri.parse("content://com.realfame.provider.MifiProvider/WifiAp");
        //注册一个数据变化监听器        
        getContentResolver().registerContentObserver(uri, true, new PersonContentObserver(new Handler()));
    }
    public class PersonContentObserver extends ContentObserver{
    	
    public PersonContentObserver(Handler handler) {
    		super(handler);
    	}

       
        public void onChange(boolean selfChange) {
        	Toast.makeText(MainActivity.this, "数据变化", Toast.LENGTH_SHORT).show();
        }

    }
}
