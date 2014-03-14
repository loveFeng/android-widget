package com.android.neweffect;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class mGLSurfaceView extends GLSurfaceView
{
	private GLRender mRender;
//	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final float TRACKBALL_SCALE_FACTOR = 18.0f;
    
    private float mPreviousX;
    private float mPreviousY;
    private float mDownY = 0.0f;
    private float mUpY = 0.0f;
	public mGLSurfaceView(Context context) 
	{
		super(context);
				
		// We want an 8888 pixel format because that's required for
        // a translucent window.
        // And we want a depth buffer.
		mRender = new GLRender();
		mRender.setResources(this.getResources());
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setRenderer(mRender);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	public mGLSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean onTrackballEvent(MotionEvent e) 
	{
        mRender.rotateQuad += e.getY() * TRACKBALL_SCALE_FACTOR;
        requestRender();
        return true;       
    }

    public boolean onTouchEvent(MotionEvent e) 
    {      
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
//            float dx = x - mPreviousX;
//            float dy = y - mPreviousY; 
            //mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
            float Translate = mUpY -(y-mDownY)*1.6f/240.0f;
            mRender.setTranslate(Translate);
            //mRenderer.scale += -0.5*mRenderer.Translate +1.0f; //* TOUCH_SCALE_FACTOR;
            //Log.i("mAngleX", "mAngleX = "+ mRenderer.mAngleX );
            //Log.i("mAngleY", "mAngleY = "+ mRenderer.rotateQuad );
            requestRender();
            break;
        case MotionEvent.ACTION_DOWN:
    		mDownY = y;
            requestRender();
            break;
        case MotionEvent.ACTION_UP:
        	mUpY = mRender.Translate;
            requestRender();        
            break;
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}
