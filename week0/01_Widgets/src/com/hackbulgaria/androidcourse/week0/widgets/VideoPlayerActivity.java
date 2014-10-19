package com.hackbulgaria.androidcourse.week0.widgets;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity implements OnClickListener {
	public static VideoView Video;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_video_player);

		View backBtn = findViewById(R.id.video_player_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		VideoPlayerActivity.Video = (VideoView) findViewById(R.id.video);
		VideoPlayerActivity.Video.setVideoPath(Environment.getExternalStorageDirectory().getPath() + this.getString(R.string.video_name));
		VideoPlayerActivity.Video.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				ImageView btn = (ImageView) findViewById(R.id.video_play_pause_button);
				btn.setImageDrawable(getResources().getDrawable(R.drawable.play));
				
			}
		});

		findViewById(R.id.video_prev_button).setOnClickListener(this);
		findViewById(R.id.video_play_pause_button).setOnClickListener(this);
		findViewById(R.id.video_next_button).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.video_prev_button:
			this.pauseVideo();
			VideoPlayerActivity.Video.seekTo(VideoPlayerActivity.Video.getCurrentPosition() - 3000);
			break;

		case R.id.video_play_pause_button:
			if (VideoPlayerActivity.Video.isPlaying()) {
				this.pauseVideo();
			} else {
				this.unpauseVideo();
			}
			break;

		case R.id.video_next_button:
			this.pauseVideo();
			VideoPlayerActivity.Video.seekTo(VideoPlayerActivity.Video.getCurrentPosition() + 3000);
			break;

		default:
			break;
		}
	}
	
	private void pauseVideo() {
		ImageView btn = (ImageView) findViewById(R.id.video_play_pause_button);
		VideoPlayerActivity.Video.pause();
		btn.setImageDrawable(this.getResources().getDrawable(R.drawable.play));
	}
	
	private void unpauseVideo() {
		ImageView btn = (ImageView) findViewById(R.id.video_play_pause_button);
		VideoPlayerActivity.Video.start();
		btn.setImageDrawable(this.getResources().getDrawable(R.drawable.pause));
	}

}
