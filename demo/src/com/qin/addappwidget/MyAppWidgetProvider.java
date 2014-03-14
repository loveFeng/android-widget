package com.qin.addappwidget;

import com.example.demo_highlights.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;


/**
 * �Զ��� AppWidget
 * @author qinjuning
 */
public class MyAppWidgetProvider extends AppWidgetProvider
{

    private static String TAG = "MyAppWidgetProvider";
    
    private static RemoteViews remoteViews = null ;
    
    private static boolean isDefaultIcon = true ;  //�Ƿ�����ΪĬ��ͼƬ
    
    
    private static int count = 0 ;
    
    
     // ÿ��ɾ������͵Ĵ���С����(AppWidget)ʱ���ᴥ�� ��ͬʱ����ACTION_APPWIDGET_DELETED�㲥��
     //     �ù㲥�ɱ�onReceive()�������ܵ�.
    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
        Log.i(TAG, "--- onDeleted ---");
    }
    // ���ɾ������͵Ĵ���С����(AppWidget)ʱ����   
    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
        Log.i(TAG, "--- onDisabled ---");
    }

    //��һ����Ӹ����͵Ĵ���С��������С����(AppWidget)ʱ����
    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
        Log.i(TAG, "--- onEnabled ---");
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        Log.i(TAG, "--- onReceive and action is ---" + intent.getAction() );      
    	
        
        Log.i(TAG, "--- idDefaultIcon---" + isDefaultIcon );    
        
        if(intent.getAction().equals("com.qin.action.CHANGE_IMAGE")){ 
            
        	
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                                 
            //����ΪĬ��ͼƬ
            if (remoteViews == null) {
            	return;
            }
            if(isDefaultIcon)
                remoteViews.setImageViewResource(R.id.img, R.drawable.icon);               
            else
            	remoteViews.setImageViewResource(R.id.img, R.drawable.girl); 
            
            //��������ָ���µ�RemoteViews����
            //remoteViews = new RemoteViews(context.getPackageName() , R.layout.main);
            	
            appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class), remoteViews);
            //��ͬ���������ַ�ʽ
            //������и����͵Ĵ���С����id
            //int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,MyAppWidgetProvider.class)); 
            //appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
            
            isDefaultIcon = !isDefaultIcon ;
        }
        
    }

    //ÿ�����һ�������͵Ĵ���С��������С����(AppWidget)���ᴥ����ͬʱ����ACTION_APPWIDGET_UPDATE�㲥
    // һ���ڸú���Ϊ��ʼ����ӵĴ���С���� , ��Ϊ�����RemoteViews
     
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
    	count ++ ;
    	System.out.println("---------------- count -------- " + count);
        // TODO Auto-generated method stub
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        
        RemoteViews remoteViews = updateWidgetView(context);
        
        //�Դ��ݹ����Ĳ���appWidgetIds[]������б������/��ʼ��
        int N = appWidgetIds.length;
        for(int i = 0 ; i < N ; i++){
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }
    
    private RemoteViews updateWidgetView(Context context){        

    	if(remoteViews == null)
    		 //��ʼ��remoteView����
    	    remoteViews = new RemoteViews(context.getPackageName() , R.layout.widget_change_image);
    	
    	//�������ͼƬ��Դ
        Intent  resetIntent = new Intent("com.qin.action.CHANGE_IMAGE"); 
        PendingIntent resetNoPending = PendingIntent.getBroadcast(context, 0, resetIntent, 0);
        
        //������Ӧ����
        remoteViews.setOnClickPendingIntent(R.id.btn_resetNumber, resetNoPending);       
        remoteViews.setImageViewResource(R.id.img, R.drawable.icon);    
        
        return remoteViews ;
    }

}
