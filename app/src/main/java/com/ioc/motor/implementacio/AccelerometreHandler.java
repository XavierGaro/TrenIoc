package com.ioc.motor.implementacio;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometreHandler implements SensorEventListener {
	float accelX;
	float accelY;
	float accelZ;

	public AccelerometreHandler(Context context) {
		// Obtenim un sensor manager per saber si hi ha un acceleròmetre al
		// dispositiu
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		// Si hi ha un acceleròmetre
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			// Obtenim l'acceleròmetre
			Sensor accelerometre = manager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);

			// i registrem aquesta classe com a listener dels seus events
			// SENSOR_DELAY_GAME és una constant dissenyada per jocs per dir amb
			// quina freqüència es comprova l'acceleròmetre
			manager.registerListener(this, accelerometre,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	@Override
	// S'ha produït un event de l'acceleròmetre
	public void onSensorChanged(SensorEvent event) {
		// Obtenim l'acceleració dels tres eixos
		accelX = event.values[0];// X
		accelY = event.values[1];// Y
		accelZ = event.values[2];// Z
	}

	public float getAccelX() {
		return accelX;
	}

	public float getAccelY() {
		return accelY;
	}

	public float getAccelZ() {
		return accelZ;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// No fem res aquí
	}
}