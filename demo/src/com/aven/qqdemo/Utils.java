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
	private static final int MAX_CAPACITY = 10; // һ����������ռ�
	private static final long DELAY_BEFORE_PURGE = 10 * 1000;// ��ʱ������
	public static final int MESSAGE_NOTIFY_GRID = 7;
	public static final int MESSAGE_NOTIFY_LIST = 8;
	public static final int MESSAGE_NOTIFY_GALLERY = 9;

	// 0.75�Ǽ�������Ϊ����ֵ��true���ʾ��������������ĸߵ�����false���ʾ���ղ���˳������
	private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
			MAX_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > MAX_CAPACITY) {// ������һ��������ֵ��ʱ�򣬽��ϵ�ֵ��һ������ᵽ��������
				mSecondLevelCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			return false;
		};
	};

	/**
	 * �������棬���õ��������ã�ֻ�����ڴ�Խ���ʱ�������òŻᱻ����
	 */
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			MAX_CAPACITY / 2);

	/**
	 * ��ʱ������
	 */
	private Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clear();
		}
	};

	/**
	 * ������
	 */
	private void clear() {
		mFirstLevelCache.clear();
		mSecondLevelCache.clear();
	}

	private Handler mPurgeHandler = new Handler();

	/**
	 * ���û��������timer
	 */
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * ����ͼƬ������������о�ֱ�Ӵӻ������ã�������û�о�����
	 * 
	 * @param url
	 * @param myAdapter
	 * @param holder
	 */
	public void loadGridImage(String url, BaseAdapter adapter,
			ViewHolder holder, Handler handler) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// �ӻ����ж�ȡ
		if (bitmap == null) {
			holder.imageView.setImageResource(R.drawable.title_bar);// ����û����ΪĬ��ͼƬ
			GridImageLoadThread gridImageLoadThread = new GridImageLoadThread(
					url, adapter, holder, handler);
			gridImageLoadThread.start();
		} else {
			holder.imageView.setImageBitmap(bitmap);// ��Ϊ����ͼƬ
		}

	}

	public void loadListImage(String url, ListAdapter adapter,
			com.aven.qqdemo.ListAdapter.ViewHolder holder, Handler handler) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// �ӻ����ж�ȡ
		if (bitmap == null) {
			holder.imageView.setImageResource(R.drawable.title_bar);// ����û����ΪĬ��ͼƬ
			ListImageLoadThread listImageLoadThread = new ListImageLoadThread(
					url, adapter, holder, handler);
			listImageLoadThread.start();
		} else {
			holder.imageView.setImageBitmap(bitmap);// ��Ϊ����ͼƬ
		}

	}

	public void loadGalleryImage(String url, MyGalleryAdapter adapter,
			com.aven.qqdemo.MyGalleryAdapter.ViewHolder viewHolder,
			Handler handler) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// �ӻ����ж�ȡ
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.title_bar);// ����û����ΪĬ��ͼƬ
			GalleryImageLoadThread galleryImageLoadThread = new GalleryImageLoadThread(
					url, adapter, viewHolder, handler);
			galleryImageLoadThread.start();
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// ��Ϊ����ͼƬ
		}
		/*//viewHolder.imageView.setImageResource(R.drawable.title_bar);// ����û����ΪĬ��ͼƬ
		GalleryImageLoadThread galleryImageLoadThread = new GalleryImageLoadThread(
				url, adapter, viewHolder, handler);
		galleryImageLoadThread.start();*/
	}
	
	
	/**
	 * ���뻺��
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
	 * ͼƬ�����첽���񣨴��������أ�
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
	 * //���뻺�� adapter.notifyDataSetChanged();
	 * //����getView����ִ�У����ʱ��getViewʵ���ϻ��õ��ոջ���õ�ͼƬ } }
	 */

	/**
	 * ������������ͼƬ
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
	 * �ӻ�����ȡͼƬ���ȴ�һ��������ȡ�� û����ȥ����������ȡ
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(url);// ��һ����������
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getFromSecondLevelCache(url);// �Ӷ�����������
		return bitmap;
	}

	/**
	 * �Ӷ�����������
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
		if (softReference != null) {
			bitmap = softReference.get();
			if (bitmap == null) {// �����ڴ�Խ����������Ѿ���gc������
				mSecondLevelCache.remove(url);
			}
		}
		return bitmap;
	}

	/**
	 * ��һ��������ȡ
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(String url) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(url);
			if (bitmap != null) {// ��������ʵ�Ԫ�طŵ�����ͷ���������һ�η��ʸ�Ԫ�صļ����ٶȣ�LRU�㷨��
				mFirstLevelCache.remove(url);
				mFirstLevelCache.put(url, bitmap);
			}
		}
		return bitmap;
	}

	/**
	 * ͼƬ�����첽���񣨴��������أ�
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
	 * //���뻺�� adapter.notifyDataSetChanged();
	 * //����getView����ִ�У����ʱ��getViewʵ���ϻ��õ��ոջ���õ�ͼƬ } }
	 */
	interface downloadListener {
		public void downloda();
	}
}
