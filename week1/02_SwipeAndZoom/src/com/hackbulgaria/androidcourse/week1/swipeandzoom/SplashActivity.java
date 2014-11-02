package com.hackbulgaria.androidcourse.week1.swipeandzoom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private int current = 0;
	private List<Drawable> images = null;
	private ImageView image = null;
	private TextView progress = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		this.images = this.getImages();

		this.image = (ImageView) findViewById(R.id.image);
		this.progress = (TextView) findViewById(R.id.counter);

		this.image.setImageDrawable(images.get(0));

		this.progress.setText(String.format("%d/%d", current + 1, this.images.size()));
		
		GestureDetector.SimpleOnGestureListener gestureListener = new MyGestureListener();
		final GestureDetector gestureDetector = new GestureDetector(this, gestureListener);
		
		image.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
	}

	private List<Drawable> getImages() {
		TypedArray pieces = this.getResources().obtainTypedArray(R.array.pieces);
		List<Drawable> piecesDrawables = new ArrayList<Drawable>();

		for (int i = 0; i < pieces.length(); i++) {
			piecesDrawables.add(pieces.getDrawable(i));
		}

		pieces.recycle();

		return piecesDrawables;
	}

	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private boolean isUpscaled = false;

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (!this.isUpscaled) {
				image.setScaleX(2);
				image.setScaleY(2);
			} else {
				image.setScaleX(1);
				image.setScaleY(1);
			}

			this.isUpscaled = !this.isUpscaled;

			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			float diffX = e2.getX() - e1.getX();

			if (diffX < 0) {
				current++;
			} else if (diffX > 0) {
				current--;
			}

			if (current < 0) {
				current = images.size() - 1;
			}
			current = current % images.size();

			image.setImageDrawable(images.get(current));
			progress.setText(String.format("%d/%d", current + 1, images.size()));

			return true;
		}
	}
}
