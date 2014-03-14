package com.android.alvin.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.alvin.adapter.TaskAdapter;
import com.example.demo_highlights.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationManager {

	private Context context;
	public static String PREF_FILENAME = "TaskManager.conf";
	public static String PREF_EXCLUDE_LIST = "excludeList";
	private TabHost tabHost;
	private TextView leftMemory_tv;
	private TextView pc_tv;
	private ListView app_list;
	private ActivityManager actManager;
	private PackageManager pm;
	private List<Drawable> icons;
	private List<String> lables;
	private List<Integer> pids;
	private List<String> packageList;
	private Button refreshButton;
	private Button killButton;
	private Button killAllButton;
	private List<String> sysProcessList = new ArrayList<String>();
	private List<String> excludeProcessList = new ArrayList<String>();
	private TaskAdapter app_adapter;
	private SharedPreferences pref;
	private TaskManager taskManager;
	
	public ApplicationManager(TaskManager taskManager){
		this.taskManager = taskManager;
		actManager = (ActivityManager)taskManager.getSystemService(Activity.ACTIVITY_SERVICE);
        pm = taskManager.getPackageManager();
        pref = taskManager.getSharedPreferences(PREF_FILENAME, Activity.MODE_PRIVATE);
	}
	
	 public void appProcessManager(){
	    	leftMemory_tv = (TextView)taskManager.findViewById(R.id.left_memory);
	        pc_tv = (TextView)taskManager.findViewById(R.id.process_counts);
	        app_list = (ListView)taskManager.findViewById(R.id.application_list);
	        refreshButton = (Button)taskManager.findViewById(R.id.refresh);
	        killButton = (Button)taskManager.findViewById(R.id.kill);
	        killAllButton = (Button)taskManager.findViewById(R.id.kill_all);
	        
	        //Load Exclude Process List
	        loadExcludeProcess();
	        //Add System Process
	        if (this.sysProcessList.isEmpty()) {
				this.sysProcessList.add("system");
				this.sysProcessList.add("com.android.launcher2");
				this.sysProcessList.add("com.android.phone");
				this.sysProcessList.add("com.android.wallpaper");
				this.sysProcessList.add("com.google.process.gapps");
				this.sysProcessList.add("android.process.acore");
				this.sysProcessList.add("android.process.media");
			}
	        
	        getAppProcesses();
	        
	        refreshButton.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					getAppProcesses();
				}
	        });
	        //Kill Process
	        killButton.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					List<String> selectedProcessList = app_adapter.getSelectedList();
					if(selectedProcessList.size()>0){
						boolean haveSelf = false;
						for(String position : selectedProcessList){
							String packageName = packageList.get(Integer.parseInt(position));
							if(packageName.equals("com.android.alvin.activity")){
								haveSelf = true;
								continue;
							}
							Log.i("======================Task Manager","Kill process "+packageName);
							actManager.restartPackage(packageName);
						}
						if(haveSelf){
							Log.i("======================Task Manager","Kill process com.android.alvin.activity");
							actManager.restartPackage("com.android.alvin.activity");
						}
						Toast.makeText(taskManager, R.string.process_killed, Toast.LENGTH_SHORT).show();
						selectedProcessList.clear();
						app_adapter.removeAllItems();
						getAppProcesses();
					}
				}
	        	
	        });
	        //Kill all processes
	        killAllButton.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					boolean haveSelf = false;
					for(String packageName : packageList){
						if(excludeProcessList.contains(packageName))continue;
						if(packageName.equals("com.android.alvin.activity")){
							haveSelf = true;
							continue;
						}
						Log.i("======================Task Manager","Kill process "+packageName);
						actManager.restartPackage(packageName);
						actManager.killBackgroundProcesses(packageName);
					}
					if(haveSelf){
						Log.i("======================Task Manager","Kill process com.android.alvin.activity");
						actManager.restartPackage("com.android.alvin.activity");
						actManager.killBackgroundProcesses("com.android.alvin.activity");
					}
					Toast.makeText(taskManager, R.string.process_killed, Toast.LENGTH_SHORT).show();
					app_adapter.removeAllItems();
					getAppProcesses();
				}
	        });
	        
	        app_list.setOnItemLongClickListener(new ListView.OnItemLongClickListener(){
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, final View view,
						final int position, long arg3) {
					final String packageName = packageList.get(position);
					OnClickListener listener1 = new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
							if(which==0){
								killProcess(packageName);
							}else if(which==1){
								excludeApplication(packageName, view);
							}else if(which==2){
								cancelExcludeApplication(packageName, view);
							}else if (which==3){
								installApplication(packageName);
							}
						}
					};
					new AlertDialog.Builder(taskManager)
						.setTitle(R.string.operate)
						.setItems(R.array.operates,listener1)
						.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
					return true;
				}
	        });
	        
	        app_list.setOnItemClickListener(new ListView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
						long arg3) {
					ImageView checkBoxView = (ImageView)view.findViewById(R.id.checkbox_icon);
					if(excludeProcessList.contains(packageList.get(position))) return;
					if(app_adapter.getSelectedList().contains(String.valueOf(position))){
						app_adapter.removeSelectedItem(String.valueOf(position));
						checkBoxView.setImageResource(R.drawable.checkbox3);
					}else{
						app_adapter.addSelectedItem(String.valueOf(position));
						checkBoxView.setImageResource(R.drawable.checkbox4);
					}
				}
	        	
	        });
	    }
	    
	    public void getAppProcesses(){
	    	List<RunningAppProcessInfo> runningApp = actManager.getRunningAppProcesses();
	        icons = new ArrayList<Drawable>();
	        lables = new ArrayList<String>();
	        pids = new ArrayList<Integer>();
	        packageList = new ArrayList<String>();
	        
	        for(RunningAppProcessInfo tempApp : runningApp){
	        	for(String pkgName :tempApp.pkgList){
	        		PackageInfo pi;
					try {
						pi = pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
						ApplicationInfo ai = pi.applicationInfo;
						if(!sysProcessList.contains(tempApp.processName.split(":")[0])){
							icons.add(pm.getApplicationIcon(ai));
							lables.add(pm.getApplicationLabel(ai).toString());
							pids.add(tempApp.pid);
							packageList.add(pkgName);
						}
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
	        	}
	        }
	        
	        app_adapter = new TaskAdapter(taskManager,icons,lables,packageList,excludeProcessList);
	        app_list.setAdapter(app_adapter);
	        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
	        actManager.getMemoryInfo(memoryInfo);
	        leftMemory_tv.setText((memoryInfo.availMem>>20)+"M  ");
	        pc_tv.setText(lables.size()+"");
	    }
	    
	    public void killProcess(String packageName){
	    	Log.i("======================Task Manager","Kill process "+packageName);
			actManager.restartPackage(packageName);
			Toast.makeText(taskManager, R.string.process_killed, Toast.LENGTH_SHORT).show();
			getAppProcesses();
	    }
	    
	    public void excludeApplication(String packageName,View view){
	    	//Exclude application
			if(!excludeProcessList.contains(packageName)){
				excludeProcessList.add(packageName);
				ImageView checkBoxView = (ImageView)view.findViewById(R.id.checkbox_icon);
				checkBoxView.setImageResource(R.drawable.exclude);
				saveExcludeProcess();
			}
	    }
	    
	    public void cancelExcludeApplication(String packageName, View view){
	    	//Cancel exclude application
			if(excludeProcessList.contains(packageName)){
				excludeProcessList.remove(packageName);
				ImageView checkBoxView = (ImageView)view.findViewById(R.id.checkbox_icon);
				checkBoxView.setImageResource(R.drawable.checkbox3);
				saveExcludeProcess();
			}
	    }
	    
	    public void installApplication(String packageName){
	    	//Install application
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:"+packageName));
			taskManager.startActivity(intent);
	    }
	    
	    
	    public void loadExcludeProcess(){
	    	excludeProcessList.clear();
	    	for(int i=0;i<64;i++){
	    		String packageName = pref.getString(PREF_EXCLUDE_LIST+i, null);
	    		if(packageName==null) break;
	    		excludeProcessList.add(packageName);
	    	}
	    }
	    
	    public void saveExcludeProcess(){
	    	if(excludeProcessList.size()>0){
	    		SharedPreferences.Editor editor = pref.edit();
	    		for(int i=0;i<64;i++){
	    			String packageName = excludeProcessList.size()>i ? excludeProcessList.get(i) : null;
	    			editor.putString(PREF_EXCLUDE_LIST+i, packageName);
	    		}
	    		editor.commit();
	    	}
	    }
}
