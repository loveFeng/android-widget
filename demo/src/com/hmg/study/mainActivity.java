package com.hmg.study;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class mainActivity extends Activity {

	CubeRenderer mCubeRenderer;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题
		GLSurfaceView GLView = new GLSurfaceView(this); //创建一个GLSurfaceView
		
		GLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
//	    mCubeRenderer = new CubeRenderer(this);
		CubeRendererS cubeRendererS = new CubeRendererS(true);
//		GLView.setRenderer(mCubeRenderer);
		GLView.setRenderer(cubeRendererS);
		GLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(GLView);
	}
}