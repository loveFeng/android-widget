package com.example.myapp_lizi;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.app.Activity;

public class LiZiMainActivity extends Activity {

    /** The OpenGL View */  
    private MyRenderer glSurface;  
  
    /** 
     * Initiate the OpenGL View and set our own Renderer 
     */  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        glSurface = new MyRenderer(this);  
        setContentView(glSurface);  
    }  
  
    /** 
     * Remember to resume the glSurface 
     */  
    @Override  
    protected void onResume() {  
        super.onResume();  
        glSurface.onResume();  
    }  
  
    /** 
     * Also pause the glSurface 
     */  
    @Override  
    protected void onPause() {  
        super.onPause();  
        glSurface.onPause();  
    }  
  
}
