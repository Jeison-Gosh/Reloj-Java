package alarma;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Temporizador implements Runnable {
    
    private static int d,h,m,s;
    private static boolean stop;
    private static String dd,hh,mm,ss,stopWatch;

    public Temporizador(){
        
        Temporizador.stop = false;
        Temporizador.stopWatch = "";
        Temporizador.ss = "";
        Temporizador.mm = "";
        Temporizador.hh = "";
        Temporizador.dd = "";
        Temporizador.d = 0;
        Temporizador.h = 0;
        Temporizador.m = 0;
        Temporizador.s = 0;
    }
    @Override
    
    public void run() {
        while(true){
            if(stop==false){
                Main.aplicacion.timer.setText(chronometer());
            }   
            try {
                 Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Temporizador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private String chronometer(){
        
        ss=parseToString(s);
        mm=parseToString(m);
        hh=parseToString(h);
        dd=parseToString(d);
        if(s==59){
            s=0;
            m++;
            if(m==60){
                m=0;
                h++;
                if(h==24){
                    h=0;
                    d++;
                    if(d==100){
                    }
                }
            }
        }
        stopWatch=(dd+":"+hh+":"+mm+":"+ss);
        s++;
        return stopWatch;
    }
    public static void Stop(){
        Temporizador.stop=true;
    }
    public static void Resume(){
        Temporizador.stop=false;
    }
    public static void Reset(){
        s=0;
        m=0;
        h=0;
        d=0;
        ss="";
        mm="";
        hh="";
        dd="";
        Main.aplicacion.timer.setText("00:00:00:00");
    }
    private String parseToString(int valor){
        String string_valor;
        if(valor<10){
            string_valor="0"+String.valueOf(valor);
        }else{
            string_valor=String.valueOf(valor);
        }
        return string_valor;
    }

}
