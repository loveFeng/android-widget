package com.example.test1;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo_highlights.R;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity  {
	private int window_width, window_height;// �ؼ����
	private DragImageView dragImageView;// �Զ���ؼ�
	private int state_height;// ״̬���ĸ߶�

	private ViewTreeObserver viewTreeObserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_gesturelistener);
		/** ��ȡ��Ҋ����߶� **/
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();

		dragImageView = (DragImageView) findViewById(R.id.imageid);
		Bitmap bmp = BitmapUtil.ReadBitmapById(this, R.drawable.image01,
				window_width, window_height);
		// ����ͼƬ
		dragImageView.setImageBitmap(bmp);
		dragImageView.setmActivity(this);//ע��Activity.
		/** ����״̬���߶� **/
		viewTreeObserver = dragImageView.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// ��ȡ״�����߶�
							Rect frame = new Rect();
							getWindow().getDecorView()
									.getWindowVisibleDisplayFrame(frame);
							state_height = frame.top;
							dragImageView.setScreen_H(window_height-state_height);
							dragImageView.setScreen_W(window_width);
						}

					}
				});

	}

	/**
	 * ��ȡ������Դ��ͼƬ
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}


}
