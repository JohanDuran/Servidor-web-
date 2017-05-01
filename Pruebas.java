
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
    
    public static void imprimir(String text){
        System.out.println(text);
    }
    
    public static void main(String args){
        String texto="/index.html";
        String[] filePara = texto.split("\\?");
        if(filePara!=null){
            imprimir("filepara "+filePara[0]);
            imprimir("tamao "+filePara.length);
            String parametros=filePara.length==2?filePara[1]:null;
            imprimir("parametros");
        }

    }
}
