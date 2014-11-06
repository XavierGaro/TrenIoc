package com.xaviergaro.motor.implementacio;
import android.graphics.Bitmap;

import com.xaviergaro.motor.Grafics.PixmapFormat;
import com.xaviergaro.motor.Pixmap;
 
public class MotorPixmap implements Pixmap
{
//Bitmap que conté els gràfics
Bitmap bitmap;
 
//Format del bitmap
PixmapFormat format;
 
//Constructor
public MotorPixmap(Bitmap bitmap, PixmapFormat format)
{
this.bitmap = bitmap;
this.format = format;
}
@Override
public PixmapFormat getFormat()
{
return format;
}
@Override
public int getAmple()
{
return bitmap.getWidth();
}
@Override
public int getAlt()
{
return bitmap.getHeight();
}
@Override
public void llibera()
{
bitmap.recycle();
}
}