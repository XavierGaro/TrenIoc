package com.xaviergaro.trenioc;

import java.util.ArrayList;
import java.util.List;

public class Tren extends Element {
	// Ordenades en el sentit de les agulles del rellotge, perquè sigui més
	// fàcil girar
	public static final int NORD = 0;
	public static final int EST = 1;
	public static final int SUD = 2;
	public static final int OEST = 3;

	public int direccio;
	public boolean potGirar = true;

	// Llista de vagons
	public List<Vago> vagons = new ArrayList<Vago>();

	public Tren(int x, int y) {
		super(x, y, ELEMENT_VAGO);
		direccio = NORD;

		// Afegim la locomotora i dos vagons
		vagons.add(new Vago(x, y));
		vagons.add(new Vago(x, y + 1));
		vagons.add(new Vago(x, y + 2));
	}

	// Avança el tren en la direcció en què està apuntant
	public void avanca() {

		// Avancem una casella en la direcció en què apunta el tren
		switch (direccio) {
		case NORD:
			y--;
			break;
		case SUD:
			y++;
			break;
		case EST:
			x++;
			break;
		case OEST:
			x--;
			break;
		}

		// Si sortim del mapa, tornem a entrar per l'altre costat
		if (x < 0)
			x = 9;
		if (x > 9)
			x = 0;
		if (y < 0)
			y = 12;
		if (y > 12)
			y = 0;

		// Arroseguem els vagons
		arrosega();

		// Despres d'avançar una casella pot tornar a girar
		potGirar = true;
	}

	// Modifiquem la direcció del tren
	public void giraEsquerra() {
		// Només girem si es possible girar, i evitem que es pugui tornar a
		// girar fins que avancem
		if (potGirar) {
			direccio--;
			potGirar = false;
			if (direccio < NORD)
				direccio = OEST;
		}

	}

	// Modifiquem la direcció del tren
	public void giraDreta() {
		// Només girem si es possible girar, i evitem que es pugui tornar a
		// girar fins que avancem
		if (potGirar) {
			direccio++;
			potGirar = false;
			if (direccio > OEST)
				direccio = NORD;
		}
	}

	/**
	 * Afegeix un vagó a la posició de cua
	 */
	public void afegirVago() {
		Vago cua = vagons.get(vagons.size() - 1);
		vagons.add(new Vago(cua.x, cua.y));
	}

	/**
	 * Mou tots els vagons a la posició que ocupava el vagó precedent i
	 * finalment mou la locomotora a la nova posició del tren.
	 */
	private void arrosega() {
		Vago locomotora = vagons.get(0);

		int mida = vagons.size() - 1;

		for (int i = mida; i > 0; i--) {
			Vago anterior = vagons.get(i - 1);
			Vago actual = vagons.get(i);
			actual.x = anterior.x;
			actual.y = anterior.y;
		}

		locomotora.x = x;
		locomotora.y = y;
	}
}