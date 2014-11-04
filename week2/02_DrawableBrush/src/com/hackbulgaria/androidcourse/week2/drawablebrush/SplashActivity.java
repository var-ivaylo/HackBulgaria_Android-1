package com.hackbulgaria.androidcourse.week2.drawablebrush;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	private BrushView customView;
	private ImageView prevBrush = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		customView = (BrushView) findViewById(R.id.my_custom_view);		
	}

	public void selectBrush(View view) {
		if (prevBrush != null) {
			prevBrush.setSelected(false);
		}
		
		ImageView imageView = (ImageView) view;
		imageView.setSelected(true);
		
		prevBrush = imageView;
		
		BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
		customView.setBitmapDrawable(bitmapDrawable);
	}
}