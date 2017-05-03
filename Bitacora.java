
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
    // instance variables - replace the example below with your own

    /**
     * Constructor for objects of class Bitacora
     */
    public Bitacora()
    {

    }
    
    public boolean escribe(String requestMethod, String requestPath,String parametros, String contentLength,String referer){
        try(FileWriter fw = new FileWriter("bitacora.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
            
        {
            out.println(requestMethod+"\t"+requestPath+"\t"+parametros+"\t"+contentLength+"\t"+referer+"\n");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
