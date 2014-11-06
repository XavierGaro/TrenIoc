package com.ioc.trenioc;

import com.ioc.motor.Pantalla;
import com.ioc.motor.implementacio.MotorJoc;

public class JocTrenIOC extends MotorJoc {

	@Override
	public Pantalla getPantallaInicial() {
		return new PantallaCarregarRecursos(this);
	}
}