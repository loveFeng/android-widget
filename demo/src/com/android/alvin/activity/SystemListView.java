package com.android.alvin.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.alvin.adapter.SystemAdapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class SystemListView extends ListView {
	private List<String> sysProcessList = new ArrayList<String>();
	private Context context;
	private ActivityManager actManager;
	private PackageManager pm;
	private List<Drawable> icons;
	private List<String> lables;

	public SystemListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initContent();
	}

	public SystemListView(Context context) {
		super(context);
		this.context = context;
		initContent();
	}
	
	public void initContent(){
		if (this.sysProcessList.isEmpty()) {
			this.sysProcessList.add("system");
			this.sysProcessList.add("com.android.launcher2");
			this.sysProcessList.add("com.android.phone");
			this.sysProcessList.add("com.android.wallpaper");
			this.sysProcessList.add("com.google.process.gapps");
			this.sysProcessList.add("android.process.acore");
			this.sysProcessList.add("android.process.media");
		}
		actManager = (ActivityManager)context.getSystemService(Activity.ACTIVITY_SERVICE);
		pm = context.getPackageManager();
		
		getSystemProcesses();
	}

	public void getSystemProcesses(){
    	List<RunningAppProcessInfo> runningApp = actManager.getRunningAppProcesses();
        icons = new ArrayList<Drawable>();
        lables = new ArrayList<String>();
        //pids = new ArrayList<Integer>();
        //packageList = new ArrayList<String>();
        
        for(RunningAppProcessInfo tempApp : runningApp){
        	for(String pkgName :tempApp.pkgList){
        		PackageInfo pi;
				try {
					pi = pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
					ApplicationInfo ai = pi.applicationInfo;
					if(sysProcessList.contains(tempApp.processName.split(":")[0])){
						icons.add(pm.getApplicationIcon(ai));
						lables.add(pm.getApplicationLabel(ai).toString());
						//pids.add(tempApp.pid);
						//packageList.add(pkgName);
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
        	}
        }
        
        SystemAdapter system_adapter = new SystemAdapter(context,icons,lables);
        setAdapter(system_adapter);
    }
}
