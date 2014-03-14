package com.android.neweffect;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;

public class GLItemObject extends GLItemObjectBase
{
	private int miImgResID;
	private static Resources mResources;
	
	// Data object
	public GLItemData mData = new GLItemData();
	
	
	
	public GLItemObject(GL10 gl, GLItemData data, Resources resources, int iImgResID)
	{
		super(gl);
		
		mData = data;
		mResources = resources;
		miImgResID = iImgResID;
		
		initTexture();
	}
	
	private Bitmap prepareBmp(Bitmap bmpBG)
	{
		if (bmpBG==null)
			return null;
		
		Bitmap bmpRet = Bitmap.createBitmap(bmpBG.getWidth(), bmpBG.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bmpRet);
		Paint paint = new Paint();
		canvas.drawBitmap(bmpBG, 0, 0, paint);
		
		String strTemp = String.format("From: %s", mData.getNum());
		canvas.drawText(strTemp, 10, 20, paint);
		
		strTemp = String.format("Date: %s", mData.getDate());
		canvas.drawText(strTemp, 10, 36, paint);
		
		strTemp = String.format("Detail: %s", mData.getCont());
		canvas.drawText(strTemp, 10, 52, paint);
		
		Bitmap bmpSender = mData.getBmp();
		if (bmpSender!=null)
		{
			// draw the image of the sender
//			canvas.drawBitmap(GLImage.mBitmap, new Matrix(), paint);
			
		}
		else
		{
			// draw the default image
//			canvas.drawBitmap(GLImage.mBitmap, new Matrix(), paint);
		}
		
		return bmpRet;
	}
	
	private boolean initTexture()
	{
		if (mGL==null || mResources==null || miImgResID<=0)
			return false;
		
		IntBuffer intBuffer = IntBuffer.allocate(/*1*/2);
		// ��������
		mGL.glGenTextures(/*1*/2, intBuffer);
		texture = intBuffer.get();
		// ����Ҫʹ�õ�����
		mGL.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		// load item_bg
//		GLImage.load(mResources, miImgResID);
		if (GLImage.mBitmap==null)
			return false;
		
		// copy the string onto the bg
		Bitmap bmpItem = prepareBmp(GLImage.mBitmap);
		if (bmpItem==null)
			return false;
		
		// �������
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmpItem, 0);
		
//		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap, 0);
		
		return true;
	}
	
}