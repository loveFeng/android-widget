package com.example.demo_highlights;


import com.example.demo_highlights.slidingmenu.activity.SlidingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class ShowLogoActivity extends Activity implements OnViewChangeListener {
	
	   	private MyScrollLayout main_myScroll;
		private LinearLayout main_point;
		private RelativeLayout main_layout;
		private Button startBtn;
		private int count;
		private ImageView[] imgs;
		private int currentItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showlogo_activity);
//      ��ʼ��һЩ�ؼ�
		initView();
	}
	
	private void initView() {
		/*
		 * ��ʼ������ؼ�
		 */
		main_myScroll = (MyScrollLayout) findViewById(R.id.main_myScroll);
		main_point = (LinearLayout) findViewById(R.id.main_point);
		main_layout = (RelativeLayout) findViewById(R.id.main_layout);
		//��ȡ��ť
		startBtn = (Button) findViewById(R.id.startBtn);
//		ע�����
		startBtn.setOnClickListener(onClick);
		
		count = main_myScroll.getChildCount();
		
		imgs = new ImageView[count];
		for(int i=0;i<count;i++){
			imgs[i] = (ImageView) main_point.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		
		//���õ�ǰ��
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		//���ý���仯����
		main_myScroll.SetOnViewChangeListener(this);
	}
	
	//������
	private View.OnClickListener onClick = new View.OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
//				main_myScroll.setVisibility(View.GONE);//�������õ�ʲô?����
//				main_point.setVisibility(View.GONE);
////				���������汳������Ϊ-->
//				main_layout.setBackgroundResource(R.drawable.w1);
//				���ö���Ч��
				Intent intent = new Intent(ShowLogoActivity.this,/*DEMOMainActivity*/SlidingActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	public void OnViewChange(int position) {
		setCurrentPoint(position);
	}
	
	private void setCurrentPoint(int position){
		if(position < 0 || position >count-1 || currentItem == position)
			return;
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}
	
}
