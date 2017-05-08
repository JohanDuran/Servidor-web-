/**
 * Universidad de Costa Rica
 * Desarrollo de aplicaciones para internet
 * Tarea programada #1
 * @author (Johan Durán - Kenneth Calvo) 
 * @version (1.0)
 */
import java.io.*;

public class Bitacora
{
    public static void escribeEncabezados(){
        BufferedWriter writer =null;
        try{
            String outputText=String.format("%20s %20s %20s %20s %20s %20s \r\n","requestMethod", "timestamp","server", "referer", "URL","parametros");
            File file = new File("bitacora.txt");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(outputText);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {}
        }

    }
    
    public static boolean escribe(String requestMethod, String timestamp,String server, String referer, String requestPath,String parametros){
        Debugger.print("Imprimiendo en Bitacora");
        String outputText=String.format("%20s %20s %20s %20s %20s %20s \r\n",requestMethod,timestamp,server,referer,requestPath,parametros);
        try(FileWriter fw = new FileWriter("bitacora.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(outputText);
        } catch (IOException e) {
            Debugger.print("Error al imprimir en bitacora");
            return false;
        }
        Debugger.print("Impresión exitosa en bitacora");
        return true;
    }

}
