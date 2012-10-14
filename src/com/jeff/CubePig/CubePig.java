package com.jeff.CubePig;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.util.Log;

/* This is the main class. It instantiates a GLRenderer which builds a Cube object. */

public class CubePig extends Activity {
  GLRenderer glr;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glr = new GLRenderer(this);
		setContentView(glr);
	}

  @Override
	public boolean onTouchEvent(MotionEvent e) {

		/* Update finger position so GLRender knows where your finger is */
		glr.tx = e.getX();
		glr.ty = e.getY();

    /* User interaction has occured so reset timer */
		glr.scared = false;
		glr.cdt.cancel();
		glr.cdt.start();
	
		return super.onTouchEvent(e);
	}
}
