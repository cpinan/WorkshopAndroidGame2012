package com.carlospinan.workshopandroid.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager {

	public static MediaPlayer backgroundMusic = null;
	public static HashMap<String, HashMap<Integer, SoundPool>> hashSounds = new HashMap<String, HashMap<Integer, SoundPool>>();

	public static void playBackgroundMusic(Context context, String assetSound) {
		if (assetSound.length() > 0) {
			MediaPlayer mp = new MediaPlayer();
			try {
				AssetManager assetManager = context.getAssets();
				AssetFileDescriptor descriptor = assetManager
						.openFd(assetSound);
				mp.setDataSource(descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				mp.prepare();
				mp.setLooping(true);
			} catch (IOException e) {
				mp = null;
			}
			if (mp != null) {
				backgroundMusic = mp;
				mp.start();
			}
		}
	}

	public static void preloadSFX(Context context, String assetSound) {
		if (assetSound.length() > 0) {
			int soundID = -1;
			SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
			try {
				AssetManager assetManager = context.getAssets();
				AssetFileDescriptor descriptor = assetManager
						.openFd(assetSound);
				soundID = soundPool.load(descriptor, 1);

			} catch (IOException e) {
			}
			if (soundID != -1) {
				if (hashSounds.get(assetSound) == null) {
					HashMap<Integer, SoundPool> hashMap = new HashMap<Integer, SoundPool>();
					hashMap.put(soundID, soundPool);
					hashSounds.put(assetSound, hashMap);
				}
			}
		}
	}

	public static void playSFX(String assetSound) {
		if (hashSounds.get(assetSound) != null) {
			HashMap<Integer, SoundPool> hash = hashSounds.get(assetSound);
			for (Entry<Integer, SoundPool> entry : hash.entrySet()) {
				int sID = entry.getKey();
				SoundPool sp = entry.getValue();
				sp.play(sID, 1, 1, 1, 0, 1);
			}
		}
	}

	public static void releaseMusic() {
		if (backgroundMusic != null)
			backgroundMusic.release();
		backgroundMusic = null;
	}

	public static void pauseBackgroundMusic() {
		if (backgroundMusic != null && backgroundMusic.isPlaying())
			backgroundMusic.pause();
	}

	public static void resumeBackgroundMusic() {
		if (backgroundMusic != null && !backgroundMusic.isPlaying())
			backgroundMusic.start();
	}

	public static void releaseSounds() {
		releaseMusic();

		for (Entry<String, HashMap<Integer, SoundPool>> entry : hashSounds
				.entrySet()) {
			HashMap<Integer, SoundPool> hs1 = entry.getValue();
			for (Entry<Integer, SoundPool> e : hs1.entrySet()) {
				SoundPool sp = e.getValue();
				if (sp != null) {
					sp.release();
				}
			}
		}

		hashSounds.clear();
	}

}
