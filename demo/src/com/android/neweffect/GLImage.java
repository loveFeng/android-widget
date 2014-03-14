package com.android.neweffect;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GLImage
{
	public static Bitmap mBitmap;
	
	public static int iBmpWidth = 0;
	public static int iBmpHeight = 0;
	
	public static boolean load(Resources resources, int iImgResID)
	{
		mBitmap = BitmapFactory.decodeResource(resources, iImgResID);
		if (mBitmap==null)
			return false;
				
		iBmpWidth = mBitmap.getHeight();
		iBmpHeight = mBitmap.getWidth();
		
		return true;
	}	
}