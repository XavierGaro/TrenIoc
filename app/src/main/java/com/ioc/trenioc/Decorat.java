package com.ioc.trenioc;

/**
 * El Decorat es un element invisible amb les seves coordeandes fora de la
 * graella de lloc. Només cal crear un per tenir una refrenncia a aquest tipus
 * d'objecte al mapa. Les casellas amb aquest tipus d'element es consideran
 * obstacles. En lloc de dibuixar-se el que es fa amb aquest element es marcar
 * les caselles que corresponen a obstacles a la imatge de fons (muntanyes,
 * boscos, etc.)
 * 
 * @author Xavier García
 * 
 */
public class Decorat extends Element {
	public Decorat() {
		super(-1, -1, ELEMENT_DECORAT);
	}
}
