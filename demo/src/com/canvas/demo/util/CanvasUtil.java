package com.canvas.demo.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class CanvasUtil {
	//FOR DREW IMAGE
    public static int TOP = 1;
    public static int BOTTOM = 2;
    public static int VCENTER = 4;

    public static int LEFT = 8;
    public static int RIGHT = 16;
    public static int HCENTER = 32;
    
    
    public static void drawImage(Canvas canvas, Bitmap image, int x, int y, int situation,Paint paint)
    {
        int rect_x = 0;
        int rect_y = 0;
        int xx = situation & 56;
        int yy = situation & 7;
        if (image == null)
        {
            return;
        }
        if (xx == LEFT)
        {
            rect_x = x;
        }
        else if (xx == RIGHT)
        {
            rect_x = x - image.getWidth();
        }
        else if (xx == HCENTER)
        {
            rect_x = x - image.getWidth() / 2;
        }

        if (yy == TOP)
        {
            rect_y = y;
        }
        else if (yy == BOTTOM)
        {
            rect_y = y - image.getHeight();
        }
        else if (yy == VCENTER)
        {
            rect_y = y - image.getHeight() / 2;
        }
        canvas.drawBitmap(image, rect_x, rect_y, paint);
    }
    
    public static void drawImage(Canvas canvas, Bitmap image, PointF center,float scale,Paint paint)
    {
    	if(scale <= 0 || image == null) return;

    	float zoomHeight = image.getHeight() * scale;
    	float zoomWidth = image.getWidth() * scale;
    	
    	float left = center.x - zoomWidth / 2.0f;
    	float top  = center.y - zoomHeight / 2.0f;

    	Rect srcRect = new Rect(0, 0, image.getWidth(), image.getHeight());
    	RectF dstRect = new RectF(left, top, left + zoomWidth, top + zoomHeight);
    	
    	canvas.drawBitmap(image, srcRect, dstRect, paint);
    }
    
    public static void setMatrixPolyToPoly(Matrix matrix,Rectangle src,Rectangle des)
    { 
    	float[] srcArgs = new float[] { 
    			src.LeftTop.x, src.LeftTop.y, 
    			src.LeftButtom.x, src.LeftButtom.y,
    			src.RightTop.x, src.RightTop.y,
    			src.RightButtom.x, src.RightButtom.y 
    			};
		float[] dstArgs = new float[] { 
				des.LeftTop.x, des.LeftTop.y, 
				des.LeftButtom.x, des.LeftButtom.y,
				des.RightTop.x, des.RightTop.y,
				des.RightButtom.x, des.RightButtom.y 
    			};
		
		matrix.setPolyToPoly(srcArgs, 0, dstArgs, 0, srcArgs.length >> 1);
    }
}
