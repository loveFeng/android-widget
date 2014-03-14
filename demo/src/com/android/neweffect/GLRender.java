package com.android.neweffect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.demo_highlights.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class GLRender implements Renderer
{
	 private static Resources mResources;
	 
	 public float rotateQuad;
	 public float Translate = 0.0f;
	 public float scale = 0.0f;
	 
	 // Unit of Matrix
	 int one = 0x10000;
	 int half = 0x8000;
	 int three = 0x30000;
	 
	 // Matrix buffer
	 private IntBuffer quaterBuffer;
	 private IntBuffer textureBuffer;
	 
	 // Item object
	 private List<GLItemObject> mList = new LinkedList<GLItemObject>();
	 
	 // Temp code
	 private GLItemObjectBase mItem;
	 private GLItemObjectBase mItem1;
	 private GLItemObjectBase mItem2;
	 private GLItemObjectBase mItem3;
	 private GLItemObjectBase mItem4;
	 private GLItemObjectBase mItem5;
	 private GLItemObjectBase mItem6;
	 private GLItemObjectBase mItem7;
	 private GLItemObjectBase mItem8;
	 private GLItemObjectBase mItem9;
	 private GLItemObjectBase mItem10;
	 
	 //Temp code
	 private static final int mItemCount = 11;
	 private static float m_iTransZ = 2.0f;
	 
	 //���廷����(r,g,b,a)
	 private static FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{0.5f,0.5f,0.5f,1.0f}); 
	 //���������
	 private static FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{1.0f,1.0f,1.0f,1.0f});
	 //��Դ��λ��
	 private static FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{0.0f,0.0f,2.0f,10.0f}); 
 
	 int texture = -1;
	 
	 //���ε�4������
	 int verticesMatrix[] = {
			-one,-half,three,
			one,-half,three,
			one,half,three,
			-one,half,three,
			};
	 
	 int textureMatrix[] = {
			 	0,one,one,one,one,0,0,0,	
//				one,0,0,0,0,one,one,one,	
//				0,0,0,one,one,one,one,0,
//				one,one,one,0,0,0,0,one,
//				0,one,one,one,one,0,0,0,
//				0,0,0,one,one,one,one,0,
//				one,0,0,0,0,one,one,one,
			};			
	 
	 ByteBuffer indicesBuffer = ByteBuffer.wrap(new byte[]{
				0,1,3,2,
//				4,5,7,6,
//				8,9,11,10,
//				12,13,15,14,
//				16,17,19,18,
//				20,21,23,22,
		});
	 
	 public GLRender()
	 {
		 ByteBuffer vbb = ByteBuffer.allocateDirect(verticesMatrix.length*4);
	     vbb.order(ByteOrder.nativeOrder());
	     quaterBuffer = vbb.asIntBuffer();
	     quaterBuffer.put(verticesMatrix);
	     quaterBuffer.position(0);
	     
	     ByteBuffer vbbTex = ByteBuffer.allocateDirect(textureMatrix.length*4);
	     vbbTex.order(ByteOrder.nativeOrder());
	     textureBuffer = vbbTex.asIntBuffer();
	     textureBuffer.put(textureMatrix);
	     textureBuffer.position(0);
	 }
	 
	@Override
	public void onDrawFrame(GL10 gl)
	{		
		// �����Ļ����Ȼ���
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	
		//�������GL_LIGHTING���ʲô��������
		gl.glEnable(GL10.GL_LIGHTING);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// draw
		{
			//draw all items
			drawAllItems();
		}
		
	    //ȡ�������
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    
	    //�ı���ת�ĽǶ�
	   // rotateQuad -= 0.5f;*/
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		// TODO Auto-generated method stub
		float ratio = (float) width / height;
		//����OpenGL�����Ĵ�С
		gl.glViewport(0, 0, width, height);
		//����ͶӰ����
		gl.glMatrixMode(GL10.GL_PROJECTION);
		//����ͶӰ����
		gl.glLoadIdentity();
		// �����ӿڵĴ�С
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		// ѡ��ģ�͹۲����
		gl.glMatrixMode(GL10.GL_MODELVIEW);	
		// ����ģ�͹۲����
		gl.glLoadIdentity();	
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// ������Ӱƽ��
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		// ����ɫ
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// ������Ȼ���
		gl.glClearDepthf(1.0f);							
		// ������Ȳ���
		gl.glEnable(GL10.GL_DEPTH_TEST);						
		// ������Ȳ��Ե�����
		gl.glDepthFunc(GL10.GL_LEQUAL);							
		
		// ����ϵͳ��͸�ӽ�������
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		//����2D��ͼ,����
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		// Init the environment
		{
			// set the matrix to Item
			GLItemObjectBase.setBuffer(quaterBuffer, textureBuffer, indicesBuffer);

			// init all items
			initAllItems(gl);
		}	
		
		// �����˲�
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		//���û�����
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient);

	    //���������
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse);

	    //���ù�Դ��λ��
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition);
	    
	    //����һ�Ź�Դ
	    gl.glEnable(GL10.GL_LIGHT1);
	}
	
	public void setResources(Resources resources)
	{
		mResources = resources;
	}
	
	public void setTranslate(float fTrans)
	{
		this.Translate = fTrans;
		mItem.Translate = fTrans;
		mItem1.Translate = fTrans;
		mItem2.Translate = fTrans;
		mItem3.Translate = fTrans;
		mItem4.Translate = fTrans;
		mItem5.Translate = fTrans;
		mItem6.Translate = fTrans;
		mItem7.Translate = fTrans;
		mItem8.Translate = fTrans;
		mItem9.Translate = fTrans;
		mItem10.Translate = fTrans;
		
//		Iterator<GLItemObject> it = mList.iterator();
//		while(it.hasNext())
//		{
//			it.next().Translate = fTrans;
//		}
//		for (int i=0; i<mItemCount; i++)
//		{
//			mList.get(i).Translate = fTrans;
//		}
	}
	
	public void initAllItems(GL10 gl)
	{
		// load item_bg
		GLImage.load(mResources, R.drawable.item_effect_bg);
		
//		for (int i=0; i<mItemCount; i++)
//		{
//			GLItemObject Item = new GLItemObject(gl, "\nFrom: 13XXXXXXXXXX\nDate: 2010/1/1\nHello, how are you?",
//				mResources, R.drawable.item_bg);
//			mList.add(i, Item);
//		}
		
		// Temp code
		GLItemData data = new GLItemData();
		
		data.setBmp(null);
		data.setNum("13000000000");
		data.setDate("2010/1/1");
		data.setCont("Hello, how are you?");
		mItem = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("1311111111");
		data.setDate("2010/2/1");
		data.setCont("2Hello, how are you?");
		mItem1 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("132222");
		data.setDate("2010/3/1");
		data.setCont("3Hello, how are you?");
		mItem2 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("1333333");
		data.setDate("2010/4/1");
		data.setCont("4Hello, how are you?");
		mItem3 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("13444444");
		data.setDate("2010/5/1");
		data.setCont("5Hello, how are you?");
		mItem4 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("13555555");
		data.setDate("2010/6/1");
		data.setCont("6Hello, how are you?");
		mItem5 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("136666666");
		data.setDate("2010/7/1");
		data.setCont("7Hello, how are you?");
		mItem6 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("137777777");
		data.setDate("2010/8/1");
		data.setCont("8Hello, how are you?");
		mItem7 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		data.setBmp(null);
		data.setNum("13888888888");
		data.setDate("2010/9/1");
		data.setCont("9Hello, how are you?");
		mItem8 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		
		
		data.setBmp(((BitmapDrawable)mResources.getDrawable(R.drawable.item_effect_bg)).getBitmap());
		data.setNum("13999999999");
		data.setDate("2010/10/1");
		data.setCont("10Hello, how are you?");
		mItem9 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);
		
		
		data.setBmp(null);
		data.setNum("1310101010");
		data.setDate("2010/11/1");
		data.setCont("11Hello, how are you?");
		mItem10 = new GLItemObject(gl, data, mResources, R.drawable.item_effect_bg);	
	}
	
	public void drawAllItems()
	{
//		Iterator<GLItemObject> it = mList.iterator();
//		while(it.hasNext())
//		{
//			it.next().DrawImage(m_iTransZ);
//			m_iTransZ-=0.2f;
//		}
//		for (int i=0; i<mItemCount; i++)
//		{
//			mList.get(i).DrawImage(m_iTransZ);
//			m_iTransZ=m_iTransZ-(0.4f);
//		}
		
		// Temp code
		mItem.DrawImage(2.0f);
		mItem1.DrawImage(1.6f);
		mItem2.DrawImage(1.2f);
		mItem3.DrawImage(0.8f);
		mItem4.DrawImage(0.4f);
		mItem5.DrawImage(0.0f);
		mItem6.DrawImage(-0.4f);
		mItem7.DrawImage(-0.8f);
		mItem8.DrawImage(-1.2f);
		mItem9.DrawImage(-1.6f);
		mItem10.DrawImage(-2.0f);
	}
}