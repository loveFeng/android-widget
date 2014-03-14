package com.aven.qqdemo;

import java.util.ArrayList;

import com.example.demo_highlights.R;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * ����ǵ�һ��Fragment�е�GridView��adapter��
 * @author user
 *
 */
public class GridAdapter extends BaseAdapter {
	private Handler handler;
	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<Beauty> data;
	private Utils utils;
	private LinearLayout.LayoutParams imageLayoutParams;
	private int width = 0;
	private int height = 0;
	
	public GridAdapter(Context context, Handler handler, ArrayList<Beauty> data) {
		this.context = context;
		this.handler = handler;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
		//��ͬ��fragment��Ҫ���õ�Ĭ��ͼƬ�Ĺ��ͬ��������������в�ͬ�ĳ�ʼ����screenWidth��screenHeight
		//������MainActivity�ж���ľ�̬ȫ�ֱ���
		this.width = (int) MainActivity.screenWidth - 20;
		this.height = (int) (MainActivity.screenHeight - MainActivity.grid_linear_height - 40)/2;
		this.imageLayoutParams = new LinearLayout.LayoutParams(width, height);
		//��ÿһ��adapter�д���һ��Utils������
		this.utils = new Utils();
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
			convertView = layoutInflater.inflate(R.layout.lay1_item, null);
			holder = new ViewHolder();
			//viewholder�����ͼ������
			holder.imageView = (ImageView) convertView.findViewById(R.id.grid_imageView);
			holder.textView = (TextView) convertView.findViewById(R.id.grid_textView);
			//������ͼ����ṹ
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//ȡ������е�Beautyʵ����
		Beauty beauty = data.get(position);
		String url = beauty.getImageUrl();
		String name = beauty.getName();
		//holder.imageView.setTag(url);
		
		//ΪimageView����ͼƬ
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//holder.imageView.setLayoutParams(params);
		Log.d("gridadapter", "adapterwidth="+imageLayoutParams.width+","+"adapterheight="+imageLayoutParams.height);
		//������ͬ��LayoutParams������ImageView��������
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		holder.imageView.setLayoutParams(params);
		holder.imageView.setScaleType(ScaleType.FIT_XY);
		//���ù������еļ���ͼƬ�ķ�����������url,adapter,holder,handler
		utils.loadGridImage(url, this, holder, handler);
		holder.textView.setText(name);
		
		return convertView;
	}

}
