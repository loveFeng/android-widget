package com.baidu.rocket;


import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView iv_rocket;
	private ImageView iv_top;
	private ImageView iv_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_huojian);
//		Intent intent = new Intent(this, RocketService.class);
//		startService(intent);
		iv_rocket = (ImageView) findViewById(R.id.iv_rocket);
		iv_top = (ImageView) findViewById(R.id.iv_top);
		iv_bottom = (ImageView) findViewById(R.id.iv_bottom);

		iv_bottom.setVisibility(View.VISIBLE);
		iv_top.setVisibility(View.VISIBLE);
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(600);
		iv_bottom.startAnimation(aa);

		AlphaAnimation aa2 = new AlphaAnimation(0.0f, 1.0f);
		aa2.setDuration(2000);
		iv_top.startAnimation(aa2);

		new Thread() {
			public void run() {
				for (int i = 0; i < 15; i++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							iv_rocket.layout(iv_rocket.getLeft(), 
									iv_rocket.getTop()-30, iv_rocket.getRight(), 
									iv_rocket.getBottom()-30);

						}
					});
				}
				finish();
			};
		}.start();

	}
}
