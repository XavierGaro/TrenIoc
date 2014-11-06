package com.ioc.motor.implementacio;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ioc.motor.Audio;
import com.ioc.motor.Musica;
import com.ioc.motor.So;

//Implementació de la interfície d'Audio
public class MotorAudio implements Audio {
	AssetManager assets;
	SoundPool soundPool;

	// Constructor
	public MotorAudio(Activity activity) {
		// Inicialitzem components
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	@Override
	public Musica nouMusica(String nomFitxer) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(nomFitxer);

			// Creem el nou recurs de música
			return new MotorMusica(assetDescriptor);
		} catch (IOException e) {
			throw new RuntimeException("No pot carregar fitxer de música:"
					+ nomFitxer);
		}
	}

	@Override
	public So nouSo(String nomFitxer) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(nomFitxer);
			int soundId = soundPool.load(assetDescriptor, 0);
			// Creem fitxer de so
			return new MotorSo(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("No pot carregar fitxer de so:"
					+ nomFitxer);
		}
	}
}