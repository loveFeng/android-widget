package com.android.alvin.activity;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;

public class TaskManager extends Activity {
	
	private TabHost tabHost;
	private ApplicationManager applicationManager;
	private int currentTab;
	private InstallLayout installLayout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar
        setContentView(R.layout.app_main);
        
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        
        tabHost.addTab(tabHost.newTabSpec("application")
        		.setIndicator("",getResources().getDrawable(R.drawable.app_gray))
        		.setContent(R.id.app_layout)
        );
        tabHost.addTab(tabHost.newTabSpec("service")
        		.setIndicator("",getResources().getDrawable(R.drawable.system_white))
        		.setContent(R.id.system_layout)
        );
        tabHost.addTab(tabHost.newTabSpec("uninstall")
        		.setIndicator("",getResources().getDrawable(R.drawable.install_white))
        		.setContent(R.id.install_layout)
        );
        tabHost.setCurrentTab(0);
        currentTab = 0;
        applicationManager = new ApplicationManager(TaskManager.this);
        applicationManager.appProcessManager();
        installLayout = (InstallLayout)findViewById(R.id.install_layout);
        
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId) {
				int index = tabHost.getCurrentTab();
				System.out.println("Current Tab ===="+index+"    "+currentTab);
				ImageView imageview = (ImageView)tabHost.getCurrentTabView().findViewById(android.R.id.icon);
				if(index==0)
					imageview.setImageResource(R.drawable.app_gray);
				else if(index==1)
					imageview.setImageResource(R.drawable.system_gray);
				else if(index==2){
					imageview.setImageResource(R.drawable.install_gray);
					installLayout.initListView();
				}
				View view = tabHost.getTabWidget().getChildAt(currentTab);
				ImageView imageView2 = (ImageView)view.findViewById(android.R.id.icon);
				if(currentTab==0)
					imageView2.setImageResource(R.drawable.app_white);
				else if(currentTab==1)
					imageView2.setImageResource(R.drawable.system_white);
				else if(currentTab==2)
					imageView2.setImageResource(R.drawable.install_white);
				currentTab = index;
			}
        });
    }
    
    
    
}