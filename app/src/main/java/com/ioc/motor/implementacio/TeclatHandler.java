package com.ioc.motor.implementacio;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

import com.ioc.motor.Input.EventTeclat;
import com.ioc.motor.implementacio.Fons.FonsObjectes;

//Implementa OnKeyListener per obtenir els events de View
public class TeclatHandler implements OnKeyListener {
	// Guardem de cada tecla si està premuda o no. Cada posició correspon
	// al codi de la tecla
	boolean[] teclesPremudes = new boolean[128];

	// Fons d'instàncies d'events de teclat
	Fons<EventTeclat> fonsEvents;

	// Aquí guardarem els events que encara no han estat processats
	List<EventTeclat> bufferEvents = new ArrayList<EventTeclat>();

	// Aquí guardarem els events que retornem amb getEventsTeclat
	List<EventTeclat> eventsTeclat = new ArrayList<EventTeclat>();

	// Constructor, rep la vista des de la qual obtindrem els events de teclat
	public TeclatHandler(View view) {
		// Fons per reciclar les instàncies d'events de teclat
		FonsObjectes<EventTeclat> creador = new FonsObjectes<EventTeclat>() {

			@Override
			public EventTeclat crearObjecte() {
				return new EventTeclat();
			}
		};

		fonsEvents = new Fons<EventTeclat>(creador, 100);

		// Registrem el Listener
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}

	@Override
	// Implementació del'OnKeyListener.onKey
	// Creem un event de teclat vàlid i l'afegim al buffer d'events de teclat
	public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
		// Ignorem aquest tipus d'events d'Andoid
		if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
			return false;

		synchronized (this) {
			// Obtenim un event de teclat (nou o reciclat)
			EventTeclat eventTecla = fonsEvents.objecteNou();

			// Omplim les dades de l'event de teclat
			eventTecla.codiTecla = keyCode;
			eventTecla.caracterTecla = (char) event.getUnicodeChar();

			// Tecla premuda
			if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				// Indiquem si l'event de teclat ha sigut de premut
				eventTecla.tipus = EventTeclat.KEY_DOWN;

				// Actualitzem l'array de tecles premudes
				if (keyCode > 0 && keyCode < 127)
					teclesPremudes[keyCode] = true;
			}

			// Tecla aixecada
			if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
				// Indiquem si l'event de teclat ha sigut d'aixecat
				eventTecla.tipus = EventTeclat.KEY_UP;

				// Actualitzem l'array de tecles premudes
				if (keyCode > 0 && keyCode < 127)
					teclesPremudes[keyCode] = false;
			}

			// Afegim l'event de teclat al buffer d'events
			bufferEvents.add(eventTecla);
		}
		return false;
	}

	// Ens retorna si una tecla en concret està premuda o no
	public boolean teclaPremuda(int codiTecla) {
		// Tecla fora de l'array
		if (codiTecla < 0 || codiTecla > 127)
			return false;
		// retornem el seu valor
		return teclesPremudes[codiTecla];
	}

	// Obtenim una llista dels events de teclat que han ocorregut
	public List<EventTeclat> getEventsTeclat() {
		// Es cridarà des d'un fil diferent
		synchronized (this) {
			int mida = eventsTeclat.size();

			// Alliberem la llista d'events de teclat
			for (int i = 0; i < mida; i++)
				fonsEvents.llibera(eventsTeclat.get(i));
			eventsTeclat.clear();

			// i l'omplim amb els events del buffer
			eventsTeclat.addAll(bufferEvents);

			// i buidem el buffer
			bufferEvents.clear();
			return eventsTeclat;
		}
	}
}