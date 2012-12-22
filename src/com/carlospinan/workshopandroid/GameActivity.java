package com.carlospinan.workshopandroid;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;

import com.carlospinan.workshopandroid.engine.GameEngine;
import com.carlospinan.workshopandroid.helpers.BaseActivity;
import com.carlospinan.workshopandroid.utils.Assets;
import com.carlospinan.workshopandroid.utils.SoundManager;

public class GameActivity extends BaseActivity implements SensorEventListener {

	private GameEngine gameEngine;
	private SensorManager sensorManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		gameEngine = new GameEngine(this);
		setContentView(gameEngine);

		SoundManager.playBackgroundMusic(getBaseContext(),
				Assets.snd_background);

		SoundManager.preloadSFX(getBaseContext(), Assets.sfx_energy);
		SoundManager.preloadSFX(getBaseContext(), Assets.sfx_game_over);
		SoundManager.preloadSFX(getBaseContext(), Assets.sfx_power_up);
		SoundManager.preloadSFX(getBaseContext(), Assets.sfx_hit);
		SoundManager.preloadSFX(getBaseContext(), Assets.sfx_win);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (gameEngine != null) {
			gameEngine.onPause();
		}

		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (gameEngine != null) {
			gameEngine.onResume();
		}

		if (sensorManager != null) {
			sensorManager.registerListener(this,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_GAME);
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				if (gameEngine != null && gameEngine.player != null
						&& !gameEngine.gameOver) {
					float dX = event.values[0] * -1;
					float dY = event.values[1];
					gameEngine.player.move(dX, dY);
				}
				break;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gameEngine != null && gameEngine.gameOver) {
			SoundManager.releaseSounds();
			finish();
			startActivity(getIntent());
		}
		return true;
	}

}
