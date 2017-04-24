import java.io.*;
import java.net.*;
import java.util.*;

class peticionWeb extends Thread
{
    int contador = 0;

    final int ERROR = 0;
    final int WARNING = 1;
    final int DEBUG = 2;

    void depura(String mensaje)
    {
        depura(mensaje,DEBUG);
    }   

    void depura(String mensaje, int gravedad)
    {
        System.out.println(currentThread().toString() + " - " + mensaje);
    }   

    private Socket peticionEntrante     = null;     // representa la petición de nuestro cliente
    private PrintWriter out     = null;     // representa el buffer donde escribimos la respuesta
    String requestType;
    String requestFile;
    peticionWeb(Socket peticion)
    {
        depura("El contador es " + contador);
        
        contador ++;
        
        //Variables que guardan el tipo de peticion y el archivo buscado.
        requestType=null;
        requestFile=null;
        
        
        peticionEntrante = peticion;
        setPriority(NORM_PRIORITY - 1); // hacemos que la prioridad sea baja
    }

    public void run() // Metodo run de la clase thread
    {
        depura("Procesamos conexion");

        try
        {
            BufferedReader in = new BufferedReader (new InputStreamReader(peticionEntrante.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(peticionEntrante.getOutputStream(),"8859_1"),true) ;


            String cadena = "";     // cadena donde almacenamos las lineas que leemos
            int i=0;                // lo usaremos para que cierto codigo solo se ejecute una vez
    
            boolean error = false;
            do{ 
                cadena = in.readLine();
                
                //En la primera linea se encuentra el request type y el archivo, en caso de error se termina inmediatamente.
               if(cadena != null) 
                {
                    StringTokenizer st = new StringTokenizer(cadena);
                    int cantidadElementos = st.countTokens();
                    if(i==0){//tipo de peticion y archivo solicitado

                        if (cantidadElementos >= 2) //debe tener al menos la peticion y el archivo
                        {
                            if(!guardarPeticion(st.nextToken(),st.nextToken())){//Si no existe el archivo o la peticion esta mal estructurada
                                if(requestType==null){
                                    out.println("400 Bad Request");
                                }else{
                                    out.println("404 File Not Found") ;
                                }
                                out.close();
                                error=true;
                            }
                        }else{
                            out.println("400 Bad Request") ;
                            out.close();
                            error=true;
                        }
                    
                    }else if(i==3){//Que solicita como respuesta ACCEPT
                        if (cantidadElementos >= 2) //debe tener al menos la peticion y el archivo
                        {
                            if(st.nextToken().equals("Accept:"))
                            {
                                if(esAceptado(st.nextToken())){
                                    retornaFichero(requestFile);
                                }else{
                                    out.println("Error 406");
                                }
                                out.close();
                            }       
                        }else{
                            out.println("400 Bad Request") ;
                            out.close();
                            error=true;
                        }
                        
                    }

                }
                i++;
                    
            }
            while (cadena != null && cadena.length() != 0 && !error);//mientras no existan errores y exista linea

        }
        catch(Exception e)
        {
            depura("Error en servidor\n" + e.toString());
        }
            
        depura("Hemos terminado");
    }
    
    boolean esAceptado(String tipoArchivo){

        if(!tipoArchivo.equals("*/*")){
            String[] extensionAceptada = tipoArchivo.split("/");//En la posicion 1 tiene la extension
            String[] ruta = requestFile.split("/");//En la posicion 1 tiene la extension
            String archivoSolicitado = ruta[ruta.length-1];
            String[] extensionSolicitada = archivoSolicitado.split("\\.");
            if(extensionAceptada[1].equals(extensionSolicitada[1])){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
    
    boolean guardarPeticion(String request,String file){
        if(request.equals("GET")||request.equals("POST")||request.equals("HEAD")){
            requestType="GET";
            file=existeFichero(file);
            if(file!=null){
                requestFile=file;
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    
    String existeFichero(String nombreFichero)
    {
        depura("Recuperamos el fichero " + nombreFichero);
        
        // comprobamos si tiene una barra al principio
        if (nombreFichero.startsWith("/"))
        {
            nombreFichero = nombreFichero.substring(1) ;
        }
        
        // si acaba en /, le retornamos el index.html de ese directorio
        // si la cadena esta vacia, nos retorna el index.html principal
        if (nombreFichero.endsWith("/") || nombreFichero.equals(""))
        {
            nombreFichero = nombreFichero + "index.html" ;
        }
        
            // Ahora leemos el fichero y lo retornamos
            File archivo= new File(nombreFichero) ;
                
        if (archivo.exists()) 
        {
            return nombreFichero;
        }else{
            return null;
        }
    }

    
    void retornaFichero(String sfichero)
    {
        depura("Recuperamos el fichero " + sfichero);
        
        // comprobamos si tiene una barra al principio
        if (sfichero.startsWith("/"))
        {
            sfichero = sfichero.substring(1) ;
        }
        
        // si acaba en /, le retornamos el index.htm de ese directorio
        // si la cadena esta vacia, no retorna el index.htm principal
        if (sfichero.endsWith("/") || sfichero.equals(""))
        {
            sfichero = sfichero + "index.htm" ;
        }
        
        try
        {
            
            // Ahora leemos el fichero y lo retornamos
            File mifichero = new File(sfichero) ;
                
            if (mifichero.exists()) 
            {
                out.println("HTTP/1.0 200 ok");
                out.println("Server: MiniServer/1.0");
                out.println("Date: " + new Date());
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + mifichero.length());
                out.println("\n");
            
                BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));
                
                
                String linea = "";
                
                do          
                {
                    linea = ficheroLocal.readLine();
    
                    if (linea != null )
                    {
                        // sleep(500);
                        out.println(linea);
                    }
                }
                while (linea != null);
                
                depura("fin envio fichero");
                
                ficheroLocal.close();
                out.close();
                
            }  // fin de si el fiechero existe 
            else
            {
                depura("No encuentro el fichero " + mifichero.toString());  
                out.println("404 Not Found");
                out.close();
            }
            
        }
        catch(Exception e)
        {
            depura("Error al retornar fichero");    
        }

    }
}

