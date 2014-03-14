package com.qqyumidi.shortcutdemo;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TiebaActivity extends Activity {

	private TextView textView;
	private Button button;
	private String tName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tieba_activity);

		Intent intent = getIntent();
		tName = intent.getStringExtra("tName");

		textView = (TextView) findViewById(R.id.textview1);
		textView.setText(getString(R.string.shutcut_activityName) + tName);

		button = (Button) findViewById(R.id.button3);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tName == null) {
					tName = getString(R.string.app_name);
				}
				addShortCut(tName);
			}
		});
	}

	private void addShortCut(String tName) {
		// 安装的Intent  
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷名称  
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, tName);
		// 快捷图标是允许重复
		shortcut.putExtra("duplicate", false);

		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.putExtra("tName", tName);
		shortcutIntent.setClassName("com.qqyumidi.shortcutdemo", "com.qqyumidi.shortcutdemo.AppStart");
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		// 快捷图标  
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		// 发送广播  
		sendBroadcast(shortcut);
	}
}
