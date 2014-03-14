package com.aven.qqdemo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.BaseAdapter;

import com.aven.qqdemo.GridAdapter.ViewHolder;

public class Utils {
	private static final int MAX_CAPACITY = 10; // 一级缓存的最大空间
	private static final long DELAY_BEFORE_PURGE = 10 * 1000;// 定时清理缓存
	public static final int MESSAGE_NOTIFY_GRID = 7;
	public static final int MESSAGE_NOTIFY_LIST = 8;
	public static final int MESSAGE_NOTIFY_GALLERY = 9;

	// 0.75是加载因子为经验值，true则表示按照最近访问量的高低排序，false则表示按照插入顺序排序
	private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
			MAX_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > MAX_CAPACITY) {// 当超过一级缓存阈值的时候，将老的值从一级缓存搬到二级缓存
				mSecondLevelCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			return false;
		};
	};

	/**
	 * 二级缓存，采用的是软引用，只有在内存吃紧的时候，软引用才会被回收
	 */
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			MAX_CAPACITY / 2);

	/**
	 * 定时清理缓存
	 */
	private Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clear();
		}
	};

	/**
	 * 清理缓存
	 */
	private void clear() {
		mFirstLevelCache.clear();
		mSecondLevelCache.clear();
	}

	private Handler mPurgeHandler = new Handler();

	/**
	 * 重置缓存清理的timer
	 */
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * 
	 * @param url
	 * @param myAdapter
	 * @param holder
	 */
	public void loadGridImage(String url, BaseAdapter adapter,
			ViewHolder holder, Handler handler) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			holder.imageView.setImageResource(R.drawable.title_bar);// 缓存没有设为默认图片
			GridImageLoadThread gridImageLoadThread = new GridImageLoadThread(
					url, adapter, holder, handler);
			gridImageLoadThread.start();
		} else {
			holder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}

	}

	public void loadListImage(String url, ListAdapter adapter,
			com.aven.qqdemo.ListAdapter.ViewHolder holder, Handler handler) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			holder.imageView.setImageResource(R.drawable.title_bar);// 缓存没有设为默认图片
			ListImageLoadThread listImageLoadThread = new ListImageLoadThread(
					url, adapter, holder, handler);
			listImageLoadThread.start();
		} else {
			holder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}

	}

	public void loadGalleryImage(String url, MyGalleryAdapter adapter,
			com.aven.qqdemo.MyGalleryAdapter.ViewHolder viewHolder,
			Handler handler) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.title_bar);// 缓存没有设为默认图片
			GalleryImageLoadThread galleryImageLoadThread = new GalleryImageLoadThread(
					url, adapter, viewHolder, handler);
			galleryImageLoadThread.start();
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
		/*//viewHolder.imageView.setImageResource(R.drawable.title_bar);// 缓存没有设为默认图片
		GalleryImageLoadThread galleryImageLoadThread = new GalleryImageLoadThread(
				url, adapter, viewHolder, handler);
		galleryImageLoadThread.start();*/
	}
	
	
	/**
	 * 放入缓存
	 * 
	 * @param url
	 * @param value
	 */
	public void addImage2Cache(String url, Bitmap value) {
		if (value == null || url == null) {
			return;
		}
		synchronized (mFirstLevelCache) {
			mFirstLevelCache.put(url, value);
		}
	}

	class ListImageLoadThread extends Thread {
		private String url;
		private BaseAdapter adapter;
		private com.aven.qqdemo.ListAdapter.ViewHolder holder;
		private Handler handler;

		public ListImageLoadThread(String url, ListAdapter adapter,
				com.aven.qqdemo.ListAdapter.ViewHolder holder, Handler handler) {
			this.url = url;
			this.adapter = adapter;
			this.holder = holder;
			this.handler = handler;
		}

		@Override
		public void run() {
			super.run();
			Bitmap bitmap = loadImageFromInternet(url);
			addImage2Cache(url, bitmap);
			Message message = handler
					.obtainMessage(MESSAGE_NOTIFY_LIST, bitmap);
		}

	}

	/**
	 * 图片加载异步任务（从网上下载）
	 * 
	 * @author user
	 * 
	 */
	class GridImageLoadThread extends Thread {
		private String url;
		private BaseAdapter adapter;
		private ViewHolder holder;
		private Handler handler;

		public GridImageLoadThread(String url, BaseAdapter adapter,
				ViewHolder holder, Handler handler) {
			this.url = url;
			this.adapter = adapter;
			this.holder = holder;
			this.handler = handler;
		}

		@Override
		public void run() {
			super.run();
			Bitmap bitmap = loadImageFromInternet(url);
			addImage2Cache(url, bitmap);
			Message message = handler
					.obtainMessage(MESSAGE_NOTIFY_GRID, bitmap);
			handler.sendMessage(message);
		}

	}

	class GalleryImageLoadThread extends Thread {
		private String url;
		private BaseAdapter adapter;
		private com.aven.qqdemo.MyGalleryAdapter.ViewHolder holder;
		private Handler handler;

		public GalleryImageLoadThread(String url, MyGalleryAdapter adapter,
				com.aven.qqdemo.MyGalleryAdapter.ViewHolder holder, Handler handler) {
			this.url = url;
			this.adapter = adapter;
			this.holder = holder;
			this.handler = handler;
		}

		

		@Override
		public void run() {
			super.run();
			Bitmap bitmap = loadImageFromInternet(url);
			addImage2Cache(url, bitmap);
			Log.d("url", "url="+url);
			//holder.imageView.setImageBitmap(bitmap);
			Message message = handler.obtainMessage(MESSAGE_NOTIFY_GALLERY,
					bitmap);
			handler.sendMessage(message);
		}
	}

	/*
	 * class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> { String url;
	 * BaseAdapter adapter; ViewHolder holder;
	 * 
	 * @Override protected Bitmap doInBackground(Object... params) { url =
	 * (String) params[0]; adapter = (BaseAdapter) params[1]; holder =
	 * (ViewHolder) params[2]; Bitmap bitmap = loadImageFromInternet(url);
	 * return bitmap; }
	 * 
	 * @Override protected void onPostExecute(Bitmap result) {
	 * super.onPostExecute(result); if(result == null) { return; } //ImageView
	 * imageView = holder.imageView.findViewWithTag(url);
	 * //holder.imageView.setImageBitmap(result); addImage2Cache(url, result);
	 * //放入缓存 adapter.notifyDataSetChanged();
	 * //触发getView方法执行，这个时候getView实际上会拿到刚刚缓存好的图片 } }
	 */

	/**
	 * 从网络上下载图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap loadImageFromInternet(String url) {
		Bitmap bitmap = null;
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpResponse response = null;
		InputStream inputStream = null;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				return bitmap;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					return bitmap = BitmapFactory.decodeStream(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
			((AndroidHttpClient) client).close();
		}
		return bitmap;
	}

	/**
	 * 从缓存中取图片，先从一级缓存中取， 没有再去二级缓存中取
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(url);// 从一级缓存中拿
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getFromSecondLevelCache(url);// 从二级缓存中拿
		return bitmap;
	}

	/**
	 * 从二级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
		if (softReference != null) {
			bitmap = softReference.get();
			if (bitmap == null) {// 由于内存吃紧，软引用已经被gc回收了
				mSecondLevelCache.remove(url);
			}
		}
		return bitmap;
	}

	/**
	 * 从一级缓存中取
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(String url) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(url);
			if (bitmap != null) {// 将最近访问的元素放到链的头部，提高下一次访问该元素的检索速度（LRU算法）
				mFirstLevelCache.remove(url);
				mFirstLevelCache.put(url, bitmap);
			}
		}
		return bitmap;
	}

	/**
	 * 图片加载异步任务（从网上下载）
	 * 
	 * @author user
	 * 
	 */
	/*
	 * class ListImageLoadTask extends AsyncTask<Object, Void, Bitmap> { String
	 * url; BaseAdapter adapter; ViewHolder holder;
	 * 
	 * @Override protected Bitmap doInBackground(Object... params) { url =
	 * (String) params[0]; adapter = (BaseAdapter) params[1]; holder =
	 * (ViewHolder) params[2]; Bitmap bitmap = loadImageFromInternet(url);
	 * return bitmap; }
	 * 
	 * @Override protected void onPostExecute(Bitmap result) {
	 * super.onPostExecute(result); if(result == null) { return; } //ImageView
	 * imageView = holder.imageView.findViewWithTag(url);
	 * //holder.imageView.setImageBitmap(result); addImage2Cache(url, result);
	 * //放入缓存 adapter.notifyDataSetChanged();
	 * //触发getView方法执行，这个时候getView实际上会拿到刚刚缓存好的图片 } }
	 */
	interface downloadListener {
		public void downloda();
	}
}
