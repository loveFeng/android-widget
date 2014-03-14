package com.aven.qqdemo;

import java.util.ArrayList;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

/**
 * �����Gallery��������
 * 
 * @author user
 * 
 */
public class MyGalleryAdapter extends BaseAdapter{
	public static final int MESSAGE_BITMAP = 20;
	private Context context;
	private ArrayList<Beauty> data;
	private Handler handler;
	private LayoutInflater layoutInflater;
	private ViewHolder viewHolder;
	private Utils utils;
	private LinearLayout.LayoutParams imageLayoutParams;
	private int width;
	private int height;
	private Handler galleryHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Utils.MESSAGE_NOTIFY_GALLERY:
				String url = msg.getData().getString("url");
				((ImageView) viewHolder.imageView.findViewWithTag(url)).setImageBitmap((Bitmap) msg.obj);
				//viewHolder.imageView.setImageBitmap((Bitmap) msg.obj);
				break;
			}
		};
	};

	public MyGalleryAdapter(Context context, ArrayList<Beauty> data,
			Handler handler) {
		this.context = context;
		this.data = data;
		this.handler = handler;
		this.utils = new Utils();
		this.layoutInflater = LayoutInflater.from(context);
		this.width = (int) (MainActivity.screenWidth);
		this.height = (int) (MainActivity.screenHeight - MainActivity.grid_linear_height)/2;
		this.imageLayoutParams = new LinearLayout.LayoutParams(width, height);
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

	static class ViewHolder {
		ImageView imageView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (null == convertView) {
			convertView = layoutInflater.inflate(R.layout.gallery_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView_gallery);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Beauty beauty = data.get(position);
		String url = beauty.getImageUrl();
		viewHolder.imageView.setTag(url);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		viewHolder.imageView.setLayoutParams(params);
		viewHolder.imageView.setScaleType(ScaleType.FIT_XY);
		viewHolder.imageView.setImageResource(android.R.drawable.title_bar);// ����û����ΪĬ��ͼƬ
		utils.loadGalleryImage(url, this, viewHolder, handler);
		
		
		
		/*if (bitmap == null) {
			viewHolder.imageView.setBackgroundResource(R.drawable.item3);
		}
		viewHolder.imageView.setImageBitmap(bitmap);*/
		return convertView;
	}

	private Bitmap bitmap;

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	class MyGalleryThread extends Thread {
		private String url;
		private Handler galleryHandler;
		
		public MyGalleryThread (String url, Handler galleryHandler) {
			this.url = url;
			this.galleryHandler = galleryHandler;
		}
		@Override
		public void run() {
			Bitmap bitmap = utils.loadImageFromInternet(url);
			Message message = galleryHandler.obtainMessage(MESSAGE_BITMAP, bitmap);
			galleryHandler.sendMessage(message);
		}
		
	}

	
	
	

}
