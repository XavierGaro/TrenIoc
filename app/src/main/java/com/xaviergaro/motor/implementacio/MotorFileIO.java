package com.xaviergaro.motor.implementacio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

import com.xaviergaro.motor.FileIO;

public class MotorFileIO implements FileIO {
	AssetManager recursos;
	String pathSD;

	public MotorFileIO(AssetManager recursos) {
		// Inicialitzem
		this.recursos = recursos;
		this.pathSD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}

	@Override
	public InputStream llegirRecurs(String nomFitxer) throws IOException {
		return recursos.open(nomFitxer);
	}

	@Override
	public InputStream llegirFitxer(String nomFitxer) throws IOException {
		return new FileInputStream(pathSD + nomFitxer);
	}

	@Override
	public OutputStream escriureFitxer(String nomFitxer) throws IOException {
		return new FileOutputStream(pathSD + nomFitxer);
	}
}