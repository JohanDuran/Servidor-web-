/**
 * Universidad de Costa Rica
 * Desarrollo de aplicaciones para internet
 * Tarea programada #1
 * @author (Johan Durán - Kenneth Calvo) 
 * @version (1.0)
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.Timestamp;

class PeticionWeb extends Thread
{
    //Tipos de error
    String error400="Error 400 - Bad Request";
    String error404="Error 404 - Not Found";
    String error406="Error 406 - Not Acceptable";
    //encabezados aceptados
    String []encabezados={"Head:","Host:","User-Agent:","Accept:","Content-Length:","Content-Type:"};
    //variables de debuggeo
    final int ERROR = 0;
    final int WARNING = 1;
    final int DEBUG = 2;
    
    //socket de entrada y Printer de salida de datos
    private Socket peticionEntrante = null;     // representa la petición de nuestro cliente
    private PrintWriter out = null;     // representa el buffer donde escribimos la respuesta
    
    //peticion realizada
    String requestMethod;
    //ruta  y archivo solicitado en caso de ser / se le agrega index
    String requestPath;
    //URL es la recibida de la petición sin agregar nada
    String URL;
    //Parametros de la peticion
    String parametros;
    //PARA POST: tamaño de los parametros
    int contentLength;
    //how made the petition
    String referer;
    //Estampilla de tiempo
    Timestamp timestamp;
    //nombre del servidor
    String serverName="ECCIServer";

   PeticionWeb(Socket peticion)
    { 
        //asignación de prioridad normal
        setPriority(NORM_PRIORITY); 
        timestamp = new Timestamp(System.currentTimeMillis());

        peticionEntrante = peticion;
                
    }

    public void run() //método principal de la clase thread
    {
        try
        {
            BufferedReader in = new BufferedReader (new InputStreamReader(peticionEntrante.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(peticionEntrante.getOutputStream(),"UTF-8"),true) ;


            String cadena = "";     // cadena donde almacenamos las lineas que leemos
            int i=0;                // lo usaremos para que cierto codigo solo se ejecute una vez
    
            boolean error = false;
            do{ 
                cadena = in.readLine();
                Debugger.print(cadena);
                //En la primera linea se encuentra el request type y el archivo, en caso de error se termina inmediatamente.
               if(cadena != null&&!cadena.equals("")) 
                {
                    error=verificarCadena(i,cadena);
                }
                i++;
            }
            while (cadena != null && cadena.length() != 0 && !error);//mientras no existan errores y exista linea
           
            //Para el metodo post el input buffer termina al encontrar \n\r por lo tanto se debe continuar leyendo para recuperar los parametros
            if(requestMethod.equals("POST")&&!error){//no es tomado en cuenta dentro de los errores
                String postParameters="";
                for (int j = 0; j < contentLength; j++) {
                    int c = in.read();
                    postParameters+=(char)c;
               } 
                parametros=postParameters.equals("")?null:postParameters;
            }
            //Si no existen errores se retorna el fichero
            if(!error){
                retornaFichero();
                completaBitacora();
                Debugger.print("Petición completada con exito");
            }else{
                Debugger.print("Errores en el servidor");
            }
            
        }
        catch(Exception e)
        {
            System.out.println("Error en servidor\n" + e.toString());
        }
            

    }
    
    public boolean verificarCadena(int i, String cadena){
        //Se obtienen los toquen de la cadena
        String[] solicitud = cadena.split(" ");
        //deben venir dos elementos key-value
        int cantidadElementos = solicitud.length;
        boolean siError=true;
        boolean noError=false;
        
        //verificamos que sea la cantidad correcta de parámetros
        if(cantidadElementos<2){
            out.println(error400) ;
            out.close();
            return siError;
        }
        
        /*verificamos que sea el encabezado correcto
         * i: indica la sección actual por ej accept, content-type solamente para indexar el vector que contiene los 
         * encabezados
         * Solicitud en la posición 0 contiene el texto asociado a en indice. Se verifica para encabezados en forma
         * independiente y para todos los demás con el vector de encabezados
           */
        i = verificaEncabezado(solicitud[0]);

        //si se llega acá la estructura de la petición es correcta
        switch(i){
            case 0://encabezados
                //parametro 1 es tipo de peticion parametro 2 el archivo
                if(!guardarPeticion(solicitud[0],solicitud[1])){//Si no existe el archivo o la peticion esta mal estructurada
                    if(requestMethod==null||requestPath==null){//Metodo incorrecto
                        out.println(error400);
                        out.close();
                    }
                    return siError;
                }else{
                 return noError;
                }
                
            case 1://indica el host que hizo la solicitud
                referer=solicitud[1];      
                return noError;
            case 3://accept MIME-TYPES
            //se verifica si el contenido no es aceptado
                String[] acc = solicitud[1].split(",");
                if(esAceptado(acc[0])){
                    return noError;//no errores
                }else{
                    out.println(error406);
                    out.close();
                    return siError;//existen
                }
            case 4://lenght POST only
                try{
                    contentLength = Integer.parseInt(solicitud[1]);
                }catch(Exception e){
                    return siError;
                }
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
            //Si es igual a la solicitada retorna true caso contrario false.
            return (extensionAceptada[1].equals(extensionSolicitada[1]))?true:false;
        }else{
            return false;
        }
    }
    
    boolean guardarPeticion(String request,String file){
        requestMethod=request;//GET POST HEAD
        //se obtiene nombre de archivo y parametros
        String[] filePara = file.split("\\?");
        //Si el tamaño no es dos quiere decir que no existen parametros o en un POST
        parametros=filePara.length==2?filePara[1]:null;
        //En la posición 0 de filePara se encuentra el archivo solicitado
        file=existeFichero(filePara[0]);//retorna el nombre del fichero o null si no existe
        //si existe el fichero se guarda toda la direccion en la variable global requestPath
        if(file!=null){
            requestPath=file;
            return true;
        }else{
            return false;
        }
    }
    
    
    String existeFichero(String nombreFichero)
    {
        URL=nombreFichero;
        //para una peticion sin archivo se utilza index.html
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
        String[] ruta = requestPath.split("/");//en la ultima posicion de la ruta esta el archivo que se solicita
        String archivoSolicitado = ruta[ruta.length-1];//se obtiene el archivo
        String[] extensionSolicitada = archivoSolicitado.split("\\.");//se separa por punto para obtener la extension
        try
        {
            
            // Ahora leemos el fichero y lo retornamos
            File mifichero = new File(requestPath) ;
                
            if (mifichero.exists()) 
            {
                long tamano = mifichero.length()+2;
                out.println("HTTP/1.0 200 ok");
                out.println("Server: "+serverName);
                out.println("Date: " + new Date());
                out.println("Content-Type: "+MimeTypes.getMimeType(extensionSolicitada[1]));
                out.println("Content-Length: " + tamano);
                out.println("\n");
                BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));
                String linea = "";
                do          
                {
                    linea = ficheroLocal.readLine();
    
                    if (linea != null )
                    {
                        out.println(linea);
                    }
                }
                while (linea != null);
                out.flush();
                ficheroLocal.close();
                out.close();    
            }  // fin de si el fiechero existe 
            else
            {
                out.println(error404);
                out.close();
            }
            
        }
        catch(Exception e)
        {
            Debugger.print("Error al retornar fichero");    
        }

    }
    
    public int verificaEncabezado(String head){
            if(head.equals("GET")||head.equals("POST")||head.equals("HEAD")){
                return 0;
            }else{
                return Arrays.asList(encabezados).indexOf(head);
            }
    }

    public void completaBitacora(){
        //se escriben los parametros a la bitacora
        Bitacora.escribe(requestMethod, String.valueOf(timestamp.getTime()), serverName, referer, URL, parametros);
    }
}


