package com.rrr.view;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class DialView extends SurfaceView implements Callback,Runnable{
	private SurfaceHolder holder;
	private Thread thread;
	private Paint paint;
	private Canvas canvas;
	private int screenW,screenH;
	private Bitmap bigDialBmp,bigPointerBmp,smallDialBmp,smallPointerBmp,bgBmp,fuSheBmp;
	private boolean flag;
	private int bigDialX,bigDialY,bigPointerX,bigPointerY,smallDialX,smallDialY,
				smallPointerX,smallPointerY,textBgX,textBgY;
	private Rect bgRect;
	private Bitmap textBg;
	public int bigDialDegrees,smallDialDegrees;
	private String percentageText="";
	private int percentageX,percentageY;
	public DialView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder=getHolder();
		holder.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setColor(Color.argb(255, 207, 60, 11));
		paint.setTextSize(22);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	public void myDraw(){
		try {
			canvas=holder.lockCanvas(bgRect);
			canvas.drawColor(Color.WHITE);
			drawBigDial();
			drawSmallDial();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			holder.unlockCanvasAndPost(canvas);
		}
	}
    public void drawBigDial(){
		canvas.drawBitmap(bigDialBmp, bigDialX, bigDialY, paint);
		canvas.save();
		canvas.rotate(bigDialDegrees,bigPointerX+bigPointerBmp.getWidth()/2, bigPointerY+bigPointerBmp.getHeight()/2);
		canvas.drawBitmap(bigPointerBmp,bigPointerX,bigPointerY,paint);
		canvas.restore();
	}
    public void drawSmallDial(){
		canvas.drawBitmap(smallDialBmp, smallDialX, smallDialY, paint);
		canvas.save();
		canvas.rotate(smallDialDegrees,smallPointerX+smallPointerBmp.getWidth()/2, smallPointerY+smallPointerBmp.getHeight()/2);
		canvas.drawBitmap(smallPointerBmp,smallPointerX,smallPointerY,paint);
		canvas.restore();
	}
	public void logic(){
		bigDialDegrees++;
		smallDialDegrees--;
//		percentageText.
	}
	public void run() {
		while(flag){
			long start = System.currentTimeMillis();
	        myDraw();
	        logic();
	        long end = System.currentTimeMillis();
	        try {
	            if (end - start < 50)
	           Thread.sleep(50 - (end - start));
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
		}
	}
	public void surfaceCreated(SurfaceHolder holder) {
		fuSheBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_dj_iv);
		textBg = BitmapFactory.decodeResource(getResources(), R.drawable.black_box);
		bigDialBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_dashboard_01);
		bigPointerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_pointer_01);
		smallDialBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_dashboard_small_bg);
		smallPointerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_pointer_02);
		bgBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_dj_ll_blue);
		screenH=getHeight();
		screenW=getWidth();
		bgRect=new Rect(0, 0,screenW , bgBmp.getHeight());
		bigDialX =20;
		bigDialY =50;
		bigPointerX = 20/2+bigDialBmp.getWidth()/2-bigPointerBmp.getWidth()/2+10;
		bigPointerY = 50;
		
		textBgX = bigDialX+bigDialBmp.getWidth()/2-textBg.getWidth()/2;
		textBgY = bigDialY+bigDialBmp.getHeight()/2-textBg.getHeight()/2-50;
		
		smallDialX = bigDialX+bigDialBmp.getWidth();
		smallDialY = 30;
		smallPointerX = smallDialX+smallDialBmp.getWidth()/2-smallPointerBmp.getWidth()/2-15;
		smallPointerY = 53;
		flag=true;
		thread= new Thread(this);
		thread.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		flag=false;
	}
	public int getBigDialDegrees() {
		return bigDialDegrees;
	}
	public void setBigDialDegrees(int bigDialDegrees) {
		this.bigDialDegrees = bigDialDegrees;
	}
	public int getSmallDialDegrees() {
		return smallDialDegrees;
	}
	public void setSmallDialDegrees(int smallDialDegrees) {
		this.smallDialDegrees = smallDialDegrees;
	}
	public String getPercentageText() {
		return percentageText;
	}
	public void setPercentageText(String percentageText) {
		this.percentageText = percentageText;
	}
	
}
