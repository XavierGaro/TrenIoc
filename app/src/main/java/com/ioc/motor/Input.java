package com.ioc.motor;

import java.util.List;

//Interfície del mòdul Input
public interface Input {
	// Conté les característiques d'un event de pulsació de tecla
	public static class EventTeclat {
		public static final int KEY_DOWN = 0;
		public static final int KEY_UP = 1;

		public int tipus;
		public int codiTecla;
		public char caracterTecla;

		// Obtenim una cadena amb les característiques de la pulsació
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (tipus == KEY_DOWN)
				builder.append("Premuda, ");
			else
				builder.append("Aixecada, ");
			builder.append(codiTecla);
			builder.append(",");
			builder.append(caracterTecla);
			return builder.toString();
		}
	}

	// Conté les característiques d'un event de pulsació de pantalla
	public static class EventTouch {
		public static final int PREMUT = 0;
		public static final int AIXECAT = 1;
		public static final int ARROSEGAT = 2;

		public int tipus;
		public int x, y;// Posicions relatives respecte a l'origen
		public int punter;// id del dit per pantalles multitouch

		// //Obtenim una cadena amb les característiques de la pulsació
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (tipus == PREMUT)
				builder.append("Premut, ");

			else if (tipus == ARROSEGAT)
				builder.append("Arrosegat, ");
			else
				builder.append("Aixecat, ");
			builder.append(punter);
			builder.append(",");
			builder.append(x);
			builder.append(",");
			builder.append(y);
			return builder.toString();
		}
	}

	// Mètodes que s'han d'implementar

	// Retorna si la tecla amb aquest codi està premuda o no
	public boolean teclaPremuda(int codiTecla);

	// Mirem si un punter està premut
	public boolean punterPremut(int punter);

	// Mirem si la pantalla està premuda
	public boolean pantallaPremuda();

	// Obtenir les coordenades de la pulsació
	public int getTouchX(int punter);

	public int getTouchY(int punter);

	// Obté l'acceleració dels tres eixos
	public float getAccelX();

	public float getAccelY();

	public float getAccelZ();

	// Per gestió basada en events. Obté un llistat d'events de teclat i touch
	// que
	// s'han enregistrat des de la darrera vegada que es van cridar
	public List<EventTeclat> getEventsTeclat();

	public List<EventTouch> getEventsTouch();
}