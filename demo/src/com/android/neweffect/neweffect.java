package com.android.neweffect;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

public class neweffect extends Activity 
{
    /** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        GLSurfaceView glView = new mGLSurfaceView(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(glView);
    }
}