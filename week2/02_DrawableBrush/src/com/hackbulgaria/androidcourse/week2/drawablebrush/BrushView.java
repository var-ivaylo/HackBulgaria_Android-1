package com.hackbulgaria.androidcourse.week2.drawablebrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BrushView extends View {

	private BitmapDrawable brushDrawable;
	private Bitmap backBitmap;
	private Canvas canvas;

	public BrushView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public BrushView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(backBitmap, 0, 0, new Paint());
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		backBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		canvas = new Canvas(backBitmap);
		setOnTouchListener(new MyTouchListener());
	}

	private class MyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final float x = event.getX();
			final float y = event.getY();

			Bitmap brush = brushDrawable.getBitmap();
			Paint paint = new Paint();
			paint.setAlpha(127);

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				canvas.drawBitmap(brush, x, y, paint);
				break;

			case MotionEvent.ACTION_MOVE:
				canvas.drawBitmap(brush, x, y, paint);
				break;
			}

			BrushView.this.invalidate();

			return true;
		}

	}

	public void setBitmapDrawable(BitmapDrawable bitmapDrawable) {
		this.brushDrawable = bitmapDrawable;
	}

}
