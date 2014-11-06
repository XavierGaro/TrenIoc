package com.ioc.motor.implementacio;
 
import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.ioc.motor.Input.EventTouch;
import com.ioc.motor.implementacio.Fons.FonsObjectes;
 
public class SimpleTouch implements TouchHandler
{
//Informació sobre l'event de pulsació
boolean tocada;
int posX;
int posY;
 
//Fons d'instàncies d'events de pulsació
Fons<EventTouch> fonsEvents;
 
//Aquí guardem els events que encara no han estat processats
List<EventTouch> bufferEvents = new ArrayList<EventTouch>();
 
//Aquí guardem els events que retornem amb getEventsTouch
List<EventTouch> eventsTouch = new ArrayList<EventTouch>();
 
//Factors d'escalat per a múltiples resolucions
float escalaX;
float escalaY;
 
//Constructor, rep la vista des de la qual obtindrem els events de touch
//i el factor d'escala
public SimpleTouch(View view, float escalatX, float escalatY)
{
//Fons per reciclar les instàncies d'events de teclat
FonsObjectes<EventTouch> factory = new FonsObjectes<EventTouch>() {
@Override
public EventTouch crearObjecte() {
return new EventTouch();
}
};
fonsEvents = new Fons<EventTouch>(factory, 100);
 
//Registrem el listener
view.setOnTouchListener(this);
 
this.escalaX = escalatX;
this.escalaY = escalatY;
}
 
@Override
//Implementació d'OnTouch
public boolean onTouch(View v, MotionEvent event)
{
//Sincronitzem l'execució dels diferents fils
synchronized(this)
{
//Obtenim un objecte del fons
EventTouch touchEvent = fonsEvents.objecteNou();
 
//Creem l'event de teclat personalitzat adequadament
//Quina acció ha ocorregut?
switch (event.getAction())
{
//Pantalla premuda
case MotionEvent.ACTION_DOWN:
touchEvent.tipus = EventTouch.PREMUT;
tocada = true;
break;
//Aixecada
case MotionEvent.ACTION_CANCEL:
case MotionEvent.ACTION_UP:
touchEvent.tipus = EventTouch.AIXECAT;
tocada = false;
break;
//Premut i mogut = arrossegat
case MotionEvent.ACTION_MOVE:
touchEvent.tipus = EventTouch.ARROSEGAT;
tocada = true;
break;
}
 
//Obtenim les posicions escalades
posX = (int)(event.getX() * escalaX);
posY = (int)(event.getY() * escalaY);
 
//i les assignem a l'event
touchEvent.x = posX;
touchEvent.y = posY;
 
//Afegim l'event al buffer d'events per processar
bufferEvents.add(touchEvent);
 
return true;
}
}
 
 
@Override
//Comprova si la pantalla està premuda
public boolean pantallaPremuda(int punter)
{
synchronized(this)
{
//al ser SimpleTouch, ignorem el punter
return tocada;
}
}
 
@Override
//Obtenim una llista dels events de touch que han ocorregut
public List<EventTouch> getEventsTouch()
{
//Sincronitzem el fil
synchronized(this)
{
int mida = eventsTouch.size();
 
//Alliberem la llista d'events de touch
for( int i = 0; i < mida; i++ )
fonsEvents.llibera(eventsTouch.get(i));
eventsTouch.clear();
 
//i l'omplim amb els events del buffer
eventsTouch.addAll(bufferEvents);
 
//buidem el buffer
bufferEvents.clear();
 
return eventsTouch;
}
}
 
@Override
//Posició X del punter
public int getTouchX(int pointer)
{
synchronized(this)
{
return posX;
}
}
 
@Override
//Posició Y del punter
public int getTouchY(int pointer)
{
synchronized(this)
{
return posY;
}
}
}