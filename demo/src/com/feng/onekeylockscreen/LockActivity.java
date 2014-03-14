package com.feng.onekeylockscreen;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.view.Menu;

public class LockActivity extends Activity {
	private static final String SHORT_CUT_EXTRAS = "com.terry.extra.short";
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
//		if (devicePolicyManager != null && devicePolicyManager.isAdminActive(Dar.getCn(this))) {
//			devicePolicyManager.lockNow();
//			finish();
//		}
//		super.onResume();
//	}

	private DevicePolicyManager devicePolicyManager=null;
    private static final int REQUEST_CODE_ADD_DEVICE_ADMIN=100;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		
        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
            finish();
            return;
        }
		
		if (devicePolicyManager.isAdminActive(Dar.getCn(this))) {
			devicePolicyManager.lockNow();
			finish();
		}else{
			startAddDeviceAdminAty();
		}
//		setContentView(R.layout.activity_main2);
	}

    void createShortCut() {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClass(this, this.getClass());
        shortcutIntent.putExtra(SHORT_CUT_EXTRAS, /*"娴嬭瘯鐨勫揩鎹锋柟寮�*/ "...");

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, /*"涓�敭閿�*/"一键锁屏");
        Parcelable shortIcon = Intent.ShortcutIconResource.fromContext(
                this, R.drawable.one_key_lock_screen);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortIcon);
        setResult(RESULT_OK, intent);
    }
	
	private void startAddDeviceAdminAty(){
    	Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                Dar.getCn(this));
        i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                /*getString(R.string.onekey_lock_prompt)*/"allow to lock screen");

		startActivityForResult(i, REQUEST_CODE_ADD_DEVICE_ADMIN);
    }
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	if (resultCode==Activity.RESULT_OK) {
    		devicePolicyManager.lockNow();
    		finish();
		}else{
			finish();
			//startAddDeviceAdminAty();
		}

    	super.onActivityResult(requestCode, resultCode, data);
    }
	 @Override
	    protected void onPause() {
//	    	finish();
	    	super.onPause();
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
//	        getMenuInflater().inflate(R.menu.activity_main, menu);
	        return true;
	    }

	    
}
