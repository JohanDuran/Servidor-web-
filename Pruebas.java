
/**
 * Write a description of class Pruebas here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.*;
import java.net.*;
import java.util.*;
public class Pruebas
{
    public static void main(String args){
            String [] tipos={"x-world", "x-world", "application", "application", "application", "application", "text", "text", "video", "application", "audio", "audio", "audio", "audio", "audio", "audio", "application", "text", "application", "application", "application", "application", "application", "application", "image", "video", "text", "text", "application", "video", "video", "audio", "audio", "application", "video", "video", "video", "video", "application", "application", "application", "application", "application", "application", "image", "image", "image", "application", "application", "application", "application", "application", "application", "text", "text", "text", "application", "text", "text", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "text", "text", "application", "text", "application", "application", "application", "application", "application", "application", "application", "application", "application", "text", "application", "text", "text", "application", "application", "text", "application", "video", "application", "video", "video", "application", "application", "application", "application", "application", "video", "application", "drawing", "model", "application", "image", "image", "application", "image", "image", "application", "text", "application", "application", "application", "application", "application", "text", "application", "application", "application", "text", "text", "text", "text", "text", "application", "application", "image", "video", "video", "image", "text", "video", "text", "text", "image", "image", "application", "audio", "text", "image", "image", "video", "video", "audio", "audio", "application", "application", "application", "application", "application", "application", "multipart", "text", "text", "application", "application", "application", "text", "text", "text", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "text", "text", "text", "text", "text", "text", "x-conference", "image", "text", "image", "image", "application", "model", "application", "model", "application", "application", "application", "application", "application", "video", "audio", "application", "i-world", "application", "audio", "text", "text", "text", "text", "application", "image", "image", "image", "image", "image", "image", "image", "image", "image", "image", "application", "application", "application", "text", "text", "image", "audio", "music", "application", "text", "audio", "audio", "audio", "application", "application", "application", "application", "application", "text", "audio", "audio", "text", "application", "text", "text", "text", "application", "application", "application", "application", "application", "application", "text", "text", "video", "audio", "video", "audio", "application", "application", "text", "application", "application", "application", "application", "image", "text", "application", "application", "message", "message", "application", "audio", "audio", "audio", "music", "x-music", "application", "audio", "audio", "audio", "music", "x-music", "application", "application", "message", "www", "audio", "video", "application", "application", "application", "audio", "audio", "video", "video", "video", "audio", "audio", "video", "video", "video", "audio", "audio", "video", "video", "audio", "video", "application", "video", "video", "audio", "video", "audio", "application", "application", "application", "application", "application", "application", "video", "audio", "application", "image", "image", "application", "application", "image", "image", "application", "application", "application", "application", "application", "application", "application", "application", "text", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "text", "image", "application", "application", "image", "image", "chemical", "application", "audio", "audio", "image", "image", "image", "image", "application", "application", "text", "text", "application", "image", "text", "application", "application", "image", "application", "image", "application", "application", "model", "application", "image", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "paleovu", "application", "text", "application", "audio", "x-world", "x-world", "image", "video", "video", "image", "image", "audio", "audio", "audio", "audio", "application", "image", "image", "image", "text", "image", "image", "application", "audio", "audio", "audio", "audio", "audio", "application", "application", "application", "application", "image", "audio", "text", "text", "application", "application", "text", "application", "text", "video", "text", "audio", "application", "application", "application", "text", "text", "video", "text", "application", "application", "application", "application", "application", "application", "text", "text", "text", "text", "application", "application", "application", "text", "application", "application", "text", "text", "audio", "application", "application", "application", "application", "application", "application", "application", "application", "application", "audio", "audio", "application", "application", "text", "application", "application", "application", "application", "text", "application", "application", "application", "application", "application", "application", "application", "application", "application", "image", "image", "application", "x-world", "application", "application", "text", "application", "application", "application", "application", "text", "text", "application", "application", "application", "application", "text", "application", "application", "image", "image", "image", "image", "application", "audio", "application", "audio", "text", "image", "text", "text", "text", "text", "application", "text", "text", "application", "multipart", "application", "text", "text", "application", "text", "application", "video", "application", "video", "video", "video", "video", "application", "application", "audio", "audio", "video", "audio", "audio", "audio", "audio", "application", "model", "x-world", "x-world", "application", "application", "application", "application", "application", "application", "audio", "audio", "application", "image", "application", "application", "application", "windows", "text", "application", "text", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "model", "x-world", "model", "x-world", "text", "application", "application", "image", "image", "image", "video", "xgl", "image", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "application", "audio", "application", "text", "xgl", "application", "image", "image", "image", "video", "image", "image", "chemical", "application", "application", "application", "application", "application", "multipart", "application", "text"};
            int index=Arrays.asList(tipos).indexOf("x-world");
            boolean t=Arrays.asList(tipos).contains("x-world");
            System.out.println(index);
            System.out.println(t);
        }
}
