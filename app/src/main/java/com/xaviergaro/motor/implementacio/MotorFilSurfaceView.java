package com.xaviergaro.motor.implementacio;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MotorFilSurfaceView extends SurfaceView implements Runnable {

	// La instància del joc
	MotorJoc joc;

	// Bitmap que farem servir de framebuffer abans de dibuixar per pantalla
	Bitmap framebuffer;
	Thread filDibuixat = null;
	SurfaceHolder holder;
	volatile boolean executant = false;

	// Constructor
	public MotorFilSurfaceView(MotorJoc game, Bitmap framebuffer) {
		super(game);
		// Inicialitzem els atributs
		this.joc = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}

	@Override
	public void run() {
		Rect rectDesti = new Rect();
		// Obtenim el temps actual (abans d'executar el frame)
		// en nanosegons
		long temps = System.nanoTime();
		// Si el fil de dibuixat està en execució
		while (executant) {
			if (!holder.getSurface().isValid())
				continue;
			// Calculem el temps que ha passat i el convertim a segons
			float deltaTime = (System.nanoTime() - temps) / 1000000000.0f;
			// tornem a agafar el temps per calcular el següent frame
			temps = System.nanoTime();
			// Actualitzem el món del joc (li passem el temps transcorregut)
			joc.getPantallaActual().actualitza(deltaTime);
			// Dibuixem el frame del joc al framebuffer
			joc.getPantallaActual().mostra(deltaTime);
			Canvas canvas = holder.lockCanvas();
			// Guardem al rectangle destí l'extensió del Canvas
			canvas.getClipBounds(rectDesti);
			// Dibuixem el frame buffer al canvas
			canvas.drawBitmap(framebuffer, null, rectDesti, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void pause() {
		// Si s'atura l'aplicació
		executant = false;
		// Esperem que mori el fil de dibuixat
		while (true) {
			try {
				filDibuixat.join();
				break;
			} catch (InterruptedException e) {
			}
		}
	}

	public void resume() {
		// Retornem a executar el fil de dibuixat
		executant = true;
		filDibuixat = new Thread(this);
		filDibuixat.start();
	}
}