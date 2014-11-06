package com.ioc.motor.implementacio;
import java.util.List;

import android.view.View.OnTouchListener;

import com.ioc.motor.Input.EventTouch;
 
//Interfície del handler de touch.
public interface TouchHandler extends OnTouchListener
{
//Si està premuda la pantalla
public boolean pantallaPremuda(int punter);
 
//Coordenades on s'ha premut
public int getTouchX(int punter);
public int getTouchY(int punter);
 
//Obtenir els events de touch
public List<EventTouch> getEventsTouch();
}