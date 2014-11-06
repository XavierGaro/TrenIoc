package com.xaviergaro.motor.implementacio;

import java.util.ArrayList;
import java.util.List;

public class Fons<T> {

	public interface FonsObjectes<T> {
		public T crearObjecte();
	}

	// Llista d'objectes creats llestos per fer servir
	private final List<T> llistaDisponibles;

	// Escrea per generar noves instàncies del tipus de la classe
	private final FonsObjectes<T> creador;

	// Mida màxima del fons
	private final int midaMaxima;

	// Constructor
	public Fons(FonsObjectes<T> creador, int midaMaxima) {

		this.creador = creador;
		this.midaMaxima = midaMaxima;
		this.llistaDisponibles = new ArrayList<T>(midaMaxima);

	}

	// Crea un nou objecte
	public T objecteNou() {
		T objecte = null;
		
		// Si no hi ha cap objecte disponible el creem
		if (llistaDisponibles.size() == 0)
			objecte = creador.crearObjecte();
		else
			// Sino reciclem un dels objectes de la llista
			objecte = llistaDisponibles.remove(llistaDisponibles.size() - 1);

		return objecte;
	}

	public void llibera(T object) {

		// Quan alliberem un objecte, l'afegim a la llista de disponibles
		if (llistaDisponibles.size() < midaMaxima)
			llistaDisponibles.add(object);
	}

}