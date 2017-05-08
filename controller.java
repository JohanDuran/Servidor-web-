/**
 * Universidad de Costa Rica
 * Desarrollo de aplicaciones para internet
 * Tarea programada #1
 * @author (Johan Durán - Kenneth Calvo) 
 * @version (1.0)
 */
public class Controller
{

    public static void main(){
        Debbuger.print("iniciando el servidor web - ECCI UCR");
        ServidorWeb server = new ServidorWeb();
        server.inicia();
    }
}
