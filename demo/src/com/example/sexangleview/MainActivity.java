package com.example.sexangleview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.demo_highlights.R;
import com.example.view.SexangleImageView;
import com.example.view.SexangleViewGroup;

public class MainActivity extends Activity implements OnClickListener {
	
	private SexangleViewGroup sexangleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_sexangle);
		
		sexangleView = (SexangleViewGroup) findViewById(R.id.sexangleView);
		findViewById(R.id.btn_add).setOnClickListener(this);
		findViewById(R.id.btn_remove).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			
			SexangleImageView view = new SexangleImageView(getApplicationContext());
			sexangleView.addView(view);
			
			break;

		case R.id.btn_remove:
			sexangleView.removeViewAt(sexangleView.getChildCount() - 1);
			break;
		}
		
	}

}
