package com.carlospinan.workshopandroid.objects;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.carlospinan.workshopandroid.helpers.Layer;
import com.carlospinan.workshopandroid.helpers.SpriteHelper;
import com.carlospinan.workshopandroid.utils.Assets;
import com.carlospinan.workshopandroid.utils.SoundManager;
import com.carlospinan.workshopandroid.utils.Utils;

public class Objects extends Layer {

	private Player player;

	private static final int OBJ_ENERGY = 0;
	private static final int OBJ_FIRE_SPEED = 1;
	private static final int OBJ_ENEMY = 2;

	private static final String res_energy = Assets.GRAPH_SCREEN_DIR
			+ Assets.res_energy;

	private static final String res_fire = Assets.GRAPH_SCREEN_DIR
			+ Assets.res_fire;

	private static final String res_enemy = Assets.GRAPH_SCREEN_DIR
			+ Assets.res_enemy;

	private Bitmap bmpEnergy, bmpFire, bmpEnemy;

	private List<TmpObject> lstObjects;

	private static final float SPAWN_TIME = 15.0f;
	private float time, incTime;
	private int current = 0;

	public Objects(Context context, Player player) {
		this.player = player;
		bmpEnergy = Utils.getBitmapFromAsset(context, res_energy, null);
		bmpFire = Utils.getBitmapFromAsset(context, res_fire, null);
		bmpEnemy = Utils.getBitmapFromAsset(context, res_enemy, null);

		lstObjects = new ArrayList<TmpObject>();
		time = 0.0f;
		incTime = 1.0f;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (TmpObject obj : lstObjects) {
			obj.onDraw(canvas);
		}
	}

	@Override
	public void update() {
		super.update();
		if (player != null) {
			List<TmpObject> tmpList = new ArrayList<TmpObject>();

			for (TmpObject obj : lstObjects) {

				if (player.isPowerUp()) {
					obj.setDY(2.5f);
				} else {
					obj.setDY(1.0f);
				}

				obj.update();
				if (player.isCollide(obj)) {
					if (obj.getObjectType() == OBJ_ENERGY) {
						SoundManager.playSFX(Assets.sfx_energy);
						player.addToFuel(0.08f);
					} else if (obj.getObjectType() == OBJ_FIRE_SPEED) {
						SoundManager.playSFX(Assets.sfx_power_up);
						incTime *= 2.5f;
						player.enablePowerUp();
					} else if (obj.getObjectType() == OBJ_ENEMY) {
						SoundManager.playSFX(Assets.sfx_hit);
						player.addToFuel(-0.1f);
					}
					obj.setVisible(false);
					tmpList.add(obj);
				}

			}

			for (TmpObject obj : tmpList) {
				lstObjects.remove(obj);
			}

		}
		time += incTime;
		if (time >= SPAWN_TIME) {
			addObject();
			time = 0.0f;
		}
	}

	private void addObject() {
		float speedY = Utils.getRandomNumber(1.0f, 10.0f);
		TmpObject tmp = null;

		Bitmap bmp = null;
		int tag = 0;

		if (current == 0) {
			bmp = bmpEnergy;
			tag = OBJ_ENERGY;
		} else if (current == 1) {
			bmp = bmpEnemy;
			tag = OBJ_ENEMY;
		} else {
			bmp = bmpFire;
			tag = OBJ_FIRE_SPEED;
		}
		current++;
		if (current > 2) {
			current = 0;
		}

		float x = Utils.getRandomNumber(Background.GREEN_LEFT + bmp.getWidth()
				/ 2, Background.GREEN_RIGHT - bmp.getHeight() / 2);
		float y = -bmp.getHeight();

		tmp = new TmpObject(bmp, x, y, speedY, tag);

		lstObjects.add(tmp);
		incTime = Utils.getRandomNumber(0.1f, 0.5f);
	}

	@Override
	public void release() {
		super.release();

		bmpEnergy = null;
		bmpFire = null;

		for (TmpObject obj : lstObjects) {
			obj.release();
		}

	}

	private class TmpObject extends SpriteHelper {

		private float speedY, dY;
		private int object_type;

		public TmpObject(Bitmap bitmap, float x, float y, float speedY,
				int object_type) {
			super(bitmap, x, y);
			this.speedY = speedY;
			this.object_type = object_type;
			dY = 1.0f;
		}

		public int getObjectType() {
			return object_type;
		}

		public void setDY(float dY) {
			this.dY = dY;
		}

		@Override
		public void update() {
			super.update();
			if (isVisible()) {
				setY(getY() + speedY * dY);
			}
		}

	}

}
