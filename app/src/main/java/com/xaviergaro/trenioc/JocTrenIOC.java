package com.xaviergaro.trenioc;

import com.xaviergaro.motor.Pantalla;
import com.xaviergaro.motor.implementacio.MotorJoc;

public class JocTrenIOC extends MotorJoc {

	@Override
	public Pantalla getPantallaInicial() {
		return new PantallaCarregarRecursos(this);
	}
}