package com.ioc.trenioc;

import com.ioc.motor.Grafics;
import com.ioc.motor.Input.EventTouch;

public class Util {

	// Retorna si un event de touch està dins d'un rectangle
	public static boolean dins(EventTouch event, int x, int y, int ample, int alt) {
		if (((event.x > x) && (event.x < x + ample - 1))
				&& ((event.y > y) && (event.y < y + alt - 1)))
			return true;
		else
			return false;
	}

	/**
	 * Dibuixa els números en color blanc o vermell a les coordenades passades
	 * per argument.
	 * 
	 * @param g
	 *            objecte on es dibuixarà
	 * @param line
	 *            text per dibuixar
	 * @param x
	 *            coordenada X per dibuixar
	 * @param y
	 *            coordenada y per dibuixar
	 * @param vermell
	 *            cert si volem el color vermell, o false per fer servir el
	 *            color blanc
	 */
	public static void dibuixaText(Grafics g, String line, int x, int y, boolean vermell) {
		int len = line.length();
		for (int i = 0; i < len; i++) {
			char character = line.charAt(i);
			if (character == ' ') {
				x += 20;
				continue;
			}
			int srcX = 0;
			int srcWidth = 0;
			if (character == '.') {
				srcX = 200;
				srcWidth = 10;
			} else {
				srcX = (character - '0') * 20;
				srcWidth = 20;
			}
			if (vermell) {
				g.dibuixaPixmap(Recursos.numerosVermell, x, y, srcX, 0, srcWidth, 32);
			} else {
				g.dibuixaPixmap(Recursos.numeros, x, y, srcX, 0, srcWidth, 32);
			}

			x += srcWidth;
		}
	}

	/**
	 * Dibuixa els números en color blanc a les coordenades passades per
	 * argument.
	 * 
	 * @param g
	 *            objecte on es dibuixarà
	 * @param line
	 *            text per dibuixar
	 * @param x
	 *            coordenada X per dibuixar
	 * @param y
	 *            coordenada y per dibuixar
	 */
	public static void dibuixaText(Grafics g, String line, int x, int y) {
		dibuixaText(g, line, x, y, false);
	}
}
