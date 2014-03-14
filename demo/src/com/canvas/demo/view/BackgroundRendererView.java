package com.canvas.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import com.canvas.demo.AnimationConfig;
import com.canvas.demo.surface.RendererView;
import com.canvas.demo.util.CanvasUtil;
import com.example.demo_highlights.R;

public class BackgroundRendererView extends RendererView{

	private int[] ZOOM_ANIMATION  = {0,10,20,30,40,50,60,70,80,90,100,94,88,94,100};
	private Paint mPaint = new Paint();
	private Bitmap mBitmap = null;
	private int mStep = 0;
	
	public BackgroundRendererView(Context context) {
		super(context);
		
		mPaint.setAntiAlias(true);
		mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.round);
		
	}

	public void startRenderer() {
		mStep = 0;
		super.startRenderer();
	}

	@Override
	public void stopRenderer() {
		super.stopRenderer();
	}

	@Override
	protected void onRenderer(Canvas canvas) {
		
		int startColor = 0xEE * mStep / ZOOM_ANIMATION.length;
		startColor = startColor << 24;
		
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] { startColor, 0x00000000});
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setBounds(new Rect(0, 0, (int)(AnimationConfig.DEVICE_WIDTH / 2), (int)(AnimationConfig.DEVICE_HEIGHT)));
		drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		drawable.draw(canvas);
		
		if(mStep < ZOOM_ANIMATION.length)
		{
			CanvasUtil.drawImage(canvas, mBitmap, new PointF(-20, AnimationConfig.DEVICE_HEIGHT / 2.0f), ZOOM_ANIMATION[mStep] / 100.0f, mPaint);
		}
		
		if(this.doingRenderer())
		{
			if((mStep + 1) >= ZOOM_ANIMATION.length)
			{
				this.stopRenderer();
			}else{
				mStep ++ ;
			}
		}
		
	}
	
	

}
