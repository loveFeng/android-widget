package com.example.myapp_lizi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class MyRenderer extends GLSurfaceView implements Renderer {

    private float slowdown = 20.f; // Slow Down Particles  
    private float zoom = -40.0f; // Used To Zoom Out  
    private int[] texture = new int[1];// Storage For Our Particle Texture  
    private Random random = new Random(); // to generate a int value randomly  
    private FloatBuffer vertexBuffer; // to hold the particle's vertices  
    private FloatBuffer textureBuffer; // to hold the particle's texture  
                                        // vertices  
    private float[] textureCoordinate = { 0.0f, 0.f, 1.0f, 0.0f, 0.0f, 1.0f,  
            1.0f, 1.0f }; // the particle's texture coordinate  
    private Particle[] particles = new Particle[1000];// to hold the particle  
                                                        // object array  
    private Context context;// the activity context  
  
    /** 
     * the method to generate a FloatBuffer reference with pass a float array 
     * */  
    private FloatBuffer formatFloatBuffer(float[] array) {  
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);  
        bb.order(ByteOrder.nativeOrder());  
        FloatBuffer fb;  
        fb = bb.asFloatBuffer();  
        fb.put(array);  
        fb.position(0);  
        return fb;  
  
    }  
  
    // the particle color array  
    private float colors[][] = { { 1.0f, 0.5f, 0.5f }, { 1.0f, 0.75f, 0.5f },  
            { 1.0f, 1.0f, 0.5f }, { 0.75f, 1.0f, 0.5f }, { 0.5f, 1.0f, 0.5f },  
            { 0.5f, 1.0f, 0.75f }, { 0.5f, 1.0f, 1.0f }, { 0.5f, 0.75f, 1.0f },  
            { 0.5f, 0.5f, 1.0f }, { 0.75f, 0.5f, 1.0f }, { 1.0f, 0.5f, 1.0f },  
            { 1.0f, 0.5f, 0.75f } };  
  
    /** 
     * the MyRenderer constructor 
     * */  
    public MyRenderer(Context context) {  
        super(context);  
        this.setRenderer(this);  
        this.context = context;  
    }  
  
    /** 
     * the method to initiate the particle objects 
     *  
     * */  
    private void initParticals() {  
        for (int i = 0; i < particles.length; i++) {  
            particles[i] = new Particle();  
            particles[i].active = true;  
            particles[i].life = 1.0f;  
            particles[i].fade = (float) (random.nextInt(10) % 100) / 1000.0f + 0.003f;  
            particles[i].r = colors[random.nextInt(12)][0];  
            particles[i].g = colors[random.nextInt(12)][1];  
            particles[i].b = colors[random.nextInt(12)][2];  
            particles[i].xi = (float) ((random.nextInt(100) % 50) - 26.0f) * 10.0f;  
            particles[i].yi = (float) ((random.nextInt(100) % 50) - 25.0f) * 10.0f;  
            particles[i].zi = (float) ((random.nextInt(100) % 50) - 25.0f) * 10.0f;  
            particles[i].xg = 0.0f;  
            particles[i].yg = -0.8f;  
            particles[i].zg = 0.0f;  
  
        }  
  
    }  
  
    /** 
     * draws the whole particles and change the each one's attribute after draws 
     * it 
     * */  
    private void drawParticle(GL10 gl) {  
        for (int i = 0; i < particles.length; i++) {  
  
            if (particles[i].active) {  
                float temVectex[] = { particles[i].x + 0.5f,  
                        particles[i].y + 0.5f, particles[i].z + zoom,  
                        particles[i].x + 0.5f, particles[i].y - 0.5f,  
                        particles[i].z + zoom, particles[i].x - 0.5f,  
                        particles[i].y + 0.5f, particles[i].z + zoom,  
                        particles[i].x - 0.5f, particles[i].y - 0.5f,  
                        particles[i].z + zoom };  
                gl.glColor4f(particles[i].r, particles[i].g, particles[i].b,  
                        particles[i].life);  
                vertexBuffer = formatFloatBuffer(temVectex);  
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);  
                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, temVectex.length / 3);  
                particles[i].x += particles[i].xi / (slowdown * 10);  
                particles[i].y += particles[i].yi / (slowdown * 10);  
                particles[i].z += particles[i].zi / (slowdown * 10);  
                particles[i].xi += particles[i].xg;  
                particles[i].yi += particles[i].yg;  
                particles[i].zi += particles[i].zg;  
                particles[i].life -= particles[i].fade;  
                if (particles[i].life < 0.0f) {  
                    particles[i].life = 1.0f;  
                    particles[i].fade = (float) (random.nextInt(100) % 100) / 1000.0f + 0.003f;  
                    particles[i].x = 0.0f;  
                    particles[i].y = 0.0f;  
                    particles[i].z = 0.0f;  
                    particles[i].xi = (float) ((random.nextInt(100) % 50) - 26.0f) * 10.0f;  
                    particles[i].yi = (float) ((random.nextInt(100) % 50) - 25.0f) * 10.0f;  
                    particles[i].zi = (float) ((random.nextInt(100) % 50) - 25.0f) * 10.0f;  
                    particles[i].xg = 0.0f;  
                    particles[i].yg = -0.8f;  
                    particles[i].zg = 0.0f;  
                }  
  
            }  
        }  
    }  
  
    public void onDrawFrame(GL10 gl) {  
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);  
        gl.glLoadIdentity();  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  
        textureBuffer = formatFloatBuffer(textureCoordinate);  
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);  
        gl.glTranslatef(0.0f, 0.0f, -5.0f);  
        drawParticle(gl);  
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);  
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  
  
    }  
  
    public void onSurfaceChanged(GL10 gl, int width, int height) {  
        if (height == 0) {  
            height = 1;  
        }  
        gl.glViewport(0, 0, width, height);  
        gl.glMatrixMode(GL10.GL_PROJECTION);  
        gl.glLoadIdentity();  
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,  
                200.0f);  
        gl.glMatrixMode(GL10.GL_MODELVIEW);  
        gl.glLoadIdentity();  
    }  
  
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {  
        gl.glShadeModel(GL10.GL_SMOOTH);  
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  
        gl.glClearDepthf(1.0f);  
        gl.glDisable(GL10.GL_DEPTH_TEST);  
        gl.glEnable(GL10.GL_BLEND);  
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);  
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  
        gl.glEnable(GL10.GL_TEXTURE_2D);  
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);  
        loadTexture(gl, context);  
        initParticals();  
    }  
  
    /** 
     * the method is to load texture for the particle 
     * */  
    public void loadTexture(GL10 gl, Context context) {  
  
        InputStream is = context.getResources()  
                .openRawResource(R.drawable.star);  
        Bitmap bitmap = null;  
        try {  
  
            bitmap = BitmapFactory.decodeStream(is);  
        } finally {  
            try {  
  
                is.close();  
                is = null;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
  
        }  
  
        gl.glGenTextures(1, texture, 0);  
  
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);  
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,  
                GL10.GL_LINEAR);  
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,  
                GL10.GL_LINEAR);  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);  
  
        bitmap.recycle();  
    }  
  
    /** 
     * this is the particle class it has some attribute for each particle object 
     * */  
    class Particle {  
        // We start off with the boolean variable active. If this variable is  
        // TRUE, our particle is alive and kicking. If it's FALSE our particle  
        // is dead or we've turned it off! In this program I don't use active,  
        // but it's handy to include.  
  
        // The variables life and fade control how long the particle is  
        // displayed, and how bright the particle is while it's alive. The  
        // variable life is gradually decreased by the value stored in fade. In  
        // this program that will cause some particles to burn longer than  
        // others.  
  
        private boolean active;  
        private float life;  
        private float fade;  
  
        // The variables r, g and b hold the red intensity, green intensity and  
        // blue intensity of our particle. The closer r is to 1.0f, the more red  
        // the particle will be. Making all 3 variables 1.0f will create a white  
        // particle.  
  
        private float r;  
        private float g;  
        private float b;  
        // The variables x, y and z control where the particle will be displayed  
        // on the screen. x holds the location of our particle on the x axis. y  
        // holds the location of our particle on the y axis, and finally z holds  
        // the location of our particle on the z axis  
  
        private float x, y, z;  
        // The next three variables are important. These three variables control  
        // how fast a particle is moving on specific axis, and what direction to  
        // move. If xi is a negative value our particle will move left. Positive  
        // it will move right. If yi is negative our particle will move down.  
        // Positive it will move up. Finally, if zi is negative the particle  
        // will move into the screen, and postive it will move towards the  
        // viewer.  
        private float xi, yi, zi;  
  
        // Lastly, 3 more variables! Each of these variables can be thought of  
        // as gravity. If xg is a positive value, our particle will pull to the  
        // right. If it's negative our particle will be pulled to the left. So  
        // if our particle is moving left (negative) and we apply a positive  
        // gravity, the speed will eventually slow so much that our particle  
        // will start moving the opposite direction. yg pulls up or down and zg  
        // pulls towards or away from the viewer.  
        private float xg, yg, zg;  
  
    }  


}
