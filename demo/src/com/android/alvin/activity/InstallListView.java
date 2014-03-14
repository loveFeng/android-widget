package com.android.alvin.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.alvin.adapter.InstallAdapter;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class InstallListView extends ListView implements OnItemClickListener {
	private Context context;
	private List<String> packageList;
	private List<Drawable> iconList;
	private List<String> labeList;
	private PackageManager packageManager;
	private List<String> sysProcessList;
	private InstallAdapter installAdapter;
	private ProgressDialog dialog;
	public static final int LOAD_FINISHED = 1001;
	
	public InstallListView(Context context) {
		super(context);
		this.context = context;
		setOnItemClickListener(this);
		//initContent();
	}
	
	public InstallListView(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context = context;
		setOnItemClickListener(this);
		//initContent();
	}
	
	public void initContent(){
		if (this.sysProcessList==null) {
			sysProcessList = new ArrayList<String>();
			this.sysProcessList.add("system");
			this.sysProcessList.add("com.android.launcher2");
			this.sysProcessList.add("com.android.phone");
			this.sysProcessList.add("com.android.wallpaper");
			this.sysProcessList.add("com.google.process.gapps");
			this.sysProcessList.add("android.process.acore");
			this.sysProcessList.add("android.process.media");
		}
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("加载中..");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.show();
		
		new Thread(){
			public void run(){
				packageManager = context.getPackageManager();
				iconList = new ArrayList<Drawable>();
				labeList = new ArrayList<String>();
				packageList = new ArrayList<String>();
				List<ApplicationInfo> appInfoList = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
				for(ApplicationInfo appInfo : appInfoList){
					//Filter system application
					if(!sysProcessList.contains(appInfo.processName.split(":")[0])){
						iconList.add(packageManager.getApplicationIcon(appInfo));
						labeList.add(packageManager.getApplicationLabel(appInfo).toString());
						packageList.add(appInfo.packageName);
					}
				}
				Message msg = new Message();
				msg.what = LOAD_FINISHED;
				InstallListView.this.handler.sendMessage(msg);
			}
		}.start();
		
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==LOAD_FINISHED){
				installAdapter = new InstallAdapter(context,iconList,labeList);
				setAdapter(installAdapter);
				dialog.cancel();
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		String packageName = packageList.get(position);
		installApplication(packageName);
	}
	
	public void installSelectedApplication(){
		List<String> selectedList = installAdapter.getSelectedList();
		if(selectedList.size()>0){
			for(int i=0;i<selectedList.size();i++){
				String packageName = packageList.get(Integer.parseInt(selectedList.get(i)));
				installApplication(packageName);
			}
		}
	}

	public void installApplication(String packageName){
    	//Install application
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:"+packageName));
		context.startActivity(intent);
    }
}
