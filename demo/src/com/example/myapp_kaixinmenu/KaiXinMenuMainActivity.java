package com.example.myapp_kaixinmenu;


import com.example.demo_highlights.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class KaiXinMenuMainActivity extends Activity {

	private View mUgcView;
	private RelativeLayout mUgcLayout;
	private ImageView mUgc;
	private ImageView mUgcBg;
	private ImageView mUgcVoice;
	private ImageView mUgcPhoto;
	private ImageView mUgcRecord;
	private ImageView mUgcLbs;
	
	
	/**
	 * 判断当前的path菜单是否已经显示
	 */
	private boolean mUgcIsShowing = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kai_xin_menu_main);
        
        mUgcView = (View) findViewById(R.id.desktop_ugc);
		mUgcLayout = (RelativeLayout) mUgcView.findViewById(R.id.ugc_layout);
		mUgc = (ImageView) findViewById(R.id.ugc);
		mUgcBg = (ImageView) findViewById(R.id.ugc_bg);
		mUgcVoice = (ImageView) findViewById(R.id.ugc_voice);
		mUgcPhoto = (ImageView) findViewById(R.id.ugc_photo);
		mUgcRecord = (ImageView) findViewById(R.id.ugc_record);
		mUgcLbs = (ImageView) findViewById(R.id.ugc_lbs);
		setListener();
        
    }
    
	/**
	 * 关闭Path菜单
	 */
	public void closeUgc() {
		mUgcIsShowing = false;
		UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc, 500);
	}
    
    /**
	 * UI事件监听
	 */
	private void setListener() {
		// 头布局监听
/*		mTopLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 切换界面View为用户首页
				if (mOnChangeViewListener != null) {
					mOnChangeViewListener.onChangeView(ViewUtil.USER);
					mAdapter.setChoose(-1);
					mAdapter.notifyDataSetChanged();
				}
			}
		});*/
		// Path监听
		mUgcView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// 判断是否已经显示,显示则关闭并隐藏
				if (mUgcIsShowing) {
					mUgcIsShowing = false;
					UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
							500);
					return true;
				}
				return false;
			}
		});
		// Path监听
		mUgc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 判断是否显示,已经显示则隐藏,否则则显示
				mUgcIsShowing = !mUgcIsShowing;
				if (mUgcIsShowing) {
					UgcAnimations.startOpenAnimation(mUgcLayout, mUgcBg, mUgc,
							500);
				} else {
					UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
							500);
				}
			}
		});
		// Path 语音按钮监听
		mUgcVoice.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
//						mContext.startActivity(new Intent(mContext,
//								VoiceActivity.class));
						closeUgc();
					}
				});
				mUgcVoice.startAnimation(anim);
			}
		});
		// Path 拍照按钮监听
		mUgcPhoto.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
//						PhotoDialog();
						closeUgc();
					}
				});
				mUgcPhoto.startAnimation(anim);
			}
		});
		// Path 记录按钮监听
		mUgcRecord.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
//						mContext.startActivity(new Intent(mContext,
//								WriteRecordActivity.class));
						closeUgc();
					}
				});
				mUgcRecord.startAnimation(anim);
			}
		});
		// Path 签到按钮监听
		mUgcLbs.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Animation anim = UgcAnimations.clickAnimation(500);
				anim.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationRepeat(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
//						mContext.startActivity(new Intent(mContext,
//								CheckInActivity.class));
						closeUgc();
					}
				});
				mUgcLbs.startAnimation(anim);
			}
		});
		// 选项按钮监听
//		mSetUp.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				mContext.startActivity(new Intent(mContext, SetUpActivity.class));
//			}
//		});
	}

}
