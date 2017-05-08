/**
 * Universidad de Costa Rica
 * Desarrollo de aplicaciones para internet
 * Tarea programada #1
 * @author (Johan Durán - Kenneth Calvo) 
 * @version (1.0)
 */
import javax.swing.JOptionPane; 
public class Debbuger
{
    //imprime un texto en consola de tipo String
    public static void print(String text){
        System.out.println(text);
    }
    //imprime un texto en consola de tipo String con bandera
    public static void print(String text, String flag){
        System.out.println(flag+" > "+text);
    }
    
    //muesta dialogo de ventana
    public static void messageDialog(String text){
     JOptionPane.showMessageDialog(null,text);
    }
    
    //muestra ventana de entrada de datos
    public static String inputDialog(String text){
     return JOptionPane.showInputDialog(null,text);
    }

    //muestra ventana de confirmación. yes no cancel
    public static int confirmDialog(String text){
        return JOptionPane.showConfirmDialog(null,text);
    }
}
