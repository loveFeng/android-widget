package com.tutor.update;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.os.Bundle;

public class MainAcitivity extends Activity {
    

	private UpdateManager mUpdateManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_update);
        
        //���������汾�Ƿ���Ҫ����
        mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo();
    }     
}