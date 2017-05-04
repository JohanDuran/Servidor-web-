
/**
 * Write a description of class Bitacora here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Bitacora
{
    
    public static boolean escribe(String requestMethod, String timestamp,String server, String referer, String requestPath,String parametros){
        try(FileWriter fw = new FileWriter("bitacora.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
            
        {
            out.println(requestMethod+"\t"+timestamp+"\t"+server+"\t"+referer+"\t"+requestPath+"\t"+parametros+"\n");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
