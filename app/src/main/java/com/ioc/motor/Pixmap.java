package com.ioc.motor;

import com.ioc.motor.Grafics.PixmapFormat;

public interface Pixmap {
	// Ample i altdelPixmap
	public int getAmple();

	public int getAlt();

	// Format delpixmap
	public PixmapFormat getFormat();

	// Allibera memòria
	public void llibera();
}