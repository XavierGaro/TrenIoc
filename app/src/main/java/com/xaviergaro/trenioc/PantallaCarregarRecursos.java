package com.xaviergaro.trenioc;

import com.xaviergaro.motor.Grafics;
import com.xaviergaro.motor.Grafics.PixmapFormat;
import com.xaviergaro.motor.Joc;
import com.xaviergaro.motor.Pantalla;
import com.xaviergaro.motor.Pixmap;

public class PantallaCarregarRecursos extends Pantalla {
	public PantallaCarregarRecursos(Joc joc) {
		super(joc);
	}

	@Override
	public void actualitza(float temps) {
		Grafics g = joc.getGraphics();
		Recursos.logo = g.nouPixmap("grafics/logo.png", PixmapFormat.ARGB4444);
		Recursos.menuPrincipal = g.nouPixmap("grafics/menuprincipal.png",
				PixmapFormat.ARGB4444);
		Recursos.botons = g.nouPixmap("grafics/botons.png", PixmapFormat.ARGB4444);
		Recursos.ajuda1 = g.nouPixmap("grafics/ajuda1.png", PixmapFormat.ARGB4444);
		Recursos.ajuda2 = g.nouPixmap("grafics/ajuda2.png", PixmapFormat.ARGB4444);
		Recursos.ajuda3 = g.nouPixmap("grafics/ajuda3.png", PixmapFormat.ARGB4444);
		Recursos.ajuda4 = g.nouPixmap("grafics/ajuda4.png", PixmapFormat.ARGB4444);
		Recursos.numeros = g.nouPixmap("grafics/numeros.png", PixmapFormat.ARGB4444);
		Recursos.preparat = g.nouPixmap("grafics/preparat.png", PixmapFormat.ARGB4444);
		Recursos.pausa = g.nouPixmap("grafics/menupausa.png", PixmapFormat.ARGB4444);
		Recursos.gameOver = g.nouPixmap("grafics/gameover.png", PixmapFormat.ARGB4444);
		Recursos.trenAdalt = g.nouPixmap("grafics/trenadalt.png", PixmapFormat.ARGB4444);
		Recursos.trenEsquerra = g.nouPixmap("grafics/trenesquerra.png", PixmapFormat.ARGB4444);
		Recursos.trenAbaix = g.nouPixmap("grafics/trenabaix.png", PixmapFormat.ARGB4444);
		Recursos.trenDreta = g.nouPixmap("grafics/trendreta.png", PixmapFormat.ARGB4444);
		Recursos.obstacle = g.nouPixmap("grafics/obstacle.png", PixmapFormat.ARGB4444);
		Recursos.carbo = g.nouPixmap("grafics/carbo.png", PixmapFormat.ARGB4444);

		// Afegim les imatges dels vagons, fem servir la mateixa imatge pel vagò a dreta i esquerra
		Recursos.vagoAdalt = g.nouPixmap("grafics/vagoadalt.png", PixmapFormat.ARGB4444);
		Recursos.vagoAbaix = g.nouPixmap("grafics/vagoabaix.png", PixmapFormat.ARGB4444);
		Recursos.vagoDreta = g.nouPixmap("grafics/vagohoritzontal.png", PixmapFormat.ARGB4444);
		Recursos.vagoEsquerra = g.nouPixmap("grafics/vagohoritzontal.png",
				PixmapFormat.ARGB4444);

		// Afegim el fons del títol i les pantalles del menú principal 
		Recursos.fonsTitol = g.nouPixmap("grafics/fonstitol.png", PixmapFormat.RGB565);
		
		// Afegim els quadres semitransparents
		Recursos.quadreAjuda = g.nouPixmap("grafics/cuadreAjuda.png", PixmapFormat.ARGB4444);
		Recursos.quadreRecord = g.nouPixmap("grafics/cuadreRecord.png", PixmapFormat.ARGB4444);
		Recursos.quadreGui = g.nouPixmap("grafics/quadreGui.png", PixmapFormat.ARGB4444);

		// Afegim el missatge de nivell completat
		Recursos.completat = g.nouPixmap("grafics/completat.png", PixmapFormat.ARGB4444);

		// Afegim els números vermells
		Recursos.numerosVermell = g.nouPixmap("grafics/numerosvermells.png",
				PixmapFormat.ARGB4444);

		// Afegim els fons dels nivells en un array
		Recursos.fons = new Pixmap[Mon.NIVELLS_DIFERENTS];
		Recursos.fons[0] = g.nouPixmap("grafics/fons1.png", PixmapFormat.RGB565);
		Recursos.fons[1] = g.nouPixmap("grafics/fons2.png", PixmapFormat.RGB565);
		Recursos.fons[2] = g.nouPixmap("grafics/fons3.png", PixmapFormat.RGB565);
		Recursos.fons[3] = g.nouPixmap("grafics/fons4.png", PixmapFormat.RGB565);
		Recursos.fons[4] = g.nouPixmap("grafics/fons5.png", PixmapFormat.RGB565);
		Recursos.fons[5] = g.nouPixmap("grafics/fons6.png", PixmapFormat.RGB565);

		Recursos.soClick = joc.getAudio().nouSo("audio/click.wav");

		/**
		 * Títol: Train Whistle Llicencia: Gratuït per us no comercial URL:
		 * http://www.soundjay.com/train-sound-effect.html
		 */
		Recursos.soCarbo = joc.getAudio().nouSo("audio/train-whistle-01.mp3");

		/**
		 * Títol: Crash Large Llicencia: Public domain URL:
		 * http://soundbible.com/1172-Crash-Large.html
		 */
		Recursos.soCrash = joc.getAudio().nouSo("audio/crash_large.mp3");

		/**
		 * Títol: Crowds - Applause, Cheers & Whistles Llicencia: Free preview
		 * URL:
		 * http://sounddogs.com/sound-effects/25/mp3/482033_SOUNDDOGS__cr.mp3
		 */
		Recursos.soComplet = joc.getAudio().nouSo("audio/crowd.mp3");

		/**
		 * Títol: 303 vs 909 Autor: canton Llicencia: Creative Commons
		 * Attribution, Non-commercial, No derivatives URL:
		 * http://sampleswap.org/viewtopic.php?t=983
		 */
		Recursos.soMusica = joc.getAudio().nouMusica("audio/canton_303-vs-909-160.mp3");
		Recursos.soMusica.setBucle(true);

		Configuracio.carregaConfiguracio(joc.getFileIO());

		// Activem la configuració de so
		com.xaviergaro.motor.implementacio.MotorSo.soActivat = Configuracio.soActivat;
		com.xaviergaro.motor.implementacio.MotorMusica.musicaActivada = Configuracio.soActivat;

		joc.setPantalla(new PantallaMenuPrincipal(joc));
	}

	@Override
	public void mostra(float temps) {
		// Es podria mostrar el logo del joc o carregant aquí
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void llibera() {
	}
}