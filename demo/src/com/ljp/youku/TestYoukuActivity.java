package com.ljp.youku;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class TestYoukuActivity extends Activity {
	/** Called when the activity is first created. */
	private boolean areLevel2Showing = true, areLevel3Showing = true;
	private RelativeLayout relate_level2, relate_level3;

	private ImageButton home, menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_youku_menu);

		findViews();
		setListener();
		
	}

	private void findViews() {
		relate_level2 = (RelativeLayout) findViewById(R.id.relate_level2);
		relate_level3 = (RelativeLayout) findViewById(R.id.relate_level3);
		home = (ImageButton) findViewById(R.id.home);
		menu = (ImageButton) findViewById(R.id.menu);
	}

	private void setListener() {
		// 给大按钮设置点击事件
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areLevel2Showing) {
					MyAnimation.startAnimationsIn(relate_level2, 500);
				} else {
					if (areLevel3Showing) {
						MyAnimation.startAnimationsOut(relate_level2, 500, 500);
						MyAnimation.startAnimationsOut(relate_level3, 500, 0);
						areLevel3Showing = !areLevel3Showing;
					} else {
						MyAnimation.startAnimationsOut(relate_level2, 500, 0);
					}
				}
				areLevel2Showing = !areLevel2Showing;
			}
		});
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areLevel3Showing) {
					MyAnimation.startAnimationsIn(relate_level3, 500);
				} else {
					MyAnimation.startAnimationsOut(relate_level3, 500, 0);
				}
				areLevel3Showing = !areLevel3Showing;
			}
		});
	}

}