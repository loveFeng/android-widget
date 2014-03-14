package com.amaker.ch07.app;

import com.amaker.ch07.app.IPerson;
import com.example.demo_highlights.R;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @author ���־
 * RPC ����
 */
public class MainActivity extends Activity {
	// ����IPerson�ӿ�
	private IPerson iPerson;
	// ���� Button
	private Button btn;
	private Button btn2;
	// ʵ��ServiceConnection
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		synchronized public void onServiceConnected(ComponentName name, IBinder service) {
			// ���IPerson�ӿ�
			iPerson = IPerson.Stub.asInterface(service);
			if (iPerson != null)
				try {
					// RPC ��������
					iPerson.setName("hz.guo");
					iPerson.setAge(30);
					String msg = iPerson.display();
					Log.d("zhengpanyong", "connection success!");
					// ��ʾ�������÷���ֵ
					Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG)
							.show();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���õ�ǰ��ͼ����
		setContentView(R.layout.main_aidl);
		// ʵ��Button
		btn = (Button) findViewById(R.id.Button01);
		//ΪButton��ӵ����¼�������
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ʵ��Intent
				Intent intent = new Intent();
				// ����Intent Action ����
				intent.setAction("com.amaker.ch07.app.action.MY_REMOTE_SERVICE");
				// �󶨷���
				bindService(intent, conn, Service.BIND_AUTO_CREATE);
			}
		});
		btn2 = (Button) findViewById(R.id.Button02);
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// ����Intent Action ����
				intent.setAction("com.amaker.ch09.app.action.MY_REMOTE_SERVICE");
				// �󶨷���
				unbindService(conn);
			}
		});
	}
}