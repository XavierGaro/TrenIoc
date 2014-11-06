package com.ioc.motor;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
//Interfície d'entrada i sortida de fitxers
public interface FileIO
{
//Llegir un recurs de l'aplicació
public InputStream llegirRecurs(String nomFitxer) throws IOException;
 
//Llegir o escriure un fitxer de la memòria SD
public InputStream llegirFitxer(String nomFitxer) throws IOException;
public OutputStream escriureFitxer(String nomFitxer) throws IOException;
}