package com.ioc.motor.implementacio;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.ioc.motor.Input;

public class MotorInput implements Input {
	// Els tres handlers per cada tipus d'event d'entrada
	AccelerometreHandler handlerAccelerometre;
	TeclatHandler handlerTeclat;
	TouchHandler handlerTouch;

	// Constructor, inicialitzem els handlers
	public MotorInput(Context context, View view, float escalaX, float escalaY) {
		handlerAccelerometre = new AccelerometreHandler(context);
		handlerTeclat = new TeclatHandler(view);
		handlerTouch = new SimpleTouch(view, escalaX, escalaY);
	}

	// Cridem els diferents handlers per implementar els mètodes de la
	// interfície
	@Override
	public boolean punterPremut(int pointer) {
		return handlerTouch.pantallaPremuda(pointer);
	}

	@Override
	public boolean pantallaPremuda() {
		return handlerTouch.pantallaPremuda(0);
	}

	@Override
	public boolean teclaPremuda(int codiTecla) {
		return handlerTeclat.teclaPremuda(codiTecla);
	}

	@Override
	public List<EventTouch> getEventsTouch() {
		return handlerTouch.getEventsTouch();
	}

	@Override
	public List<EventTeclat> getEventsTeclat() {
		return handlerTeclat.getEventsTeclat();
	}

	@Override
	public int getTouchX(int pointer) {
		return handlerTouch.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		return handlerTouch.getTouchY(pointer);
	}

	@Override
	public float getAccelX() {
		return handlerAccelerometre.getAccelX();
	}

	@Override
	public float getAccelY() {
		return handlerAccelerometre.getAccelY();
	}

	@Override
	public float getAccelZ() {
		return handlerAccelerometre.getAccelZ();
	}
}