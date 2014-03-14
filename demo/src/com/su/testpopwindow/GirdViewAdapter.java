package com.su.testpopwindow;

import com.example.demo_highlights.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GirdViewAdapter extends BaseAdapter {

	private int imgRecouse[];

	private String title[];

	// -------------------

	LayoutInflater inflater;

	Context context;

	public GirdViewAdapter(Context context) {

		this.context = context;

		inflater = LayoutInflater.from(context);

		imgRecouse = new int[] { R.drawable.s1, R.drawable.s2, R.drawable.s3,
				R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7,
				R.drawable.s8 };

		title = new String[] { "������ǩ", "��ǩ/��ʷ", "ˢ��", "ҹ��ģʽ", "�˻�", "�����ļ�",
				"ȫ��", "�˳�" };
	}

	public int getCount() {

		return imgRecouse.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgRecouse[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View currentView, ViewGroup arg2) {

		currentView = inflater.inflate(R.layout.imagebutton, null);

		ImageView imageView = (ImageView) currentView
				.findViewById(R.id.imgbtn_img);
		TextView textView = (TextView) currentView
				.findViewById(R.id.imgbtn_text);

		imageView.setBackgroundResource(imgRecouse[position]);
		textView.setText(title[position]);

		return currentView;
	}

}
