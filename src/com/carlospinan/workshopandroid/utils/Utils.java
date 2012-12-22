package com.carlospinan.workshopandroid.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class Utils {

	public static void log(String msg) {
		if (Constants.IS_DEBUG_LOG) {
			Log.d(Constants.LOG_TAG, msg);
		}
	}

	public static Bitmap getBitmapFromAsset(Context context, String str_name,
			Options options) {
		AssetManager asset_manager = context.getAssets();
		InputStream istr = null;
		Bitmap bitmap = null;
		try {
			istr = asset_manager.open(str_name);
			bitmap = BitmapFactory.decodeStream(istr, null, options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static float getRandomNumber(float start, float end) {
		Random random = new Random();
		float value = (random.nextFloat() * (end - start + 1) + start);
		return value;
	}

}
