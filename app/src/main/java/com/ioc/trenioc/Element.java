package com.ioc.trenioc;

//Aquesta classer epresenta la superclasse de cada element que pot estar al mapa
public abstract class Element {
	public static final int ELEMENT_CARBO = 0;
	public static final int ELEMENT_OBSTACLE = 1;
	public static final int ELEMENT_VAGO = 2;
	public static final int ELEMENT_DECORAT = 3; // Afegit l'element decorat

	public int x;
	public int y;
	public int tipus;

	public Element(int x, int y, int tipus) {
		this.x = x;
		this.y = y;
		this.tipus = tipus;
	}
}