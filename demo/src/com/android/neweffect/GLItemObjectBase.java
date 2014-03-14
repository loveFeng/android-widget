package com.android.neweffect;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLItemObjectBase
{
	public GL10 mGL; 

	public int texture = -1;
	
	// Matrix buffer
	private static IntBuffer quaterBuffer;
	private static IntBuffer textureBuffer;
	private static ByteBuffer indicesBuffer;
	
	public float Translate = 0.0f;
	public float scale = 0.0f;
	
	
	
	public GLItemObjectBase(GL10 gl)
	{
		mGL = gl;
	}
	
	public static void setBuffer(IntBuffer quaterBuf,IntBuffer textureBuf, ByteBuffer indicesBuf)
	{
		quaterBuffer = quaterBuf;
		textureBuffer = textureBuf;
		indicesBuffer = indicesBuf;	
	}
	
	public void DrawImage(float Tran)
	{
//		if (texture<=0)
//			return;
		
		scale = (float) (-0.5*Math.abs(Translate + Tran) +1.0f);
		if(scale <= 0.000001)
		{
			//Translate;
		}
		else
		{
			mGL.glLoadIdentity();
		    
		    // ���� 1.5 ��λ����������Ļ 6.0
			float scalez = 0.1171875f*(Translate + Tran)*(Translate + Tran) + 4.5f;
			mGL.glTranslatef(0.0f,(Translate + Tran), -scalez);
			
		    //������ת
			mGL.glScalef(scale, scale, 1.0f);
		    //gl.glRotatef(rotateQuad-10.0f, 1.0f, 0.0f, 0.0f);
		    	    
		    // ������
			mGL.glBindTexture(GL10.GL_TEXTURE_2D, texture);
			
		    //���ö�������
		    //���úͻ���������
			mGL.glVertexPointer(3, GL10.GL_FIXED, 0, quaterBuffer);
		    //������ı��ζ�Ӧ�Ķ���
			mGL.glTexCoordPointer(2, GL10.GL_FIXED, 0, textureBuffer);
//		    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		    
		    //����
			mGL.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indicesBuffer);
		}

	}
}
