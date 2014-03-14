package com.aven.qqdemo;

import java.util.ArrayList;

import com.example.demo_highlights.R;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
/**
 * ���ǵڶ���Fragment�����ListView��adapter
 * @author user
 *
 */
public class ListAdapter extends BaseAdapter {
	private Handler handler;
	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<Beauty> data;
	private Utils util;
	private RelativeLayout.LayoutParams imageLayoutParams;;
	private int width = 0;
	private int height = 0;
	
	public ListAdapter(Context context, ArrayList<Beauty> data, Handler handler) {
		this.context = context;
		this.data = data;
		this.handler = handler;
		this.layoutInflater = LayoutInflater.from(context);
		//��ͬ��fragment��Ҫ���õ�Ĭ��ͼƬ�Ĺ��ͬ��������������в�ͬ�ĳ�ʼ����screenWidth��screenHeight
		//������MainActivity�ж���ľ�̬ȫ�ֱ���
		this.width = (int) (MainActivity.screenWidth - 250);
		this.height = (int) (MainActivity.screenHeight - MainActivity.grid_linear_height)/2 - 100;
		this.imageLayoutParams = new RelativeLayout.LayoutParams(width, height);
		//��ÿһ��adapter�д���һ��Utils������
		this.util = new Utils(); 
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	static class ViewHolder{
		ImageView imageView;
		TextView textView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.lay2_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.list_imageView);
			holder.textView = (TextView) convertView.findViewById(R.id.list_textView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//ȡ������е�Beautyʵ����
		Beauty beauty = data.get(position);
		String url = beauty.getImageUrl();
		String name = beauty.getName();
		//������ͬ��LayoutParams������ImageView��������
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		holder.imageView.setLayoutParams(params);
		holder.imageView.setScaleType(ScaleType.FIT_XY);
		//���ù������еļ���ͼƬ�ķ�����������url,adapter,holder,handler
		util.loadListImage(url, this, holder, handler);
		holder.textView.setText(name);
		return convertView;
	}

}
