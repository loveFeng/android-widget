package com.example.myapp_webservise;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.demo_highlights.R;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class WebServiseActivity extends Activity {

	//命名空间
    private static final String serviceNameSpace="http://WebXml.com.cn/";
    //调用方法(获得支持的城市)
    private static final String getSupportCity="getSupportCity";
    private static final String getWeatherbyCityName="getWeatherbyCityName";
    //请求URL
    private static final String serviceURL="http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
    
    private TextView mTextView;
    private SoapSerializationEnvelope envelope;
    private boolean bool = true;
    private HttpTransportSE transport;
    private static int i = 1;
    private SoapObject detail;
    private String weatherToday;
    private Handler handler = new Handler() {  
    	  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
                case 0:
                	mTextView.setText("连接未成功 : " + i++);
                	break;  
                case 1:
				if(envelope != null /*&& envelope.getResponse()!=null*/){
					mTextView.setText(envelope.bodyIn.toString());
					
					 SoapObject result = (SoapObject) envelope.bodyIn;  
					  detail = (SoapObject) result  
							  .getProperty("getWeatherbyCityNameResult");
					  mTextView.setText(detail.toString());
					  try {
						parseWeather(detail);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bool = false;
					
/*					ContentResolver cr = WebServiseActivity.this.getContentResolver();
					
					Uri uri = Uri.parse("content://com.realfame.provider.MifiProvider/WifiAp");
					
					Cursor cursor = cr.query(uri, null, null, null, null);
					
					while(cursor.moveToNext()){
						Toast.makeText(WebServiseActivity.this, cursor.getInt(cursor.getColumnIndex("_id")) + "\t" + cursor.getString(cursor.getColumnIndex("ssid")) + "\t" + cursor.getString(cursor.getColumnIndex("security"))  + "\t" + cursor.getString(cursor.getColumnIndex("password"))  + "\t" + cursor.getString(cursor.getColumnIndex("maxconnections")), Toast.LENGTH_LONG).show();
					}
					Toast.makeText(WebServiseActivity.this, "查询结束", Toast.LENGTH_LONG).show();*/
				}
                    break;  
            }  
        };  
    }; 
    
    private void parseWeather(SoapObject detail)  
    		  throws UnsupportedEncodingException {  
    	  String date = detail.getProperty(6).toString();  
    	  weatherToday = "今天：" + date.split(" ")[0];  
    	  weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];  
    	  weatherToday = weatherToday + "\n气温："  
    			  + detail.getProperty(5).toString();  
    	  weatherToday = weatherToday + "\n风力："  
    			  + detail.getProperty(7).toString() + "\n";  
    	  System.out.println("weatherToday is " + weatherToday);  
    	  Toast.makeText(this, weatherToday, 
    	  Toast.LENGTH_LONG).show();  
    	  
  		NotificationManager mNotificationManager = (NotificationManager)
				getSystemService(Context.NOTIFICATION_SERVICE);
				int icon = android.R.drawable.stat_sys_warning;
				long when = System.currentTimeMillis();
				Notification notification = new Notification(icon, null, when);
				notification.defaults = Notification.DEFAULT_SOUND;
				Context context = getApplicationContext(); 
				
				Intent notificationIntent = new Intent(this, WebServiseActivity.class);
				
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
				
				notification.setLatestEventInfo(this, "weatherToday", weatherToday, contentIntent);
				mNotificationManager.notify(0, notification);
				
				
				String ns = Context.NOTIFICATION_SERVICE;

				NotificationManager mNotificationManager2 = (NotificationManager)getSystemService(ns);

				//定义Notification的各种属性

				int icon2 = R.drawable.ic_action_search; //通知图标

				CharSequence tickerText = "Hello"; //状态栏显示的通知文本提示

				long when2 = System.currentTimeMillis(); //通知产生的时间，会在通知信息里显示

				//用上面的属性初始化Nofification

				Notification notification2 = new Notification(icon2,tickerText,when2);

				RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.view_ui_bar);

				contentView.setImageViewResource(R.id.image, R.drawable.ic_action_search);

				contentView.setTextViewText(R.id.text, "Hello,this is JC");

				notification2.contentView = contentView;

				Intent notificationIntent2 = new Intent(this,WebServiseActivity.class);

				PendingIntent contentIntent2 = PendingIntent.getActivity(this,0,notificationIntent2,0);

				notification2.contentIntent = contentIntent2;
				//正在进行的
				notification2.flags=notification2.flags/* | Notification.FLAG_ONGOING_EVENT*/;

				//把Notification传递给NotificationManager

				mNotificationManager2.notify(0,notification2);
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(500);

    	 }  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_servise);
        mTextView = (TextView)findViewById(R.id.textview);
/*		ContentResolver cr = WebServiseActivity.this.getContentResolver();
		
		Uri uri = Uri.parse("content://com.realfame.provider.MifiProvider/WifiAp");//com.yy.test.studentprovider/student
		
		ContentValues cValues = new ContentValues();
		cValues.put("_id", 1);
		cValues.put("ssid", "mifi");
		cValues.put("security", "WPA PSK");
		cValues.put("password", "12345678");
		cValues.put("maxconnections", 8);
		cr.insert(uri, cValues);
		Toast.makeText(WebServiseActivity.this, "插入数据", Toast.LENGTH_LONG).show();*/
        //实例化SoapObject对象
        SoapObject request=new SoapObject(serviceNameSpace, getWeatherbyCityName);
//        request.addProperty("byProvinceName","陕西");
        request.addProperty("theCityName","西安");
      //获得序列化的Envelope
        envelope=new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.bodyOut=request;
        envelope.dotNet = true; 
        envelope.setOutputSoapObject(request);
        
//        envelope.setOutputSoapObject(request);
        
        (new MarshalBase64()).register(envelope);
        
        //Android传输对象
        transport=new HttpTransportSE(serviceURL);
        transport.debug=true;
        new Thread(){  
            @Override  
            public void run() {  
                // TODO Auto-generated method stub  
                super.run();  
                if (bool) {
                	handler.sendEmptyMessage(0);
                try {
        			transport.call(serviceNameSpace+getWeatherbyCityName, envelope);
        			handler.sendEmptyMessage(1);
        		} catch (IOException e) {
        			e.printStackTrace();
        			Log.d("zhengpanyong", "e: " + e.getMessage());
        			handler.sendEmptyMessage(0);
        		} catch (XmlPullParserException e) {
        			e.printStackTrace();
        			handler.sendEmptyMessage(0);
        		}
                }
            }  
        }.start();  

        mTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        try {
					if(envelope != null && envelope.getResponse()!=null){
						mTextView.setText(envelope.bodyIn.toString());
					}
				} catch (SoapFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

    }
}
