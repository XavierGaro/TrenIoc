package com.ioc.motor;
 
public interface Musica
{
//Control dereproducció
public void play();
public void stop();
public void pausa();
 
public void setBucle(boolean bucle);
public void setVolum(float volum);
 
//Per comprovar l'estat de la instància de música
public boolean isPlaying();
public boolean isStopped();
public boolean isBucle();
 
public void llibera ();
}