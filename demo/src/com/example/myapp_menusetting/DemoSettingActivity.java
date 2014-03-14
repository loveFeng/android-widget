package com.example.myapp_menusetting;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class DemoSettingActivity extends Activity {
	RightFragment mRightFragment;
    @SuppressLint({ "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_settings);
        mRightFragment = (RightFragment) getFragmentManager().findFragmentById(R.id.demo_setting);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_menu_setting_main, menu);
//        return true;
//    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mRightFragment.destorySlidingLayer()) {
			return;
		}
		
		super.onBackPressed();
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		
//
//        
//        switch (item.getItemId()) {
//        case R.id.menu1:
//            return true;
//        case R.id.menu2:
//            return true;
//        case R.id.menu3:
//            return true;
//        case R.id.menu4:
//            return true;
//        case R.id.menu5:
//            final Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
//            settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            startActivity(settings);
//            return true;
//        case R.id.menu6:
//            return true;
//    }
//		return super.onOptionsItemSelected(item);
//	}
}
