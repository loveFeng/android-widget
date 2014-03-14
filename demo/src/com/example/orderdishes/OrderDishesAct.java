package com.example.orderdishes;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class OrderDishesAct extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView(){
		setContentView(R.layout.tabtest);
		parseHorizontalTab();
	}
	 Drawable icon_tab_1, icon_tab_2, icon_tab_3, icon_tab_4;
	 private void parseHorizontalTab() {
		   // ע������Ĵ����õ���android.R.id.tabhost���ڲ�������2��ID�����ǹ̶�����Ҫʹ�ù̶���ID:
		  
		   // ѡ���TabWidget->android:id/tabs
		   // ѡ�����ݣ�FrameLayout android:id="android:id/tabcontent"
		   final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		   tabHost.setup();
		   icon_tab_1 = this.getResources().getDrawable(R.drawable.icon1);
		   icon_tab_2 = this.getResources().getDrawable(R.drawable.icon2);
		   icon_tab_3 = this.getResources().getDrawable(R.drawable.icon3);
		   icon_tab_4 = this.getResources().getDrawable(R.drawable.icon4);
		   createHorizontalTab(tabHost);
		  }

		  private void createHorizontalTab(TabHost tabHost) {
		   tabHost.addTab(tabHost
		     .newTabSpec("tab1")
		     .setIndicator(
		       createIndicatorView(this, tabHost, icon_tab_1, "tab_1"))
		     .setContent(R.id.id_tab_view1));
		   tabHost.addTab(tabHost
		     .newTabSpec("tab2")
		     .setIndicator(
		       createIndicatorView(this, tabHost, icon_tab_2, "tab_2"))
		     .setContent(R.id.id_tab_view2));

		   tabHost.addTab(tabHost
		     .newTabSpec("tab3")
		     .setIndicator(
		       createIndicatorView(this, tabHost, icon_tab_3, "tab_3"))
		     .setContent(R.id.id_tab_view3));
		   tabHost.addTab(tabHost
		     .newTabSpec("tab4")
		     .setIndicator(
		       createIndicatorView(this, tabHost, icon_tab_4, "tab_4"))
		     .setContent(R.id.id_tab_view4));

		   TabWidget tw = tabHost.getTabWidget();
		   tw.setOrientation(LinearLayout.VERTICAL);//ע���ڴ˴����ô˲��� ʹTAB ��ֱ����
		 }
		  
		  /**
		   * �����Զ���� ѡ���ͼ
		  *
		   * @param context
		   * @param tabHost
		   * @param icon
		   * @return
		   */
		  private View createIndicatorView(Context context, TabHost tabHost,
		    Drawable icon, String title) {

		   LayoutInflater inflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		   View tabIndicator = inflater.inflate(R.layout.tab_indicator_horizontal,
		     tabHost.getTabWidget(), false);

		   final ImageView iconView = (ImageView) tabIndicator
		     .findViewById(R.id.icon);
		   final TextView titleView = (TextView) tabIndicator
		     .findViewById(R.id.title);
		   titleView.setText(title);
		   iconView.setImageDrawable(icon);
		   return tabIndicator;
		  }

}
