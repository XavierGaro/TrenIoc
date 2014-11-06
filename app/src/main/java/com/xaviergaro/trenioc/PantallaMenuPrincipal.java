package com.xaviergaro.trenioc;

import com.xaviergaro.motor.Grafics;
import com.xaviergaro.motor.Input.EventTouch;
import com.xaviergaro.motor.Joc;
import com.xaviergaro.motor.Pantalla;

import java.util.List;

public class PantallaMenuPrincipal extends Pantalla {
	// Constructor
	public PantallaMenuPrincipal(Joc joc) {
		super(joc);

		if (Configuracio.soActivat)
			Recursos.soMusica.play();
		else
			Recursos.soMusica.pausa();
	}

	@Override
	// Mostrem el menú principal amb tots els botons
	public void mostra(float temps) {
		// Obtenim el mòdul de gràfics
		Grafics g = joc.getGraphics();

		// Dibuixem els diferents botons del menú
		g.dibuixaPixmap(Recursos.fonsTitol, 0, 0);
		g.dibuixaPixmap(Recursos.logo, 32, 20);
		g.dibuixaPixmap(Recursos.menuPrincipal, 64, 220);

		// Depenent de si està activat o no dibuixem el logo del so activat o no
		if (Configuracio.soActivat)
			// Les dues icones estan al mateix fitxer però en diferents
			// coordenades
			g.dibuixaPixmap(Recursos.botons, 0, 416, 0, 0, 64, 64);
		else
			g.dibuixaPixmap(Recursos.botons, 0, 416, 64, 0, 64, 64);
	}

	@Override
	public void actualitza(float temps) {
		// Obtenim els gràfics
		Grafics g = joc.getGraphics();

		// Obtenim un llistat dels events de touch
		List<EventTouch> touchEvents = joc.getInput().getEventsTouch();
		joc.getInput().getEventsTeclat();

		// responem a tots els events emmagatzemats
		int mida = touchEvents.size();
		for (int i = 0; i < mida; i++) {
			EventTouch event = touchEvents.get(i);
			if (event.tipus == EventTouch.AIXECAT) {

				// Comprovem si s'ha tocat dins del botó de so
				if (Util.dins(event, 0, g.getAlt() - 64, 64, 64)) {
					// Canviem la configuració
					Configuracio.soActivat = !Configuracio.soActivat;

					// I la configuració global dels mòduls
					com.xaviergaro.motor.implementacio.MotorSo.soActivat = Configuracio.soActivat;
					com.xaviergaro.motor.implementacio.MotorMusica.musicaActivada = Configuracio.soActivat;

					if (Configuracio.soActivat)
						Recursos.soMusica.play();
					else
						Recursos.soMusica.pausa();

					Recursos.soClick.play(1);
					
					// Guardem la configuració
					Configuracio.desaConfiguracio(joc.getFileIO());
				}

				// Botó jugar
				if (Util.dins(event, 64, 220, 192, 42)) {
					Recursos.soMusica.pausa();
					joc.setPantalla(new PantallaJoc(joc));

					Recursos.soClick.play(1);
					return;
				}

				// Botó rècords
				if (Util.dins(event, 64, 220 + 42, 192, 42)) {
					joc.setPantalla(new PantallaRecords(joc));
					Recursos.soClick.play(1);
					return;
				}

				// Botó ajuda
				if (Util.dins(event, 64, 220 + 84, 192, 42)) {
					joc.setPantalla(new PantallaAjuda1(joc));
					Recursos.soClick.play(1);
					return;
				}
			}
		}
	}

	@Override
	public void pause() {
		Recursos.soMusica.pausa();
		Configuracio.desaConfiguracio(joc.getFileIO());
	}

	@Override
	public void resume() {
		if (Configuracio.soActivat)
			Recursos.soMusica.play();
	}

	@Override
	public void llibera() {
	}
}