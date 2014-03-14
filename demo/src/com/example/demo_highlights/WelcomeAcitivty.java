package com.example.demo_highlights;

import com.example.demo_highlights.slidingmenu.activity.SlidingActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeAcitivty extends Activity {
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_welcome);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		loadMainUI();
		
		ImageView flag = (ImageView) findViewById(R.id.img_logo);
		AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(1000);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent intent = new Intent(WelcomeAcitivty.this,
						/*DEMOMainActivity*/SlidingActivity.class);
				WelcomeAcitivty.this.startActivity(intent);
				overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
				WelcomeAcitivty.this.finish();
			}
		});
//		bg.setAnimation(anim);
		flag.setAnimation(anim);
		anim.start();
	}
	
	/**
	 * 检查是否浏览过首次开机向导
	 */
	private boolean isFirstShow(){
		if(TextUtils.isEmpty(sp.getString("isFirst", ""))){
			return true;
		}else{
			return false;
		}
	}
	
	private void loadMainUI() {
		Intent intent;
		if(isFirstShow()){
			Editor ed = sp.edit();
			ed.putString("isFirst", "false");
			ed.commit();
			 intent = new Intent(this,ShowLogoActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
}
