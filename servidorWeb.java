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
    int puerto = 80;
    
    boolean arranca()
    {

        
        try
        {   
            ServerSocket s = new ServerSocket(puerto);
            Bitacora.escribe("requestMethod\t", "timestamp\t","server\t", "referer\t", "URL\t","parametros\t");
            while(true)  // bucle infinito .... ya veremos como hacerlo de otro modo
            {
                Socket peticion = s.accept();
                PeticionWeb pCliente = new PeticionWeb(peticion);
                pCliente.start();
            }
            
        }
        catch(Exception e)
        {
        }
        
        return true;
    }
    
}



