
/**
 * Write a description of class controller here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.*;
import java.util.*;
public class controller
{

    public static void main(){
        System.out.println("Iniciamos el servidor");
        ServidorWeb server = new ServidorWeb();
        server.arranca();
    }
}
