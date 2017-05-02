
/**
 * Write a description of class Bitacora here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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
        /*try{
            String input = requestMethod+"\t"+requestPath+"\t"+parametros+"\t"+contentLength+"\t"+referer+"\n";
            System.out.println("Input string: " + input);
            ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
            
            String filePath = "bitacora2.txt";
            Path path = Paths.get(filePath);
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE,
                StandardOpenOption.APPEND);
            System.out.println("File channel opened for write. Acquiring lock...");
            fileChannel.position(fileChannel.size() - 1); // positions at the end of file
            
            FileLock lock = fileChannel.lock(); // gets an exclusive lock
            System.out.println("Lock is shared: " + lock.isShared());
            
            fileChannel.write(buffer);
            fileChannel.close(); // also releases lock
            System.out.println("Write complete. Closing the channel and releasing lock.");
            
        }catch(Exception e){
            return false;
        }

        return true;*/
            
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
