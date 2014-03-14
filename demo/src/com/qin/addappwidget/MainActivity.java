
package com.qin.addappwidget;


import com.example.demo_highlights.R;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{

    private static String TAG = "AddAppWidget" ;
    
    private Button btAddShortCut;
    private LinearLayout linearLayout ;  // װ��Appwidget�ĸ���ͼ
    
    private static final int MY_REQUEST_APPWIDGET = 1;
    private static final int MY_CREATE_APPWIDGET = 2;
    
    private static final int HOST_ID = 1024 ;
    
    private AppWidgetHost  mAppWidgetHost = null ;
    AppWidgetManager appWidgetManager = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_addappwidget);

        btAddShortCut = (Button) findViewById(R.id.bt_addShortcut);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout) ;
      
        //�����hostid������ָ����AppWidgetHost ����Activity�ı��Id�� ֱ������Ϊһ������ֵ�� ��
        mAppWidgetHost = new AppWidgetHost(MainActivity.this, HOST_ID) ; 
        
        //Ϊ�˱�֤AppWidget�ļ�ʱ���� �� ������Activity��onCreate/onStar�������ø÷���
        // ��Ȼ������onStop�����У�����mAppWidgetHost.stopListenering() ֹͣAppWidget����
        mAppWidgetHost.startListening() ;
        
        //���AppWidgetManager����
        appWidgetManager = AppWidgetManager.getInstance(MainActivity.this) ;
        
        
        btAddShortCut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                 //��ʾ�����ܴ���AppWidget���б� ���ʹ� ACTION_APPWIDGET_PICK ��Action
                 Intent  pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK) ;
                 
                 //��ϵͳ����һ���µ�appWidgetId ����appWidgetId�����Ƿ���ActionΪACTION_APPWIDGET_PICK
                 //  ����ѡ���AppWidget�� �� ��ˣ����ǿ���ͨ�����appWidgetId��ȡ��AppWidget����Ϣ��
                 
                 //Ϊ��ǰ���ڽ������һ���µ�appWidgetId 
                 int newAppWidgetId = mAppWidgetHost.allocateAppWidgetId() ;
                 Log.i(TAG, "The alinearLayoutocated appWidgetId is ----> " + newAppWidgetId) ;
                 
                 //��ΪIntent����ֵ �� ��appWidgetId������ѡ����AppWidget��               
                 pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, newAppWidgetId) ;
                 
                 //ѡ��ĳ��AppWidget���������أ����ص�onActivityResult()���� 
                 startActivityForResult(pickIntent , MY_REQUEST_APPWIDGET) ;
                                  
            }
        });
    }

    // ���
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //ֱ�ӷ��أ�û��ѡ���κ�һ�� �����簴Back��
        if(resultCode == RESULT_CANCELED)
           return ;
        
        switch(requestCode){
            case MY_REQUEST_APPWIDGET :
                Log.i(TAG, "MY_REQUEST_APPWIDGET intent info is -----> "+data ) ;
                int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID) ;
                
                Log.i(TAG, "MY_REQUEST_APPWIDGET : appWidgetId is ----> " + appWidgetId) ;
                
                //�õ���Ϊ��Ч��id
                if(appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
                    //��ѯָ��appWidgetId�� AppWidgetProviderInfo���� �� ����xml�ļ����õ�<appwidget-provider />�ڵ���Ϣ
                    AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;
                    
                    //���������configure���� �� ��android:configure = "" ����Ҫ�ٴ�������configureָ�������ļ�,ͨ��Ϊһ��Activity
                    if(appWidgetProviderInfo.configure != null){
                        
                        Log.i(TAG, "The AppWidgetProviderInfo configure info -----> " + appWidgetProviderInfo.configure ) ;
                        
                        //���ô�Action
                        Intent intent  = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE) ;
                        intent.setComponent(appWidgetProviderInfo.configure) ;
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        
                        
                        startActivityForResult(intent , MY_CREATE_APPWIDGET) ;               
                    }
                    else  //ֱ�Ӵ���һ��AppWidget
                        onActivityResult(MY_CREATE_APPWIDGET , RESULT_OK , data) ;  //����ͬ���򵥻ص�����                       
                }           
                break ;
            case  MY_CREATE_APPWIDGET:
                completeAddAppWidget(data) ;
                break ;
        }
        
    }
   
    //��ǰ��ͼ���һ���û�ѡ���
    private void completeAddAppWidget(Intent data){
        Bundle extra = data.getExtras() ;
        int appWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID , -1) ;
        //��ͬ������Ļ�ȡ��ʽ
        //int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID) ;
        
        Log.i(TAG, "completeAddAppWidget : appWidgetId is ----> " + appWidgetId) ;
        
        if(appWidgetId == -1){
            Toast.makeText(MainActivity.this, "��Ӵ���С��������", Toast.LENGTH_SHORT) ;
            return ;
        }
        
        AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;
        
        AppWidgetHostView hostView = mAppWidgetHost.createView(MainActivity.this, appWidgetId, appWidgetProviderInfo);
                
        //linearLayout.addView(hostView) ; 
        
        int widget_minWidht = appWidgetProviderInfo.minWidth ;
        int widget_minHeight = appWidgetProviderInfo.minHeight ;
        //���ó���  appWidgetProviderInfo ����� minWidth ��  minHeight ����
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(widget_minWidht, widget_minHeight);
        //�����LinearLayout����ͼ��
        linearLayout.addView(hostView,linearLayoutParams) ;
    }
}
