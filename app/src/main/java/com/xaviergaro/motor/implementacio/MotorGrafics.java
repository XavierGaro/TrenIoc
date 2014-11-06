package com.xaviergaro.motor.implementacio;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.xaviergaro.motor.Grafics;
import com.xaviergaro.motor.Pixmap;

public class MotorGrafics implements Grafics {
	// Per carregar els bitmaps
	AssetManager recursos;
	// framebuffer que contindrà el contingut de la pantalla abans d'enviar-la
	Bitmap frameBuffer;
	// Per poder dibuixar
	Canvas canvas;
	// Les guardem aquí per no haver de crear noves instàncies cada vegada
	// evitem fer new() i GarbageCollector!
	Paint paint;
	Rect rectOrigen = new Rect();
	Rect rectDesti = new Rect();

	// Constructor, inicialitzem els atributs
	public MotorGrafics(AssetManager assets, Bitmap frameBuffer) {
		this.recursos = assets;
		this.frameBuffer = frameBuffer;
		// Creem el canvas perquè dibuixi en el framebuffer (Bitmap)
		// i no per pantalla directament
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	@Override
	// Retorna l'amplada del display (en aquest cas el nostre Bitmap que fa de
	// framebuffer)
	public int getAmple() {
		return frameBuffer.getWidth();
	}

	@Override
	// Retorna l'alçada del display (en aquest cas el nostre Bitmap que fa de
	// framebuffer)
	public int getAlt() {
		return frameBuffer.getHeight();
	}

	@Override
	// Crea un Pixmap. Intenta carregar un Bitmap des del disc
	public Pixmap nouPixmap(String nomFitxer, PixmapFormat format) {
		Config config = null;
		// Transformació entre els formats propis de Pixmap i els d'Android
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;

		// Creem les opcions correctes
		Options options = new Options();
		options.inPreferredConfig = config;
		InputStream in = null;
		Bitmap bitmap = null;
		try {
			// Carreguem el bitmap des del disc
			in = recursos.open(nomFitxer);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null)
				throw new RuntimeException("No es pot carregar gràfic '" + nomFitxer + "'");
		} catch (IOException e) {
			throw new RuntimeException("No es pot carregar gràfic '" + nomFitxer + "'");
		} finally {
			// Tanquem l'InputStream
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		// Obtenim el format després de la conversió que fa el propi Android
		if (bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else
			format = PixmapFormat.ARGB8888;
		// Retornem el Pixamp creat
		return new MotorPixmap(bitmap, format);
	}

	@Override
	// Dibuixa el Pixmap complet
	public void dibuixaPixmap(Pixmap pixmap, int x, int y) {
		canvas.drawBitmap(((MotorPixmap) pixmap).bitmap, x, y, null);
	}

	@Override
	// Dibuixa un Pixmap per pantalla. Permet dibuixar únicament una regió del
	// Pixmap original
	// definit pels seus arguments
	public void dibuixaPixmap(Pixmap pixmap, int x, int y, int origenX, int origenY,
			int origenAmple, int origenAlt) {
		// Creem el rectangle de destí on es dibuixarà
		rectDesti.left = x;
		rectDesti.top = y;
		rectDesti.right = x + origenAmple - 1;
		rectDesti.bottom = y + origenAlt - 1;

		// Creem el rectangle origen del Pixmap a dibuixar
		rectOrigen.left = origenX;
		rectOrigen.top = origenY;
		rectOrigen.right = origenX + origenAmple - 1;
		rectOrigen.bottom = origenY + origenAlt - 1;
		// Dibuixem la part corresponent del Pixmap
		canvas.drawBitmap(((MotorPixmap) pixmap).bitmap, rectOrigen, rectDesti, null);
	}

	@Override
	// Dibuixa un píxel per pantalla
	public void dibuixaPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}

	@Override
	// Dibuixa una línia per pantalla
	public void dibuixaLinia(int origenX, int origenY, int destiX, int destiY, int color) {
		paint.setColor(color);
		canvas.drawLine(origenX, origenY, destiX, destiY, paint);
	}

	@Override
	// Dibuixa un rectangle per pantalla
	public void dibuixaRectangle(int x, int y, int ample, int alt, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + ample - 1, y + alt - 1, paint);
	}

	@Override
	// Pinta tota la pantalla d'un color
	public void clear(int color) {
		// Obtenim les tres components (RGB) del color
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
	}
}