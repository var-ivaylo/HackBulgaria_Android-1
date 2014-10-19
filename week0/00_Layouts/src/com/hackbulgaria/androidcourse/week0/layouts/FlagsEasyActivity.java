package com.hackbulgaria.androidcourse.week0.layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FlagsEasyActivity extends Activity implements OnClickListener {
	public static int colorID = 0;
	public static int[] colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String flagsOrientation = this.getIntent().getStringExtra("flagsOrientation");
		View[] flags = new View[3];
		View backBtn = null;

		if (flagsOrientation.equals("vertical")) {
			this.setContentView(R.layout.activity_flags_easy_vertical);
			
			flags[0] = findViewById(R.id.flags_easy_vertical_first);
			flags[1] = findViewById(R.id.flags_easy_vertical_second);
			flags[2] = findViewById(R.id.flags_easy_vertical_third);
			
			backBtn = findViewById(R.id.flags_easy_vertical_back);
		} else if (flagsOrientation.equals("horizontal")) {
			this.setContentView(R.layout.activity_flags_easy_horizontal);
			
			flags[0] = findViewById(R.id.flags_easy_horizontal_first);
			flags[1] = findViewById(R.id.flags_easy_horizontal_second);
			flags[2] = findViewById(R.id.flags_easy_horizontal_third);
			
			backBtn = findViewById(R.id.flags_easy_horizontal_back);
		}

		FlagsEasyActivity.colors = this.getResources().getIntArray(R.array.rainbow);

		for (View v : flags) {
			v.setBackgroundColor(this.nextColor());
			v.setOnClickListener(this);
		}

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		v.setBackgroundColor(this.nextColor());
	}

	private int nextColor() {
		FlagsEasyActivity.colorID++;
		if (FlagsEasyActivity.colorID >= FlagsEasyActivity.colors.length) {
			FlagsEasyActivity.colorID = 0;
		}
		return FlagsEasyActivity.colors[FlagsEasyActivity.colorID];
	}
}
