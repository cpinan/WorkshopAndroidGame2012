package com.carlospinan.workshopandroid.threads;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.carlospinan.workshopandroid.engine.GameEngine;

public class GameThread extends Thread {

	private static final long FPS = 24;
	private static long PERIOD = 1000 / FPS;
	private GameEngine engine = null;
	private SurfaceHolder holder = null;
	private boolean pause, running;

	public GameThread(GameEngine engine, SurfaceHolder holder) {
		this.engine = engine;
		this.holder = holder;
		pause = false;
		running = false;
	}

	@Override
	public void run() {
		super.run();
		Canvas canvas = null;
		while (running) {
			if (!pause) {
				try {
					canvas = holder.lockCanvas();
					synchronized (holder) {
						engine.update();
						engine.onDraw(canvas);
						try {
							Thread.sleep(FPS);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} finally {
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
		try {
			Thread.sleep(PERIOD);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onPause() {
		pause = true;
	}

	public void onResume() {
		pause = false;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
