package com.hackbulgaria.androidcourse.week1.gestureimage;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class SplashActivity extends Activity implements OnTouchListener {

	private View root;
	private ImageView image;

	private PointF finger1;
	private PointF finger2;

	private float prevPanX;
	private float prevPanY;
	private float currPanX;
	private float currPanY;
	private float prevRotation;
	private float currRotation;
	private float prevScale;
	private float currScale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		root = findViewById(R.id.root);
		root.setOnTouchListener(this);
		image = (ImageView) findViewById(R.id.image);

		finger1 = new PointF();
		finger2 = new PointF();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		update(event);

		int pointerCount = event.getPointerCount();
		int action = event.getAction() & MotionEvent.ACTION_MASK;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			if (pointerCount == 2) {
				prevPanX = getCenter(finger1.x, finger2.x);
				prevPanY = getCenter(finger1.y, finger2.y);
				prevRotation = getAngle(finger1, finger2);
				prevScale = getDistance(finger1, finger2);
			} else if (pointerCount == 1) {
				resetPan();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
			if (event.getPointerCount() == 2) {
				resetPan();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (pointerCount == 2) {
				currPanX = getCenter(finger1.x, finger2.x);
				currPanY = getCenter(finger1.y, finger2.y);
				currRotation = getAngle(finger1, finger2);
				currScale = getDistance(finger1, finger2);

				float deltaRotation = currRotation - prevRotation;
				float deltaScale = currScale / prevScale;

				image.setRotation(image.getRotation() + deltaRotation);
				image.setScaleX(image.getScaleX() * deltaScale);
				image.setScaleY(image.getScaleY() * deltaScale);

				prevRotation = currRotation;
				prevScale = currScale;
			} else if (pointerCount == 1) {
				currPanX = finger1.x;
				currPanY = finger1.y;
			}

			float deltaPanX = currPanX - prevPanX;
			float deltaPanY = currPanY - prevPanY;

			image.setTranslationX(image.getTranslationX() + deltaPanX);
			image.setTranslationY(image.getTranslationY() + deltaPanY);

			prevPanX = currPanX;
			prevPanY = currPanY;

			break;
		}
		return true;
	}

	private void update(MotionEvent event) {
		for (int pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++) {
			int pointerId = event.getPointerId(pointerIndex);
			if (pointerId == 0) {
				finger1.x = event.getX(pointerIndex);
				finger1.y = event.getY(pointerIndex);
			}
			if (pointerId == 1) {
				finger2.x = event.getX(pointerIndex);
				finger2.y = event.getY(pointerIndex);
			}
		}
	}

	private void resetPan() {
		prevPanX = finger1.x;
		prevPanY = finger1.y;
	}

	private float getCenter(float x1, float x2) {
		return (x1 + x2) / 2;
	}

	private float getAngle(PointF p1, PointF p2) {
		float xDiff = p2.x - p1.x;
		float yDiff = p2.y - p1.y;
		return (float) Math.toDegrees(Math.atan2(yDiff, xDiff));
	}

	private float getDistance(PointF p1, PointF p2) {
		float xDiff = p2.x - p1.x;
		float yDiff = p2.y - p1.y;
		return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}
}