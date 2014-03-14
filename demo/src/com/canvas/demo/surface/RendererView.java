package com.canvas.demo.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;

public abstract class RendererView extends View implements Callback {
	
	private Handler mHandler = null;
	private boolean mSatrtRenderer = false;
	private IRendererInterface mIRendererInterface = null;
	public RendererView(Context context) {
		super(context);
		this.setBackgroundColor(0x00000000);
		
		mHandler = new Handler(this);
	}
	
	

	public void setRendererInterface(IRendererInterface rendererInterface) {
		this.mIRendererInterface = rendererInterface;
	}

	public boolean handleMessage(Message msg) {
		invalidate();
		if(mSatrtRenderer)
		{
			mHandler.sendEmptyMessageDelayed(0, 10);
		}
		return true;
	}
	

	protected void onDraw(Canvas canvas) {
	    int count = canvas.save();
		this.onRenderer(canvas);
		canvas.restoreToCount(count);
		super.onDraw(canvas);
	}
	
	public boolean doingRenderer()
	{
		return mSatrtRenderer;
	}

	public void stopRenderer()
	{
		mSatrtRenderer = false;
		if(mIRendererInterface != null)
		{
			mIRendererInterface.doRendererFinished(this,0);
		}
	}
	
	public void startRenderer()
	{
		mSatrtRenderer =true;
		mHandler.sendEmptyMessageDelayed(0, 100);
	}
	
	protected abstract void onRenderer(Canvas canvas);
}
