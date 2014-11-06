package com.xaviergaro.trenioc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

//Representació lògica del món del joc
public class Mon {
	static final int MON_AMPLE = 10;
	static final int MON_ALT = 13;
	static final int INCREMENT_PUNTUACIO = 10;
	static final float TICK_INICIAL = 0.5f;
	static final float TICK_DECREMENT = 0.01f;

	static final int TREN_INIT_X = 5;
	static final int TREN_INIT_Y = 7;

	// Nombre total de nivells al joc
	static final int NIVELLS_DIFERENTS = 6;

	public Tren tren;
	public boolean gameOver = false;
	public int puntuacio = 0;

	// Nivell actual
	public int nivell;

	Element mapa[][];
	Random random = new Random();
	float tempsAcumulat = 0;

	// Cada quant es computa un frame
	static float tick = TICK_INICIAL;

	/**
	 * Inicia el mon al nivell 0
	 */
	public Mon() {
		this(0);
	}

	/**
	 * Inicia el mon al nivell passat com argument
	 * 
	 * @param nivell
	 */
	public Mon(int nivell) {
		this.nivell = nivell;

		// Creem el món buit
		mapa = new Element[MON_AMPLE][MON_ALT];

		// Creem el tren
		tren = new Tren(TREN_INIT_X, TREN_INIT_Y);

		// Introduïem el tren i els vagons al mapa per assegurar-nos que no es
		// crea el carbó o l'obscatle nou en la posició del tren
		mapa[TREN_INIT_X][TREN_INIT_Y] = tren;
		for (int i = 0, mida = tren.vagons.size(); i < mida; i++) {
			Vago vago = tren.vagons.get(i);
			mapa[vago.x][vago.y] = tren;
		}

		// Carreguem les dades del nivell
		carregaNivell();

		// Situem una primera vagoneta
		situaElement(Element.ELEMENT_CARBO);

		tick = TICK_INICIAL;
	}

	// Fica un vagoneta de carbó o obstacle aleatòriament al mapa
	private void situaElement(int tipus) {
		int posX;
		int posY;

		// Cerquem si és possible
		while (true) {
			posX = random.nextInt(MON_AMPLE);
			posY = random.nextInt(MON_ALT);

			// Si la posició del mapa està buida
			if (mapa[posX][posY] == null) {
				// Mirem que no estigui massa aprop del cotxe
				// Si està al menys a 4 posicions (distància de Manhattan)
				if ((Math.abs(tren.x - posX) + Math.abs(tren.y - posY)) > 4)
					break;
			}
		}

		// Creem l'element
		if (tipus == Element.ELEMENT_CARBO) {
			// Creem la vagoneta de carbó i la situem al mapa
			mapa[posX][posY] = new Carbo(posX, posY);
		} else {
			// Creem obstacle
			mapa[posX][posY] = new Obstacle(posX, posY);
		}
	}

	// Actualitzem el mapa
	public void actualitza(float increment) {
		int cotxeAnticX;
		int cotxeAnticY;
		int cotxeNouX;
		int cotxeNouY;

		// Si s'ha acabat el joc no hi ha res a actualitzar!
		if (gameOver)
			return;

		tempsAcumulat += increment;

		while (tempsAcumulat > tick) {
			tempsAcumulat -= tick;

			// Guardem on és el vagó de cua
			Vago cua = tren.vagons.get(tren.vagons.size() - 1);
			cotxeAnticX = cua.x;
			cotxeAnticY = cua.y;

			// Avancem el tren
			tren.avanca();

			// Comprovem si ha xocat amb algun element
			if (mapa[tren.x][tren.y] != null) {

				// Amb què ha xocat el cotxe?
				switch (mapa[tren.x][tren.y].tipus) {
				case Element.ELEMENT_CARBO:
					puntuacio += INCREMENT_PUNTUACIO;

					// Eliminem aquesta vagoneta de carbó
					mapa[tren.x][tren.y] = null;
					situaElement(Element.ELEMENT_CARBO);
					situaElement(Element.ELEMENT_OBSTACLE);

					// S'afegeix un vagó
					tren.afegirVago();

					// Incrementem la velocitat. Decrementem el temps de tick,
					// per tant el joc anirà més ràpid
					tick -= TICK_DECREMENT;
					break;

				case Element.ELEMENT_DECORAT:
				case Element.ELEMENT_OBSTACLE:
				case Element.ELEMENT_VAGO:
					// Si es xoca amb un obstacle, element del decorat o un vagó
					// acaba la partida
					gameOver = true;
					return;
				}
			}

			// Guardem la posició del vagó de cua actual, que pot ser diferent
			// si s'ha afegit algun
			Vago cuaNou = tren.vagons.get(tren.vagons.size() - 1);
			cotxeNouX = cuaNou.x;
			cotxeNouY = cuaNou.y;

			// Si la cua s'ha mogut, s'allibera l'espai anterior del mapa
			if (cotxeNouX != cotxeAnticX || cotxeNouY != cotxeAnticY) {
				mapa[cotxeAnticX][cotxeAnticY] = null;
			}

			// Actualitzem la posició a la que es troba el tren, que correspon a
			// la locomotora.
			mapa[tren.x][tren.y] = tren;
		}
	}

	/**
	 * Carrega les dades del nivell actual desde el directori /res/raw/
	 */
	private void carregaNivell() {
		// Com que fem servir el mòdul una vegada s'acaben els mapes nous tornem
		// a començar
		int carregar = nivell % NIVELLS_DIFERENTS;

		// Creem el objecte decorat que farem servir per marcar tot el mapa
		Element decorat = new Decorat();

		// Obrim el flux de dades del fitxer corresponent al nivell.
		String file = "res/raw/nivell" + (carregar + 1) + ".map";
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		try {
			// Ens asegurem que la primera línia conté alguna cosa
			String line = reader.readLine();

			while (line != null) {
				// Obtenim les coordenades dels obstacles
				int coords[] = trencaCadena(line);
				int x = coords[0];
				int y = coords[1];

				// Afegim el element al mapa
				mapa[x][y] = decorat;

				// Llegim la següent línia
				line = reader.readLine();
			}

		} catch (IOException e) {
			// Si hi ha cap error ho mostrem al log
			e.printStackTrace();

		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// No fem res
			}
		}
	}

	/**
	 * Trenca la línea pasada com argument i retorna un array de dos posicions
	 * amb les coordenades estretes.
	 * 
	 * @param line
	 *            cadena amb les coordenades separades per una coma. El format
	 *            esperat es "x,y" sense espais
	 * @return array amb la coordenada x al index 0 i la coordenada y al index 1
	 */
	private int[] trencaCadena(String line) {
		String cadenes[] = line.split(",");
		int[] coordenades = new int[2];
		coordenades[0] = Integer.parseInt(cadenes[0]);
		coordenades[1] = Integer.parseInt(cadenes[1]);
		return coordenades;
	}
}