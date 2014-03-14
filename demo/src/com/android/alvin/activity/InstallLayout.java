package com.android.alvin.activity;

import com.example.demo_highlights.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class InstallLayout extends RelativeLayout {
	private InstallListView installListView;
	private LinearLayout linearLayout;
	private Button refreshButton;
	private Button installButton;
	private Context context;
	public static final int LIST_VIEW_ID = 0x1000;
	public static final int REL_LAYOUT_ID = 0x1001;
	public static final int REFRESH_BUTTON_ID = 0x1001;
	public static final int INSTALL_BUTTON_ID = 0x1001;
	private RelativeLayout.LayoutParams listLayoutParams = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
	
	private RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	
	private LinearLayout.LayoutParams refreshButtonParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
	
	private LinearLayout.LayoutParams installButtonParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
	
	
	public InstallLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initContent();
	}
	
	public void initContent(){
		installListView = new InstallListView(context);
		installListView.setId(LIST_VIEW_ID);
		linearLayout = new LinearLayout(context);
		linearLayout.setId(this.REL_LAYOUT_ID);
		refreshButton = new Button(context);
		refreshButton.setId(this.REFRESH_BUTTON_ID);
		refreshButton.setText(context.getResources().getText(R.string.refresh));
		installButton = new Button(context);
		installButton.setId(this.INSTALL_BUTTON_ID);
		installButton.setText(context.getResources().getText(R.string.install));
		
		listLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
		listLayoutParams.addRule(RelativeLayout.ABOVE,REL_LAYOUT_ID);
		installListView.setLayoutParams(listLayoutParams);
		addView(installListView);
		
		relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		linearLayout.setLayoutParams(relativeLayoutParams);
		addView(linearLayout);
		
		refreshButtonParams.weight = 1;
		refreshButton.setLayoutParams(refreshButtonParams);
		linearLayout.addView(refreshButton);
		
		installButtonParams.weight = 1;
		installButton.setLayoutParams(installButtonParams);
		linearLayout.addView(installButton);
		
		refreshButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				installListView.initContent();
			}
		});
		
		installButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				installListView.installSelectedApplication();
			}
		});
		
	}

	public void initListView(){
		installListView.initContent();
	}
}
