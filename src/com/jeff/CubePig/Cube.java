package com.jeff.CubePig;

import android.util.Log;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.IOException;

/* Cube class that simply draws a textured cube */
public class Cube {
	private float verticies[] = {
              /* Vertices according to faces */
              //front
			    		-1.0f, -1.0f, 1.0f, //Vertex 0
			    		 1.0f, -1.0f, 1.0f, //v1
			    		-1.0f,  1.0f, 1.0f, //v2
			    		 1.0f,  1.0f, 1.0f, //v3
			    		//right	
			    		 1.0f, -1.0f,  1.0f,	//front bottom right
			    		 1.0f, -1.0f, -1.0f, //back bottom right	
			    		 1.0f,  1.0f,  1.0f, //front top right
			    		 1.0f,  1.0f, -1.0f, //back top right
			    		//back	
			    		 1.0f, -1.0f, -1.0f, //back bottom right
			    		-1.0f, -1.0f, -1.0f, //back bottom left	
			    		 1.0f,  1.0f, -1.0f, //back top right
			    		-1.0f,  1.0f, -1.0f, //back top left
			    		//left	
			    		-1.0f, -1.0f, -1.0f, //back bottom left
			    		-1.0f, -1.0f, 	1.0f, //front bottom left	
			    		-1.0f,  1.0f, -1.0f, //back top left
			    		-1.0f,  1.0f,  1.0f, //front top left
			    		//bottom	
			    		-1.0f, -1.0f, -1.0f, //back bottom left corner
			    		 1.0f, -1.0f, -1.0f, //back bottom right corner	
			    		-1.0f, -1.0f,  1.0f, //front bottom left corner
			    		 1.0f, -1.0f,  1.0f, //front bottom right corner
			    		//top	
			    		-1.0f, 1.0f,  1.0f, //front top left corner
			    		 1.0f, 1.0f,  1.0f, //front top right corner	
			    		-1.0f, 1.0f, -1.0f, //back top left corner
			    		 1.0f, 1.0f, -1.0f, //back top right corner
	};
	/* Corresponds to vertex array */
	private float tex[] = {
						//front
						0.0f, 0.5f,
			    		0.5f, 0.5f,
			    		0.0f, 0.0f,
			    		0.5f, 0.0f, 
			    		//right	
			    		0.0f, 1.0f,
			    		0.5f, 1.0f,
			    		0.0f, 0.5f,
			    		0.5f, 0.5f,
			    		//back	
			    		0.5f, 0.5f,
			    		1.0f, 0.5f,
			    		0.5f, 0.0f,
			    		1.0f, 0.0f,
			    		//left	
			    		0.0f, 1.0f,
			    		0.5f, 1.0f,
			    		0.0f, 0.5f,
			    		0.5f, 0.5f,
						//bottom
			    		0.0f, 1.0f,
			    		0.5f, 1.0f,
			    		0.0f, 0.5f,
			    		0.5f, 0.5f,
						//top
			    		0.0f, 1.0f,
			    		0.5f, 1.0f,
			    		0.0f, 0.5f,
			    		0.5f, 0.5f,
					};
	private int[] texPointer = new int[1];
	
	/* Remember to draw triangles in ccw order or face the wrath of the cull */
	 private short[] indicies = {
    					//Faces definition
			    		0,1,3, 0,3,2, 			//Face front
			    		4,5,7, 4,7,6, 			//Face right
			    		8,9,11, 8,11,10, 		//... 
			    		12,13,15, 12,15,14, 	
			    		16,17,19, 16,19,18, 	
			    		20,21,23, 20,23,22, 	
    										};

	/* Buffers */
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer colorBuffer;
	private FloatBuffer texBuffer;

	public Cube() {
		
		/* float = 4 bytes */
		ByteBuffer vbb = ByteBuffer.allocateDirect(verticies.length * 4);	
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(verticies);
		vertexBuffer.position(0);

		/* short = 2 bytes */
		ByteBuffer ibb = ByteBuffer.allocateDirect(indicies.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indicies);
		indexBuffer.position(0);

		/* textures */
		ByteBuffer tbb = ByteBuffer.allocateDirect(tex.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(tex);
		texBuffer.position(0);

	}
	public void loadGLTexture(GL10 gl, Context con) {
		Log.d("TEXTURE","LOADING");
		
		Bitmap bm = null;
		
		//totally rad load it yourself way (requires image in  assets folder)
		try {
			AssetManager am = con.getAssets();
			InputStream is = am.open("pigblue.png");
			bm = BitmapFactory.decodeStream(is);
		}catch (IOException e) {
			e.printStackTrace();	
		}
		
		//boring recommended way (requires image in drawables folder)
		/*
		InputStream is = con.getResources().openRawResource(R.drawable.pigblue);
		try {
			bm = BitmapFactory.decodeStream(is);
		}finally {
			try {
				is.close();
				is = null;
			}catch(IOException e) {
			}
		}*/
		
		gl.glGenTextures(1,texPointer,0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texPointer[0]);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);

		/* Deals with what happens if you give a texture coordinate that's outside of the picture (like 2,2) */
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_S,GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D,0,bm,0);	

		bm.recycle();
		
	}
	/* Draw cube to screen */
	public void draw(GL10 gl) {
		/* Textures */
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texPointer[0]);

		/* Verticies */
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBuffer);//coordinates per vertex, type, offset between coordinates,pointer to first coordinate
		gl.glTexCoordPointer(2,GL10.GL_FLOAT,0,texBuffer);
		
		/* Enable */
		gl.glFrontFace(GL10.GL_CCW);//wind counter clockwise
		gl.glEnable(GL10.GL_CULL_FACE);//face culling for performance
		gl.glCullFace(GL10.GL_BACK);
				
		/* Draw	*/
		gl.glDrawElements(GL10.GL_TRIANGLES, indicies.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		/* Disable */
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);

	}
	
}
