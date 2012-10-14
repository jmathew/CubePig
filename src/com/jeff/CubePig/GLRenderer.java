package com.jeff.CubePig;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.CountDownTimer;

import android.util.Log;

import android.content.Context;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.view.View;
import android.view.WindowManager;
import android.view.Display;
import android.view.MotionEvent;

import android.graphics.Path;


/* Rendering class that renders cube to screen and animates it based on touch. */
public class GLRenderer extends GLSurfaceView implements Renderer {
	private Cube cu;
	private float angleX,angleY,angleZ;
	private float angle;
	public float tx,ty;
	public int sw,sh;
	private float vx,vy; 
	private Context con;

  /* These needs accessor methods */
	public CountDownTimer cdt;
	public boolean scared = false;

	public GLRenderer(Context con) {
		super(con);
		setRenderer(this);
		
		/* init */
		this.con = con;
		cu = new Cube();
		
		vx = vy = 0f;
		tx = ty = 0;
		angle = angleX = angleY = angleZ = 0f;

    /* This timer trips a flag. The idea is that once there is no interaction for a set period of time
       the pig gets "scared" and the texture changes to a "scared" face (which you can see in the 
       texture file) */
		cdt = new CountDownTimer(5000, 1000) {
			public void onTick(long msUntilFinished) {
				//Log.d("TICK","TICK " + msUntilFinished);
			}
			public void onFinish() {
				//Log.d("DONE","TIMING");
				scared= true;
			}
		};
		cdt.start();

		/* Get resolution */
		Display display = ((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		sw = display.getWidth();
		sh = display.getHeight();//pretty sure this disregards some of the screen
		//Log.d("INIT",""+this.sw+" "+this.sh);
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_COLOR_BUFFER_BIT);

		/* identity matrix => matrix with num rows = num cols */
		gl.glLoadIdentity();
		gl.glTranslatef(0,0,-8);

		/* TODO:
       Need 2 sets of animation code here. One is scared! the other is normal.
		   Scared mode has the face rotate to the back face, then rotate to a random
		   one of four points. 
		   Normal mode has it animate to touch point */

		/* Calculate rotation */
		vx += (float)center(this.tx,this.sw) * .05;
		vy += (float)center(this.ty,this.sh) * .05;
		
		/* Friction modifier of sorts */
		vx *= .9;
		vy *= .9;

		//Log.d("TOUCHES",""+vx+ " " + vy);	
		
		gl.glRotatef(vx,0,1,0);
		gl.glRotatef(vy,1,0,0);

		cu.draw(gl);	
	}
	public double center(double x, double total ) {
		return x - total/2;
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		/* Sets the current view port to the new size */
		gl.glViewport(0, 0, w, h);
		/* Select the projection matrix */
		gl.glMatrixMode(GL10.GL_PROJECTION);
		/* Reset the projection matrix */
		gl.glLoadIdentity();
		/* Calculate the aspect ratio of the window */
		GLU.gluPerspective(gl, 45.0f, (float) w / (float) h, 0.1f,100.0f);
		/* Select the modelview matrix */
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		/* Reset the modelview matrix */
		gl.glLoadIdentity();		
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		/* Load texture for cube */
		cu.loadGLTexture(gl,this.con);
    /* Very important, actually enables textures */
		gl.glEnable(GL10.GL_TEXTURE_2D);
		/* Set the background color to black ( rgba ) */
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		/* Enable Smooth Shading, default not really needed */
		gl.glShadeModel(GL10.GL_SMOOTH);
		/* Depth buffer setup */
		gl.glClearDepthf(1.0f);
		/* Enables depth testing */
		gl.glEnable(GL10.GL_DEPTH_TEST);
		/* The type of depth testing to do */
		gl.glDepthFunc(GL10.GL_LEQUAL);
		/* Really nice perspective calculations */
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}
}
