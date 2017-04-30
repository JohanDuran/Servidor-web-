/*Ejemplo de Servidor Web en Java
 *
 * Autor: Johan Durán Kenneth Calvo
 *
 **/


import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorWeb
{
    int puerto = 90;
    
    final int ERROR = 0;
    final int WARNING = 1;
    final int DEBUG = 2;
    
        
    // funcion para centralizar los mensajes de depuracion

    void depura(String mensaje)
    {
        depura(mensaje,DEBUG);
    }   

    void depura(String mensaje, int gravedad)
    {
        System.out.println("Mensaje: " + mensaje);
    }   
        
    
    boolean arranca()
    {
        depura("Arrancamos nuestro servidor",DEBUG);
        
        try
        {
        
            
            ServerSocket s = new ServerSocket(90);

            depura("Quedamos a la espera de conexion");
            
            while(true)  // bucle infinito .... ya veremos como hacerlo de otro modo
            {
                Socket peticion = s.accept();
                PeticionWeb pCliente = new PeticionWeb(peticion);
                pCliente.start();
            }
            
        }
        catch(Exception e)
        {
            depura("Error en servidor\n" + e.toString());
        }
        
        return true;
    }
    
}



