package com.ioc.trenioc;

import java.util.List;

import com.ioc.motor.Grafics;
import com.ioc.motor.Input.EventTouch;
import com.ioc.motor.Joc;
import com.ioc.motor.Pantalla;
import com.ioc.motor.Pixmap;

public class PantallaJoc extends Pantalla {

	enum EstatDelJoc {
		Preparat, Jugant, Pausa, GameOver, NivellComplet
	}

	private static final int OBJECTIU_INICIAL = 3;

	EstatDelJoc estat = EstatDelJoc.Preparat;
	Mon mon = new Mon();
	int puntsAntics = 0;
	String puntuacio = "0";

	// Pixmap per guardar el fons del nivell
	Pixmap fons;

	// Nombre de vagonetes de carbó necessaries per completar el nivell
	int objectiu;

	// Controla si la puntuació actual es un record o no
	boolean record;

	public PantallaJoc(Joc joc) {
		super(joc);

		// Establim les vagonetes necessaries per passar al següent nivell
		objectiu = OBJECTIU_INICIAL;

		// Establim el primer
		fons = Recursos.fons[0];

		if (Configuracio.soActivat)
			Recursos.soMusica.play();
		else
			Recursos.soMusica.pausa();
	}

	@Override
	public void mostra(float increment) {

		Grafics g = joc.getGraphics();

		g.dibuixaPixmap(fons, 0, 0);

		// Dibuixem el món
		dibuixaMon(mon);

		if (estat == EstatDelJoc.Preparat)
			dibuixaPreparat();

		if (estat == EstatDelJoc.Jugant)
			dibuixaJugant();

		if (estat == EstatDelJoc.Pausa)
			dibuixaPausa();

		if (estat == EstatDelJoc.GameOver)
			dibuixaGameOver();

		// Si l'estat del joc es nivell complet dibuixarà el missatge
		if (estat == EstatDelJoc.NivellComplet)
			dibuixaNivellComplet();

		// Dibuixem la puntuació centrada en la pantalla. La posició X depèn de
		// la llargada de la puntuació.
		// Si el record es true, els números es dibuixaran en vermell.
		Util.dibuixaText(g, puntuacio, g.getAmple() / 2 - puntuacio.length() * 20 / 2,
				g.getAlt() - 42, record);
	}

	private void dibuixaMon(Mon mon) {
		Grafics g = joc.getGraphics();
		Tren tren = mon.tren;

		// Recorreguem tot el món i dibuixem el que ens trobem
		for (int x = 0; x < Mon.MON_AMPLE; x++) {
			for (int y = 0; y < Mon.MON_ALT; y++) {
				// Si hi ha un element en aquesta posició del món
				if (mon.mapa[x][y] != null) {
					// Dibuixem l'element corresponent. Els decorats no es
					// dibuixan i els vagons es controlen al fer el moviment del
					// tren
					switch (mon.mapa[x][y].tipus) {
					case Element.ELEMENT_CARBO:
						g.dibuixaPixmap(Recursos.carbo, x * 32, y * 32);
						break;

					case Element.ELEMENT_OBSTACLE:
						g.dibuixaPixmap(Recursos.obstacle, x * 32, y * 32);
						break;
					}
				}
			}
		}

		// Locomotora
		Pixmap vagoPixmap = null;

		switch (tren.direccio) {
		case Tren.NORD:
			vagoPixmap = Recursos.trenAdalt;
			break;
		case Tren.SUD:
			vagoPixmap = Recursos.trenAbaix;
			break;
		case Tren.EST:
			vagoPixmap = Recursos.trenDreta;
			break;
		case Tren.OEST:
			vagoPixmap = Recursos.trenEsquerra;
			break;
		}
		g.dibuixaPixmap(vagoPixmap, tren.x * 32, tren.y * 32);

		// Dibuixem els vagons, comencem pel 1 perquè la locomotora ja esta
		// dibuixada
		for (int i = 1, mida = tren.vagons.size(); i < mida; i++) {
			Vago precedent = tren.vagons.get(i - 1);
			Vago vago = tren.vagons.get(i);
			vagoPixmap = colocaVago(vago, precedent, vagoPixmap);
			g.dibuixaPixmap(vagoPixmap, vago.x * 32, vago.y * 32);
		}
	}

	/**
	 * Retorna el pixmap amb la posició en la que s'ha de dibuixar el vago,
	 * segons la posició del vagó precedent. Si el vagó precedent es troba a la
	 * mateixa posició fem servir el valor passat com pixmap per defecte.
	 * 
	 * @param vago
	 *            vagó actual
	 * @param precedent
	 *            vagó precedent
	 * @param pixmap
	 *            pixmap per defecte
	 * @return Pixmap amb la direcció en la que s'ha de dibuixar el vagó
	 */
	private Pixmap colocaVago(Vago vago, Vago precedent, Pixmap pixmap) {

		// Comprovem la nostra posició respecte al vagó precedent.
		if (vago.x > precedent.x) {
			pixmap = Recursos.vagoEsquerra;

		} else if (vago.x < precedent.x) {
			pixmap = Recursos.vagoDreta;

		} else if (vago.y > precedent.y) {
			pixmap = Recursos.vagoAdalt;

		} else if (vago.y < precedent.y) {
			pixmap = Recursos.vagoAbaix;
		}

		// Comprovem si el vagó es troba en un limit de la pantalla, aquesta
		// situació te preferència sobre el comportament normal
		if (vago.x == 0 && precedent.x == Mon.MON_AMPLE - 1) {
			pixmap = Recursos.vagoEsquerra;

		} else if (vago.x == Mon.MON_AMPLE - 1 && precedent.x == 0) {
			pixmap = Recursos.vagoDreta;

		} else if (vago.y == 0 && precedent.y == Mon.MON_ALT - 1) {
			pixmap = Recursos.vagoAdalt;

		} else if (vago.y == Mon.MON_ALT - 1 && precedent.y == 0) {
			pixmap = Recursos.vagoAbaix;
		}

		return pixmap;
	}

	private void dibuixaPreparat() {
		Grafics g = joc.getGraphics();
		g.dibuixaPixmap(Recursos.preparat, 47, 100);
		g.dibuixaPixmap(Recursos.quadreGui, 0, 416); // Afegit el quadre de fons
	}

	private void dibuixaJugant() {
		Grafics g = joc.getGraphics();

		g.dibuixaPixmap(Recursos.botons, 0, 0, 64, 128, 64, 64);
		g.dibuixaPixmap(Recursos.quadreGui, 0, 416); // Afegit el quadre de fons
		g.dibuixaPixmap(Recursos.botons, 0, 416, 64, 64, 64, 64);
		g.dibuixaPixmap(Recursos.botons, 256, 416, 0, 64, 64, 64);
	}

	private void dibuixaPausa() {
		Grafics g = joc.getGraphics();

		g.dibuixaPixmap(Recursos.pausa, 80, 100);
		g.dibuixaPixmap(Recursos.quadreGui, 0, 416); // Afegit el quadre de fons
	}

	private void dibuixaGameOver() {
		Grafics g = joc.getGraphics();
		g.dibuixaPixmap(Recursos.gameOver, 62, 100);
		g.dibuixaPixmap(Recursos.quadreGui, 0, 416); // Afegit el quadre de fons
		g.dibuixaPixmap(Recursos.botons, 128, 200, 0, 128, 64, 64);
	}

	/**
	 * Dibuixa el missatge de nivell completat
	 */
	private void dibuixaNivellComplet() {
		Grafics g = joc.getGraphics();
		g.dibuixaPixmap(Recursos.completat, 47, 100);
		g.dibuixaPixmap(Recursos.quadreGui, 0, 416);
	}

	@Override
	public void actualitza(float increment) {
		List<EventTouch> touchEvents = joc.getInput().getEventsTouch();
		joc.getInput().getEventsTeclat();

		// Depenent de l'estat de la partida
		switch (estat) {
		case Preparat:
			actualitzaPreparat(touchEvents);
			break;

		case Jugant:
			actualitzaJugant(touchEvents, increment);
			break;

		case Pausa:
			actualitzaPausa(touchEvents);
			break;

		case NivellComplet:
			actualitzaNivellComplet(touchEvents);
			break;

		case GameOver:
			actualitzaGameOver(touchEvents);
			break;

		}
	}

	private void actualitzaPreparat(List<EventTouch> touchEvents) {
		// Senzillament si s'ha premut l apantalla comencem a jugar
		if (touchEvents.size() > 0)
			estat = EstatDelJoc.Jugant;
	}

	private void actualitzaPausa(List<EventTouch> touchEvents) {
		int mida = touchEvents.size();

		// Per cada event
		for (int i = 0; i < mida; i++) {
			EventTouch event = touchEvents.get(i);

			if (event.tipus == EventTouch.AIXECAT) {
				if (event.x > 80 && event.x <= 240) {
					// Tornem a jugar
					if (event.y > 100 && event.y <= 148) {
						Recursos.soClick.play(1);
						estat = EstatDelJoc.Jugant;
						return;
					}

					// Tornem al menú principal
					if (event.y > 148 && event.y < 196) {
						// Afegim la puntació al marcador
						Configuracio.afegirMarcador(mon.puntuacio);

						Recursos.soClick.play(1);
						joc.setPantalla(new PantallaMenuPrincipal(joc));
						return;
					}
				}
			}
		}
	}

	private void actualitzaGameOver(List<EventTouch> touchEvents) {
		int mida = touchEvents.size();

		for (int i = 0; i < mida; i++) {
			EventTouch event = touchEvents.get(i);
			if (event.tipus == EventTouch.AIXECAT) {
				if (event.x >= 128 && event.x <= 192 && event.y >= 200 && event.y <= 264) {
					Recursos.soClick.play(1);
					joc.setPantalla(new PantallaMenuPrincipal(joc));
					return;
				}
			}
		}
	}

	private void actualitzaJugant(List<EventTouch> touchEvents, float increment) {
		int mida = touchEvents.size();

		for (int i = 0; i < mida; i++) {
			EventTouch event = touchEvents.get(i);

			if (event.tipus == EventTouch.AIXECAT) {
				// Pulsació al botó de pausa
				if (event.x < 64 && event.y < 64) {
					Recursos.soClick.play(1);
					estat = EstatDelJoc.Pausa;
					return;
				}
			}

			if (event.tipus == EventTouch.PREMUT) {
				// Fletxa esquerra
				if (event.x < 64 && event.y > 416) {
					mon.tren.giraEsquerra();
				}
				// Fletxadreta
				if (event.x > 256 && event.y > 416) {
					mon.tren.giraDreta();
				}
			}
		}

		// Actualitzem l'estat del món deljoc
		mon.actualitza(increment);

		// Hi ha hagut una col·lisió
		if (mon.gameOver) {
			Recursos.soCrash.play(1);
			estat = EstatDelJoc.GameOver;

			// Afegim la puntació al marcador
			Configuracio.afegirMarcador(mon.puntuacio);
		}

		// Ha trobat una vagoneta de carbó
		if (puntsAntics != mon.puntuacio) {

			puntsAntics = mon.puntuacio;
			// Actualitzem l'string
			puntuacio = "" + puntsAntics;

			// Reproduïm el so
			Recursos.soCarbo.play(1);

			// Comprovem si la puntuació actual es un record
			if (!record && Configuracio.esRecord(Integer.parseInt(puntuacio))) {
				this.record = true;
			}

			// Reduïm el nombre de vagonetes necessaries per passar de nivell
			objectiu--;

			// Comprovem si s'ha completat el nivell i si es així canviem
			// l'estat
			if (objectiu == 0) {
				Recursos.soComplet.play(1);
				estat = EstatDelJoc.NivellComplet;
			}
		}
	}

	@Override
	public void pause() {
		if (estat == EstatDelJoc.Jugant)
			estat = EstatDelJoc.Pausa;
	}

	@Override
	public void resume() {

	}

	@Override
	public void llibera() {

	}

	/**
	 * Durant aquest estat esperem que el jugador toque la pantalla, una vegada
	 * ho fa s'incrementa el nivell del mon, es crea un mon buit amb el nivell
	 * actual, es passa la puntuació, s'actualitza el fons i el nombre de
	 * vagonetes necessaries per passar al següent nivell, i finalment es passa
	 * al estat de Preparat.
	 * 
	 * @param touchEvents
	 *            llista amb els tocs de teclat.
	 */
	private void actualitzaNivellComplet(List<EventTouch> touchEvents) {
		int mida = touchEvents.size();

		for (int i = 0; i < mida; i++) {
			EventTouch event = touchEvents.get(i);
			if (event.tipus == EventTouch.AIXECAT) {
				// Actualitzem el nivell i la puntuació
				mon.nivell++;
				mon = new Mon(mon.nivell);
				mon.puntuacio = puntsAntics;

				// Acutalitzem el carbò necessari per passar al següent nivell
				objectiu = 3 + (mon.nivell * 2);

				// Carreguem el nou fons
				int carrega = mon.nivell % Mon.NIVELLS_DIFERENTS;
				fons = Recursos.fons[carrega];

				// Passem a preparat
				estat = EstatDelJoc.Preparat;
			}
		}
	}
}
