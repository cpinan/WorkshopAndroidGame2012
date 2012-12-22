package com.carlospinan.workshopandroid.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.carlospinan.workshopandroid.utils.Constants;

public class SpriteHelper extends Layer {

	private float x, y;
	private Matrix matrix;
	private Bitmap bitmap;
	private boolean visible;
	private float width, height;
	private Paint psprite, pdebug;
	private int zindex;

	public SpriteHelper(Bitmap bitmap) {
		super();
		_init(bitmap, 0, 0);
	}

	public SpriteHelper(Bitmap bitmap, PointF point) {
		super();
		_init(bitmap, point.x, point.y);
	}

	public SpriteHelper(Bitmap bitmap, float x, float y) {
		super();
		_init(bitmap, x, y);
	}

	private void _init(Bitmap bitmap, float x, float y) {
		this.bitmap = bitmap;
		zindex = 0;
		visible = true;
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		this.x = x;
		this.y = y;
		psprite = new Paint();
		pdebug = new Paint();
		pdebug.setARGB(128, 255, 0, 0);
		matrix = new Matrix();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (visible) {
			matrix.reset();
			matrix.postTranslate(x - width / 2, y - height / 2);
			canvas.drawBitmap(bitmap, matrix, psprite);
			if (Constants.IS_DEBUG_SPRITE) {
				canvas.drawRect(getBounds(), pdebug);
			}
		}
	}

	@Override
	public void update() {
		// Usar como padre para un modelo personalizado e implementarlo
	}

	public void setPosition(PointF point) {
		x = point.x;
		y = point.y;
	}

	public void setPosition(float x, float y) {
		setPosition(new PointF(x, y));
	}

	public boolean isCollide(SpriteHelper s) {
		return s != null && visible && s.isVisible()
				&& getBounds().intersect(s.getBounds());
	}

	public boolean isCollide(RectF r) {
		return r != null && visible && getBounds().intersect(r);
	}

	public RectF getBounds() {
		return new RectF(x - width / 2, y - height / 2, x + width / 2, y
				+ height / 2);
	}

	@Override
	public void release() {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		bitmap = null;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getZindex() {
		return zindex;
	}

	public void setZindex(int zindex) {
		this.zindex = zindex;
	}

}
