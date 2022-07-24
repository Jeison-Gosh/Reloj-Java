package Reloj;

public class Main {
    
    public static Componentes aplicacion;
    
    public static void main(String[] args) {
        try{
            aplicacion=new Componentes();
            aplicacion.setVisible(true); 
        }catch(Exception e){
            System.out.println(e);
        }
    }   
}
