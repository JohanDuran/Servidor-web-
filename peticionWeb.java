import java.io.*;
import java.net.*;
import java.util.*;

class PeticionWeb extends Thread
{
    //cantidad de peticiones
    int contador = 0;
    //Tipos de error
    String error400="Error 400 - Bad ";
    String error404="Error 404 - Not Found";
    String error406="Error 406 - Not Acceptable";
    //variables de debuggeo
    final int ERROR = 0;
    final int WARNING = 1;
    final int DEBUG = 2;
    
    //socket de entrada y Printer de salida de datos
    private Socket peticionEntrante = null;     // representa la petición de nuestro cliente
    private PrintWriter out = null;     // representa el buffer donde escribimos la respuesta
    
    //peticion realizada
    String requestMethod;
    //ruta  y archivo solicitado
    String requestPath;
    //Parametros de la peticion
    String parametros;
    //PARA POST: tamaño de los parametros
    int contentLength;
    
    void depura(String mensaje)
    {
        depura(mensaje,DEBUG);
    }   

    void depura(String mensaje, int gravedad)
    {
        System.out.println(currentThread().toString() + " - " + mensaje);
    }   


   PeticionWeb(Socket peticion)
    {
        depura("El contador es " + contador);
        
        contador ++;
        
        //Variables que guardan el tipo de peticion y el archivo buscado.
        requestMethod=null;
        requestPath=null;
        
        
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
                depura("CADENA-----  "+cadena);
                //En la primera linea se encuentra el request type y el archivo, en caso de error se termina inmediatamente.
               if(cadena != null&&!cadena.equals("")) 
                {
                    error=verificarCadena(i,cadena);
                }
                i++;
                    
            }
            while (cadena != null && cadena.length() != 0 && !error);//mientras no existan errores y exista linea
           
            //Para el metodo post el input buffer termina al encontrar \n\r por lo tanto se debe continuar leyendo para recuperar los parametros
            if(requestMethod.equals("POST")){//no es tomado en cuenta dentro de los errores
                String postParameters="";
                for (int j = 0; j < contentLength; j++) {
                    int c = in.read();
                    postParameters+=(char)c;
               } 
                parametros=postParameters.equals("")?null:postParameters;
                }
            //Si no existen errores se retorna el fichero
            retornaFichero();
        }
        catch(Exception e)
        {
            depura("Error en servidor\n" + e.toString());
        }
            
        depura("Hemos terminado");
    }
    
    public boolean verificarCadena(int i, String cadena){
        StringTokenizer st = new StringTokenizer(cadena);
        int cantidadElementos = st.countTokens();
        boolean siError=true;
        boolean noError=false;
        if(cantidadElementos<2){
            depura("cantidad de parametros correcta");
            out.println(error400+cadena) ;
            out.close();
            return true;
        }
                
        switch(i){
            case 0://encabezados
                //parametro 1 es tipo de peticion parametro 2 el archivo
                if(!guardarPeticion(st.nextToken(),st.nextToken())){//Si no existe el archivo o la peticion esta mal estructurada
                    if(requestMethod==null){//Metodo incorrecto
                        out.println(error400);
                    }else{//archivo no existe
                        out.println(error400) ;
                    }
                    out.close();
                    return siError;
                }else{
                 return noError;
                }
            case 3://accept
                if(st.nextToken().equals("Accept:"))
                {
                    if(esAceptado(st.nextToken())){
                        return noError;//no errores

                    }else{
                        out.println(error406);
                        out.close();
                        return siError;//existen
                    }
                }else{
                    return siError;
                }       
            case 4://lenght POST only
                String[] textoValor=cadena.split(" ");
                int valor = textoValor.length==2?Integer.parseInt(textoValor[1]):0;
                contentLength = valor;
                return noError;
            default://defecto no error para seguir leyendo
                return noError;
        }
    
    }
    
    boolean esAceptado(String tipoArchivo){
        //primero verificamos que sean del mismo tipo
        String[] extensionAceptada = tipoArchivo.split("/");//En la posicion 1 tiene la extension
        String[] ruta = requestPath.split("/");//en la ultima posicion de la ruta esta el archivo que se solicita
        String archivoSolicitado = ruta[ruta.length-1];//se obtiene el archivo
        String[] extensionSolicitada = archivoSolicitado.split("\\.");//se separa por punto para obtener la extension

        if(MimeTypes.exist(extensionAceptada[0],extensionAceptada[1])){//si la extension Aceptada es valida
            //comparo la extension del archivio que me solicitan con la que debo retornar
            //Si la extension aceptada es un * se le asigna el valor de la solicitada para compararlas.
            extensionAceptada[1]=extensionAceptada[1].equals("*")?extensionSolicitada[1]:extensionAceptada[1];
            return (extensionAceptada[1].equals(extensionSolicitada[1]))?true:false;
        }else{
            return false;
        }
    }
    
    boolean guardarPeticion(String request,String file){
        if(request.equals("GET")||request.equals("POST")||request.equals("HEAD")){
            requestMethod=request;
            //se obtiene nombre de archivo y parametros
            String[] filePara = file.split("\\?");
            parametros=filePara.length==2?filePara[1]:null;
            file=existeFichero(filePara[0]);//retorna el nombre del fichero o null si no existe
            //si existe el fichero se guarda toda la direccion en la variable global requestPath
            if(file!=null){
                requestPath=file;
                return true;
            }else{
                return false;
            }
        }else{//Peticion invalida
            return false;
        }
    }
    
    
    String existeFichero(String nombreFichero)
    {
        //para una peticion sin archivo se utilza index.html
        
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

    
    void retornaFichero()
    {
        depura("Recuperamos el fichero " + requestPath);
        String[] ruta = requestPath.split("/");//en la ultima posicion de la ruta esta el archivo que se solicita
        String archivoSolicitado = ruta[ruta.length-1];//se obtiene el archivo
        String[] extensionSolicitada = archivoSolicitado.split("\\.");//se separa por punto para obtener la extension
        try
        {
            
            // Ahora leemos el fichero y lo retornamos
            File mifichero = new File(requestPath) ;
                
            if (mifichero.exists()) 
            {
                out.println("HTTP/1.0 200 ok");
                out.println("Server: MiniServer/1.0");
                out.println("Date: " + new Date());
                out.println("Content-Type: "+MimeTypes.getMimeType(extensionSolicitada[1]));
                out.println("Content-Length: " + mifichero.length());
                out.println("\n");
                if(requestMethod.equals("HEAD")){
                    out.close();
                }else{   
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
                }   
            }  // fin de si el fiechero existe 
            else
            {
                depura("No encuentro el fichero " + mifichero.toString());  
                out.println(error404+"FLAG");
                out.close();
            }
            
        }
        catch(Exception e)
        {
            depura("Error al retornar fichero");    
        }

    }
}

