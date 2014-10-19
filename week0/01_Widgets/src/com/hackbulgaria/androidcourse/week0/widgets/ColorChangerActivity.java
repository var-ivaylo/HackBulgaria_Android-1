package com.hackbulgaria.androidcourse.week0.widgets;

import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ColorChangerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_color_changer);

		final Pattern hex = Pattern.compile("#[a-zA-Z0-9]{6}");

		EditText colorPicker = (EditText) findViewById(R.id.color_picker);
		final View colorBox = findViewById(R.id.color_box);

		colorPicker.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (hex.matcher(s.toString()).find()) {
					colorBox.setBackgroundColor(Color.parseColor(s.toString()));
				}
			}
		});

		View backBtn = findViewById(R.id.color_changer_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
