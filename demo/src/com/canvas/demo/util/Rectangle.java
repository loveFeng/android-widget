package com.canvas.demo.util;

import android.graphics.PointF;
import android.graphics.RectF;

public class Rectangle {
    public PointF LeftTop     = new PointF();
    public PointF LeftButtom  = new PointF();
    public PointF RightTop    = new PointF();
    public PointF RightButtom = new PointF();
    
    public Rectangle()
    {
    	
    }
    
    public Rectangle(RectF rect)
    {
    	LeftButtom.x = rect.left;
		LeftButtom.y = rect.bottom;
		
		LeftTop.x = rect.left;
		LeftTop.y = rect.top;
		
		RightButtom.x = rect.right;
		RightButtom.y = rect.bottom;
		
		RightTop.x = rect.right;
		RightTop.y = rect.top;
    }
}
