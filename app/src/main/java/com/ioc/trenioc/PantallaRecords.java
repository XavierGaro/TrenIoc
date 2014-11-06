package com.ioc.trenioc;

import java.util.List;

import com.ioc.motor.Grafics;
import com.ioc.motor.Input.EventTouch;
import com.ioc.motor.Joc;
import com.ioc.motor.Pantalla;

public class PantallaRecords extends Pantalla {

	String puntuacions[] = new String[5];

	public PantallaRecords(Joc joc) {
		super(joc);

		// Guardem les puntuacions en String local, perquè accedirem xifra a
		// xifra
		for (int i = 0; i < 5; i++)
			puntuacions[i] = "" + (i + 1) + ". " + Configuracio.records[i];

		if (Configuracio.soActivat)
			Recursos.soMusica.play();
		else
			Recursos.soMusica.pausa();
	}

	@Override
	public void mostra(float temps) {
		Grafics g = joc.getGraphics();

		g.dibuixaPixmap(Recursos.fonsTitol, 0, 0);
		g.dibuixaPixmap(Recursos.quadreRecord, 0, 80); // Afegit quadre de fons
		g.dibuixaPixmap(Recursos.menuPrincipal, 64, 20, 0, 42, 196, 42);

		int y = 100;

		// Per cada posició de l'array de puntuacions
		for (int i = 0; i < 5; i++) {
			// Mostrem la puntuació...
			dibuixaText(g, puntuacions[i], 20, y);

			// ... i incrementem la coordenada y per la següent línia
			y += 50;
		}

		// Dibuixem el botó
		g.dibuixaPixmap(Recursos.botons, 0, 416, 64, 64, 64, 64);
	}

	// Dibuixa un text de números
	public void dibuixaText(Grafics g, String line, int x, int y) {
		int mida = line.length();

		// Anem caràcter a caràcter
		for (int i = 0; i < mida; i++) {
			char c = line.charAt(i);

			// Espai en blanc
			if (c == ' ') {
				// avancem la coordenada x
				x += 20;
				continue;
			}

			int origenX = 0;
			int origenAmple = 0;

			// El caràcter del punt està en la posició 200 del png
			if (c == '.') {
				origenX = 200;
				origenAmple = 10;
			} else {
				// Per cada caràcter, calculem la posició X en el png. Estan en
				// ordre
				origenX = (c - '0') * 20;
				origenAmple = 20;
			}

			// Dibuixem aquest número
			g.dibuixaPixmap(Recursos.numeros, x, y, origenX, 0, origenAmple, 32);

			// i 'avancem' la coordenada x
			x += origenAmple;
		}
	}

	@Override
	public void actualitza(float temps) {
		List<EventTouch> events = joc.getInput().getEventsTouch();
		joc.getInput().getEventsTeclat();

		int mida = events.size();
		for (int i = 0; i < mida; i++) {
			EventTouch event = events.get(i);
			if (event.tipus == EventTouch.AIXECAT) {
				// Botó premut
				if (event.x < 64 && event.y > 416) {
					Recursos.soClick.play(1);
					joc.setPantalla(new PantallaMenuPrincipal(joc));
					return;
				}
			}
		}
	}

	@Override
	public void pause() {
		Recursos.soMusica.pausa();
	}

	@Override
	public void resume() {
		if (Configuracio.soActivat)
			Recursos.soMusica.play();
		else
			Recursos.soMusica.pausa();
	}

	@Override
	public void llibera() {
	}
}