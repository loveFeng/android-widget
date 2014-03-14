package com.li.light;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

//import com.iadpush.adp.ServiceManager;
//import com.kuguo.ad.KuguoAdsManager;

public class ShanGuangDActivity extends Activity {
	private Button onebutton = null;
	private Camera camera = null;
	private Parameters parameters = null;
	public static boolean kaiguan = true; // ���忪��״̬��״̬Ϊfalse����״̬��״̬Ϊtrue���ر�״̬
	// public static boolean action = false;
	// //�����״̬��״̬Ϊfalse����ǰ���治�˳���״̬Ϊtrue����ǰ�����˳�
	private int back = 0;// �жϰ�����back

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȫ�����ã����ش�������װ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ������Ļ��ʾ�ޱ��⣬����������Ҫ���úã��������ٴα�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.main_shoudiantong);
		
        //add virtual menu
        try {  
            getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));  
        }catch (NoSuchFieldException e) {  
            // Ignore since this field won't exist in most versions of Android  
        }catch (IllegalAccessException e) {  
            Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);  
        }  
      //add end virtual menu
		// ���ͽӿ� false��ʾ������ʱ��������
//		KuguoAdsManager.getInstance().receivePushMessage(ShanGuangDActivity.this,false);
		/**
		 * ���뾫Ʒ�Ƽ��Ŀ�����1��ʾ��ʾ����
		 */
//		KuguoAdsManager.getInstance().showKuguoSprite(this,
//				KuguoAdsManager.STYLE_KUZAI);
		// false��ʾ�����Ҳ�
//		KuguoAdsManager.getInstance().setKuzaiPosition(false, 100);
		//=====================����iAdPush=======================
//        ServiceManager manager = new ServiceManager(this);
        
        //����Debugģʽ����ģʽ��ÿ2���ӻ��յ�һ�ι�棬
        //��ģʽ�����й�涼ֻ������ʹ�ã���������ʵͳ�ơ�
        //����ʽ����ʱ�ǵ�һ��Ҫɾ�����д���
//        manager.setDebugMode();
        
//        manager.startService();
		onebutton = (Button) findViewById(R.id.onebutton);
		onebutton.setOnClickListener(new Mybutton());
	}

	class Mybutton implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (kaiguan) {
				onebutton.setBackgroundResource(R.drawable.shoudiantong_bg1);
				camera = Camera.open();
				parameters = camera.getParameters();
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// ����
				camera.setParameters(parameters);
				camera.startPreview(); 
				// onebutton.setText("�ر�");
				kaiguan = false;
			} else {
				onebutton.setBackgroundResource(R.drawable.shoudiantong_bg);
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// �ر�
				camera.setParameters(parameters);
				camera.stopPreview();
				// onebutton.setText("����");
				kaiguan = true;
				camera.release();
			}
		}
	}
	@Override
	protected void onDestroy() {
		// ֹͣ���ͽӿ�
//		KuguoAdsManager.getInstance().stopPushMessage(ShanGuangDActivity.this);
		// ���սӿڣ��˳����м����տ�����Դ
//		KuguoAdsManager.getInstance().recycle(this);
		super.onDestroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 2, 2, "quit");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 2:
			Myback();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back++;
			switch (back) {
			case 1:
				Toast.makeText(ShanGuangDActivity.this, "try again to quit!",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				back = 0;// ��ʼ��backֵ
				Myback();
				break;
			}
			return true;// ���ó�false��backʧЧ ��true��ʾ ��ʧЧ
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void Myback() { // �رճ���
		if (kaiguan) {// ���عر�ʱ
			ShanGuangDActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// �رս��
		} else if (!kaiguan) {// ���ش�ʱ
			camera.release();
			ShanGuangDActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// �رս��
			kaiguan = true;// ���⣬�򿪿��غ��˳������ٴν��벻�򿪿���ֱ���˳�ʱ���������
		}
	}
}