package com.ichliebephone;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

//�̳�PreferenceActivity����ʵ��OnPreferenceChangeListener��OnPreferenceClickListener����ӿ�
public class Settings extends PreferenceActivity implements OnPreferenceClickListener,
OnPreferenceChangeListener{
	//������ر���
	String updateSwitchKey;
	String updateFrequencyKey;
	CheckBoxPreference updateSwitchCheckPref;
	ListPreference updateFrequencyListPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //��xml�ļ������Preference��
        addPreferencesFromResource(R.xml.preferencesii);
        //��ȡ����Preference
        updateSwitchKey = getResources().getString(R.string.auto_update_switch_key);
        updateFrequencyKey = getResources().getString(R.string.auto_update_frequency_key);
        updateSwitchCheckPref = (CheckBoxPreference)findPreference(updateSwitchKey);
        updateFrequencyListPref = (ListPreference)findPreference(updateFrequencyKey);
        //Ϊ����Preferenceע�����ӿ�    
        updateSwitchCheckPref.setOnPreferenceClickListener(this);
        updateFrequencyListPref.setOnPreferenceClickListener(this); 
        updateSwitchCheckPref.setOnPreferenceChangeListener(this);
        updateFrequencyListPref.setOnPreferenceChangeListener(this);
              
    }
	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		Log.v("SystemSetting", "preference is clicked");
		Log.v("Key_SystemSetting", preference.getKey());
		//�ж����ĸ�Preference�������
		if(preference.getKey().equals(updateSwitchKey))
		{
			Log.v("SystemSetting", "checkbox preference is clicked");
		}
		else if(preference.getKey().equals(updateFrequencyKey))
		{
			Log.v("SystemSetting", "list preference is clicked");
		}
		else
		{
			return false;
		}
		return true;
	}
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		Log.v("SystemSetting", "preference is changed");
		Log.v("Key_SystemSetting", preference.getKey());
		//�ж����ĸ�Preference�ı���
		if(preference.getKey().equals(updateSwitchKey))
		{
			Log.v("SystemSetting", "checkbox preference is changed");
		}
		else if(preference.getKey().equals(updateFrequencyKey))
		{
			Log.v("SystemSetting", "list preference is changed");
		}
		else
		{
			//����false��ʾ�����?�ı�
			return false;
		}
		//����true��ʾ����ı�
		return true;
	}
}
