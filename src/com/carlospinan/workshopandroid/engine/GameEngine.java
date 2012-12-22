package com.carlospinan.workshopandroid.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.carlospinan.workshopandroid.helpers.BaseActivity;
import com.carlospinan.workshopandroid.helpers.LayoutManagerHelper;
import com.carlospinan.workshopandroid.objects.Background;
import com.carlospinan.workshopandroid.objects.Objects;
import com.carlospinan.workshopandroid.objects.Player;
import com.carlospinan.workshopandroid.threads.GameThread;
import com.carlospinan.workshopandroid.utils.Assets;
import com.carlospinan.workshopandroid.utils.Constants;
import com.carlospinan.workshopandroid.utils.Global;
import com.carlospinan.workshopandroid.utils.SoundManager;

public class GameEngine extends SurfaceView implements SurfaceHolder.Callback {

	private Context context;
	private GameThread thread;
	private LayoutManagerHelper lmanager;

	private Background background;
	private Objects objects;

	public Player player;
	public boolean gameOver;

	private Paint pgameover;

	public GameEngine(Context context) {
		super(context);

		pgameover = new Paint();
		pgameover.setARGB(255, 0, 0, 0);
		pgameover.setTextSize(16.0f);

		gameOver = false;
		this.context = context;
		_loadResources();
		thread = new GameThread(this, getHolder());
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (thread.getState() == Thread.State.TERMINATED) {
			thread = new GameThread(this, getHolder());
		}
		thread.setRunning(true);
		thread.setPause(false);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setPause(true);
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.save();
		canvas.drawColor(0xFF000000);
		canvas.scale(Global.SCALEX, Global.SCALEY);

		if (lmanager != null) {
			lmanager.onDraw(canvas);
		}

		if (gameOver) {
			if (player.isWin()) {
				canvas.drawText("Ganaste!!!!!! =D", 80,
						Constants.DEFAULT_SCREEN_HEIGHT / 2, pgameover);

			} else {
				canvas.drawText("Ohhhhhhhh Perdiste!", 80,
						Constants.DEFAULT_SCREEN_HEIGHT / 2, pgameover);

			}
			canvas.drawText("Tap en la pantalla para continuar", 50,
					Constants.DEFAULT_SCREEN_HEIGHT / 2 + 100, pgameover);
		}

		canvas.restore();
	}

	public void update() {

		if (!gameOver) {
			if (player.getFuel() <= 0) {
				SoundManager.pauseBackgroundMusic();
				BaseActivity.vibrate(500);
				SoundManager.playSFX(Assets.sfx_game_over);
				gameOver = true;
			} else if (player.isWin()) {
				SoundManager.pauseBackgroundMusic();
				BaseActivity.vibrate(500);
				SoundManager.playSFX(Assets.sfx_win);
				gameOver = true;
			} else {

				if (!player.isPowerUp()) {
					if (player.isCollide(background.getCespedLeft())
							|| player.isCollide(background.getCespedRight())) {
						player.setMinSpeed();
					} else {
						player.setNormalSpeed();
					}
				}

				background.setTrackSpeed(player.getTrackSpeed());

				if (lmanager != null) {
					lmanager.update();
				}
			}
		}

	}

	public void release() {
		if (lmanager != null) {
			lmanager.release();
		}
	}

	public void onDestroy() {

	}

	public void onPause() {
		if (thread != null) {
			thread.onPause();
		}
	}

	public void onResume() {
		if (thread != null) {
			thread.onResume();
		}
	}

	private void _loadResources() {
		lmanager = new LayoutManagerHelper();

		player = new Player(context);
		lmanager.add(player, 10);

		background = new Background(context);
		lmanager.add(background, 0);

		objects = new Objects(context, player);
		lmanager.add(objects, 9);

	}
}
