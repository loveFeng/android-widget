package com.ichliebephone;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class AndroidPreferenceDemoII extends Activity {
    /** Called when the activity is first created. */
	// �˵���
	final private int menuSettings=Menu.FIRST;
	private static final int REQ_SYSTEM_SETTINGS = 0;  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_preferences);
        //add virtual menu
        try {  
            getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));  
        }catch (NoSuchFieldException e) {  
            // Ignore since this field won't exist in most versions of Android  
        }catch (IllegalAccessException e) {  
            Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);  
        }  
      //add end virtual menu
    }
    //�����˵�
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
		// �����˵�
    	menu.add(0, menuSettings, 2, "menu1");
		return super.onCreateOptionsMenu(menu);
    }
    //�˵�ѡ���¼�����
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
    	switch (item.getItemId())
    	{
	    	case menuSettings:
	    		//ת��Settings���ý���
	    		startActivityForResult(new Intent(this, Settings.class), REQ_SYSTEM_SETTINGS);
	    		break;
	    	default:
	    		break;
    	}
    	return super.onMenuItemSelected(featureId, item);
    }
    //Settings���ý��淵�صĽ��
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_SYSTEM_SETTINGS)
		{
			//��ȡ���ý���PreferenceActivity�и���Preference��ֵ
	        String updateSwitchKey = getResources().getString(R.string.auto_update_switch_key);
	        String updateFrequencyKey = getResources().getString(R.string.auto_update_frequency_key);
	        //ȡ���������Ӧ�ó����SharedPreferences
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			Boolean updateSwitch = settings.getBoolean(updateSwitchKey, true);
			String updateFrequency = settings.getString(updateFrequencyKey, "10");
			//��ӡ���
			Log.v("CheckBoxPreference_Main", updateSwitch.toString());
			Log.v("ListPreference_Main", updateFrequency);
		}
		else
		{
			//����Intent���صĽ��
		}
    }
}