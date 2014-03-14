package com.zhy.myslipbutton;

import com.example.demo_highlights.R;
import com.zhy.myslipbutton.SlipButton.onChangeListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
//import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	private SlipButton slipButton;
	private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slipbutton);
        init();
        setListener();
    }

	private void init() {
		slipButton = (SlipButton) findViewById(R.id.splitbutton);
		button = (Button) findViewById(R.id.ringagain);
		slipButton.setCheck(true);
	}
	private void setListener(){
		slipButton.SetOnChangedListener(new onChangeListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				button.setText(CheckState?"Ture":"False");
			}
		});
	}
    
}
