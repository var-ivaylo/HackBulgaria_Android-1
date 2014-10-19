package com.hackbulgaria.androidcourse.week0.layouts;

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

		View[] btns = { findViewById(R.id.task_easy_button),
				findViewById(R.id.task_medium_button),
				findViewById(R.id.task_hard_button),
				findViewById(R.id.task_uber_button) };

		for (View v : btns) {
			v.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.task_easy_button:
			i = new Intent(getApplicationContext(), FlagsEasyActivity.class);
			i.putExtra("flagsOrientation", "vertical");
			// i.putExtra("flagsOrientation", "horizontal");
			break;

		case R.id.task_medium_button:
			i = new Intent(getApplicationContext(), FlagsMediumActivity.class);
			break;

		case R.id.task_hard_button:
			i = new Intent(getApplicationContext(), FlagsHardActivity.class);
			break;

		case R.id.task_uber_button:
			i = new Intent(getApplicationContext(), TaskUberActivity.class);
			break;

		default:
			i = null;
			break;
		}

		startActivity(i);
	}
}
