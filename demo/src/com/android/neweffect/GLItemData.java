package com.android.neweffect;

import android.graphics.Bitmap;

public class GLItemData
{
	private String mStrNum;		// Number
	private String mStrDate;	// Date
	private String mStrCont;	// The content of the message
	private Bitmap mBmpSender;	// The image of the sender
	
	public GLItemData()
	{
		mStrNum = null;
		mStrDate = null;
		mStrCont = null;
		mBmpSender = null;
	}
	
	public GLItemData(String StrNum, String StrDate, String StrCont, Bitmap BmpSender)
	{
		mStrNum = StrNum;
		mStrDate = StrDate;
		mStrCont = StrCont;
		mBmpSender = BmpSender;
	}
	
	public String getNum()
	{
		return mStrNum;
	}
	
	public String getDate()
	{
		return mStrDate;
	}
	
	public String getCont()
	{
		return mStrCont;
	}
	
	public Bitmap getBmp()
	{
		return mBmpSender;
	}
	
	public void setNum(String StrNum)
	{
		mStrNum = StrNum;
	}
	
	public void setDate(String StrDate)
	{
		mStrDate = StrDate;
	}
	
	public void setCont(String StrCont)
	{
		mStrCont = StrCont;
	}
	
	public void setBmp(Bitmap BmpSender)
	{
		mBmpSender = BmpSender;
	}
}