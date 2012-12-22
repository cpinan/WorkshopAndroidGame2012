package com.carlospinan.workshopandroid.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.carlospinan.workshopandroid.helpers.SpriteHelper;
import com.carlospinan.workshopandroid.utils.Assets;
import com.carlospinan.workshopandroid.utils.Constants;
import com.carlospinan.workshopandroid.utils.Utils;

public class Player extends SpriteHelper {

	private static final String res_player = Assets.GRAPH_SCREEN_DIR
			+ Assets.res_player;

	private int current_speed;

	private float fuel, quit_fuel;
	private static final float speedX = 2;
	private static final float maxSpeedY = 2, normalSpeedY = 1,
			minSpeedY = 0.5f, friction = 0.2f;
	private float speedY = normalSpeedY;

	private static final int NORMAL_SPEED = 0;
	private static final int MIN_SPEED = 1;
	private static final int MAX_SPEED = 2;
	private static final long POWER_UP_TIME = 3000L;
	private static final int MAX_SCORE = 2000;

	private Paint pempty, pfuel, pscore;
	private RectF rempty, rfuel;

	private boolean power_up, win;
	private long tpower_up = 0L;

	private static final float MAX_FUEL_WIDTH = 150f;

	private int score;

	public Player(Context context) {
		super(Utils.getBitmapFromAsset(context, res_player, null));
		float x = Constants.DEFAULT_SCREEN_WIDTH / 2 - getWidth() / 2;
		float y = Constants.DEFAULT_SCREEN_HEIGHT - getHeight() / 2;
		setPosition(x, y);

		current_speed = NORMAL_SPEED;
		fuel = 1.0f;
		quit_fuel = 0.001f;

		pempty = new Paint();
		pempty.setARGB(255, 255, 0, 0);

		pfuel = new Paint();
		pfuel.setARGB(255, 0, 255, 0);

		rempty = new RectF(80, 10, 80 + MAX_FUEL_WIDTH, 40);
		rfuel = new RectF(rempty);

		pscore = new Paint();
		pscore.setColor(0xFF000000);
		pscore.setTextSize(14.0f);

		power_up = false;
		win = false;
		score = 0;
	}

	public void move(float dX, float dY) {
		float current_speedx = speedX * dX;
		float current_speedy = speedY * dY;

		dX = getX() + current_speedx;
		dY = (getY() + current_speedy) + friction;

		if (dX > getWidth() / 2
				&& dX < Constants.DEFAULT_SCREEN_WIDTH - getWidth() / 2)
			setX(dX);

		if (dY > getHeight() && dY < Constants.DEFAULT_SCREEN_HEIGHT)
			setY(dY);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawRect(rempty, pempty);
		canvas.drawRect(rfuel, pfuel);
		canvas.drawText("Score: " + score, rempty.left + 10, rempty.top + 20,
				pscore);

	}

	@Override
	public void update() {
		super.update();
		switch (current_speed) {
		case NORMAL_SPEED:
			speedY = normalSpeedY;
			break;
		case MIN_SPEED:
			speedY = minSpeedY;
			break;
		case MAX_SPEED:
			speedY = maxSpeedY;
			break;
		default:
		}
		if (fuel - quit_fuel >= 0) {
			fuel -= quit_fuel;
		} else {
			fuel = 0;
		}

		rfuel.right = MAX_FUEL_WIDTH * fuel + rfuel.left;

		if (power_up && tpower_up > 0) {
			if (System.currentTimeMillis() - tpower_up > POWER_UP_TIME) {
				power_up = false;
				tpower_up = 0;
				setNormalSpeed();
			}
		}

		if (!win) {
			score++;
			if (score >= MAX_SCORE) {
				score = MAX_SCORE;
				win = true;
			}
		}
	}

	public float getFuel() {
		return fuel;
	}

	public void setFuel(float fuel) {
		this.fuel = fuel;
	}

	public void addToFuel(float f) {
		this.fuel += f;

		if (this.fuel < 0)
			this.fuel = 0;

		if (this.fuel > 1)
			this.fuel = 1;

	}

	public void setNormalSpeed() {
		current_speed = NORMAL_SPEED;
	}

	public void setMaxSpeed() {
		current_speed = MAX_SPEED;
	}

	public void setMinSpeed() {
		current_speed = MIN_SPEED;
	}

	public float getTrackSpeed() {
		return speedY;
	}

	public void enablePowerUp() {
		tpower_up = System.currentTimeMillis();
		setMaxSpeed();
		this.power_up = true;
	}

	public boolean isPowerUp() {
		return power_up;
	}

	public boolean isWin() {
		return win;
	}
}
