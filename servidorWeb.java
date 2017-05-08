/**
 * Universidad de Costa Rica
 * Desarrollo de aplicaciones para internet
 * Tarea programada #1
 * @author (Johan Durán - Kenneth Calvo) 
 * @version (1.0)
 */
import java.net.*;
public class ServidorWeb
{
    int puerto = 80;
    
    void inicia()
    {
        
        try
        {   
            ServerSocket s = new ServerSocket(puerto);
            Bitacora.escribeEncabezados();
            while(true){
                Socket peticion = s.accept();
                Debugger.print("Recibiendo petición");
                PeticionWeb pCliente = new PeticionWeb(peticion);
                pCliente.start();
            }
            
        }
        catch(Exception e)
        {
            Debugger.print("Error en el servidor");
        }
        
    }
    
}



