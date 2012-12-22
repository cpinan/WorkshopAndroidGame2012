package com.carlospinan.workshopandroid.helpers;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.carlospinan.workshopandroid.utils.Assets;
import com.carlospinan.workshopandroid.utils.Constants;
import com.carlospinan.workshopandroid.utils.Global;
import com.carlospinan.workshopandroid.utils.SoundManager;

public class BaseActivity extends Activity {

	private static Vibrator vibrator = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		setFullScreen();
		_init();
	}

	private void _init() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		Global.SCALEX = (float) width / (float) Constants.DEFAULT_SCREEN_WIDTH;
		Global.SCALEY = (float) height
				/ (float) Constants.DEFAULT_SCREEN_HEIGHT;

		String dir = Constants.DEFAULT_SCREEN_WIDTH + "x"
				+ Constants.DEFAULT_SCREEN_HEIGHT + "/";
		Assets.GRAPH_SCREEN_DIR = Assets.GRAPHICS_DIR + "" + dir;

	}

	@Override
	protected void onPause() {
		super.onPause();
		SoundManager.pauseBackgroundMusic();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SoundManager.resumeBackgroundMusic();
	}


	public void setFullScreen() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	public static void vibrate(long milliseconds) {
		if (vibrator != null) {
			vibrator.cancel();
			vibrator.vibrate(milliseconds);
		}
	}

}
