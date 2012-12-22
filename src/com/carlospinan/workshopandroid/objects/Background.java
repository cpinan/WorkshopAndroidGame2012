package com.carlospinan.workshopandroid.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.carlospinan.workshopandroid.helpers.Layer;
import com.carlospinan.workshopandroid.helpers.SpriteHelper;
import com.carlospinan.workshopandroid.utils.Assets;
import com.carlospinan.workshopandroid.utils.Constants;
import com.carlospinan.workshopandroid.utils.Utils;

public class Background extends Layer {

	private static final String res_grass = Assets.GRAPH_SCREEN_DIR
			+ Assets.res_grass;

	private Context context;
	private static final float TRACK_W = 5, TRACK_H = 90;
	public static final float GREEN_LEFT = 50;
	public static final float GREEN_RIGHT = Constants.DEFAULT_SCREEN_WIDTH
			- GREEN_LEFT;

	private Paint pcesped, ptracks;
	private RectF rcespedL, rcespedR;
	private RectF rtracks[];
	private float track_speed;
	private SpriteHelper spGrass[];

	public Background(Context context) {
		this.context = context;
		_init();
	}

	private void _init() {
		int i;

		track_speed = 0;

		pcesped = new Paint();
		pcesped.setColor(0xAA99FF66);

		ptracks = new Paint();
		ptracks.setColor(0xFFFFFFFF);

		rcespedL = new RectF(0, 0, GREEN_LEFT, Constants.DEFAULT_SCREEN_HEIGHT);
		rcespedR = new RectF(GREEN_RIGHT, 0, Constants.DEFAULT_SCREEN_WIDTH,
				Constants.DEFAULT_SCREEN_HEIGHT);

		rtracks = new RectF[10];

		float left = Constants.DEFAULT_SCREEN_WIDTH / 2 - TRACK_W, top = -Constants.DEFAULT_SCREEN_HEIGHT;
		float right = left + TRACK_W;
		float bottom;

		for (i = 0; i < rtracks.length; i++) {
			bottom = top + TRACK_H;
			rtracks[i] = new RectF(left, top, right, bottom);
			top += TRACK_H * 1.2;
		}

		spGrass = new SpriteHelper[20];
		Bitmap bmpGrass = Utils.getBitmapFromAsset(context, res_grass, null);

		top = -Constants.DEFAULT_SCREEN_HEIGHT / 2;

		for (i = 0; i < spGrass.length; i++) {
			if (i % 2 == 0) {
				left = bmpGrass.getWidth() / 2 + 10;
				if (i > 0) {
					top += bmpGrass.getHeight() * 2 + 20;
				}
			} else {
				left = Constants.DEFAULT_SCREEN_WIDTH - bmpGrass.getWidth() / 2
						- 10;
			}

			spGrass[i] = new SpriteHelper(bmpGrass, left, top);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(0xFFD8D8D8);
		canvas.drawRect(rcespedL, pcesped);
		canvas.drawRect(rcespedR, pcesped);

		for (RectF track : rtracks) {
			canvas.drawRect(track, ptracks);
		}

		for (SpriteHelper s : spGrass) {
			s.onDraw(canvas);
		}

	}

	@Override
	public void update() {
		super.update();
		for (RectF track : rtracks) {
			track.top += track_speed;
			track.bottom = track.top + TRACK_H;
			if (track.top > Constants.DEFAULT_SCREEN_HEIGHT + TRACK_H * 2) {
				track.top = -Constants.DEFAULT_SCREEN_HEIGHT + TRACK_H / 2;
				track.bottom = track.top + TRACK_H;
			}
		}

		for (SpriteHelper s : spGrass) {
			s.setY(s.getY() + track_speed);
			if (s.getY() > Constants.DEFAULT_SCREEN_HEIGHT + s.getHeight() * 2) {
				s.setY(-Constants.DEFAULT_SCREEN_HEIGHT / 2 - s.getHeight() * 2);
			}
		}

	}

	public void setTrackSpeed(float track_speed) {
		if (track_speed < 0)
			track_speed = 0;
		this.track_speed = track_speed * 3;
	}

	public RectF getCespedRight() {
		return rcespedR;
	}

	public RectF getCespedLeft() {
		return rcespedL;
	}
}
