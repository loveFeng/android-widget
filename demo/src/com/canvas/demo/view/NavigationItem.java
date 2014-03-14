package com.canvas.demo.view;

import com.canvas.demo.AnimationConfig;
import com.canvas.demo.anim.RendererAnimation;
import com.canvas.demo.util.CanvasUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class NavigationItem {
	
	private Paint mIconPaint = new Paint();
	private Paint mTitlePaint = new Paint();
	private Bitmap mBitmap = null;
	private String mTitle = null;
	public NavigationItem(Bitmap bitmap,String title)
	{
		
		mIconPaint.setAntiAlias(true);
		mTitlePaint.setAntiAlias(true);
		mTitlePaint.setTextSize(AnimationConfig.TITLE_FONT_SIZE);
		mTitlePaint.setColor(Color.WHITE);
		mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
		mBitmap = bitmap;
		mTitle = title;
	}
	
	public int getHeight()
	{
		return mBitmap.getHeight();
	}
	
	public int getWidth()
	{
		return mBitmap.getWidth();
	}
	
	public void drawTitle(Canvas canvas,float vCenter,int alpha)
	{
		int count = canvas.save();
		
		mTitlePaint.setAlpha(alpha);
		
		canvas.translate(AnimationConfig.TITLE_LEFT, 0);
		canvas.drawText(mTitle, AnimationConfig.TITLE_FONT_SIZE / 2.0f, vCenter + AnimationConfig.TITLE_FONT_SIZE / 2.0f, mTitlePaint);
		canvas.restoreToCount(count);
	}
	
	public void draw(Canvas canvas,float scale,int alpha,float radius,float rotate)
	{
	    int count = canvas.save();
	    mIconPaint.setAlpha(alpha);
	    canvas.rotate(rotate - 90);
	    canvas.translate(radius, 0);
	    CanvasUtil.drawImage(canvas, mBitmap, new PointF(0, 0), scale, mIconPaint);
	    canvas.restoreToCount(count);
	}
	
	public void draw(Canvas canvas,int alpha,float rotate,float radius,RectF rect)
	{
	    int count = canvas.save();
	    mIconPaint.setAlpha(alpha);
	    canvas.rotate(rotate - 90);
	    canvas.translate(radius, 0);
	    canvas.drawBitmap(mBitmap, new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight()), rect, mIconPaint);
	    canvas.restoreToCount(count);
	}

}
