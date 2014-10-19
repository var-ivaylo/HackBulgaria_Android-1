package com.hackbulgaria.androidcourse.week0.widgets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SplashActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		View[] btns = { findViewById(R.id.task_color_changer),
				findViewById(R.id.task_video_player) };

		for (View v : btns) {
			v.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.task_color_changer:
			i = new Intent(getApplicationContext(), ColorChangerActivity.class);
			break;

		case R.id.task_video_player:
			i = new Intent(getApplicationContext(), VideoPlayerActivity.class);
			break;

		default:
			i = null;
			break;
		}

		startActivity(i);
	}

}
