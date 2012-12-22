package com.carlospinan.workshopandroid.helpers;

import android.graphics.Canvas;

public class Layer {

	private int zindex;
	private int tag;

	public Layer() {
		setTag(0);
	}

	public int getZindex() {
		return zindex;
	}

	public void setZindex(int zindex) {
		this.zindex = zindex;
	}

	public void onDraw(Canvas canvas) {
		// Implementar en clases hijas si es necesario
	}

	public void update() {
		// Implementar en clases hijas si es necesario
	}

	public void release() {
		// Implementar en clases hijas si es necesario
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

}
