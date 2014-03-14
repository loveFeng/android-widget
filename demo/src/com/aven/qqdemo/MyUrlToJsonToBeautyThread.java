package com.aven.qqdemo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ������߳��࣬������url��ַ��JSON��ݽ������������ͨ�����Handler������Ϣ��������
 * ���˼·���Ǹ�ݴ�������url���Ȼ��JSON��ݣ��ٽ���JSON��ݣ�ȡ��imageUrl��name�����ֶ�
 * ������Beautyʵ������
 * @author user
 *
 */
public class MyUrlToJsonToBeautyThread extends Thread {
	//���ﶨ�������ֶΣ���ʹ��Handler������Ϣ������ߵ�ʱ�򣬲��������˽��������ݣ���������ֶ�
	//������
	public static final int MESSAGE_GRID_JSON = 0;
	public static final int MESSAGE_LIST_JSON = 1;
	public static final int MESSAGE_GALLERY_JSON = 2;
	private Handler handler;
	private String url;
	//signNum������ʾ���ĸ����������̣߳�Ȼ��������߳�ͨ�������Handler������Ϣ��ʱ��
	//�ڵ������Ǳ߿��Ը�õķֱ����
	private int signNum;
	private ArrayList<Beauty> data = null;

	public MyUrlToJsonToBeautyThread(Handler handler, String url, int signNum) {
		this.handler = handler;
		this.url = url;
		this.signNum = signNum;
	}

	@Override
	public void run() {
		data = getBeauties(url);
		Log.d("Beauty", "data="+data);
		Message message = null;
		Log.d("jiaoshou", data.toString());
		if (signNum == TestFragment.GRID_THREAD) {
			message = handler.obtainMessage(MESSAGE_GRID_JSON, data);
		} else if (signNum == TestFragment.LIST_THREAD) {
			message = handler.obtainMessage(MESSAGE_LIST_JSON, data);
		} else if (signNum == TestFragment.GALLERY_THREAD) {
			message = handler.obtainMessage(MESSAGE_GALLERY_JSON, data);
		}

		handler.sendMessage(message);
	}

	private ArrayList<Beauty> getBeauties(String url) {
		ArrayList<Beauty> parseJson = null;
		String json = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (200 == response.getStatusLine().getStatusCode()) {
				//�õ�JSON���
				json = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			parseJson = new ArrayList<Beauty>();
			//ͨ��String���͵�json�õ�JSONObject����
			if (json == null) {
				return parseJson;
			}
			JSONObject jsObject = new JSONObject(json);
			//��ͨ��JSONObject����õ�JSONArray����
			JSONArray jsArray = jsObject.getJSONArray("groups");
			//����JSONArray����JSONArray����������һϵ�е�JSONObject��ͨ��get***����ȡ��
			//��Ҫ��ֵ,������Beautyʵ�����У�����ٽ�ʵ���ౣ���ڼ�����
			for (int i = 0; i < jsArray.length(); i++) {
				String imageUrl = jsArray.getJSONObject(i).getString("cover");
				String name = jsArray.getJSONObject(i).getString("name");
				Beauty beauty = new Beauty(imageUrl, name);
				parseJson.add(beauty);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return parseJson;
	}

}
