package alarma;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reloj implements Runnable {
    public boolean formato; 
    public Reloj(boolean formato){
        this.formato=formato;   
    }

    @Override
    public void run(){
        while(true){
            String horaActual = getTime();
            if(formato){
                Main.aplicacion.clock1.setText(horaActual);
            }else{
                Main.aplicacion.clock2.setText(horaActual);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String getTime() {
        int h,m,s;
        String hh="", mm="", ss="", pmam=" ", hms="";
        Calendar tiempo=new GregorianCalendar();
        h=tiempo.get(Calendar.HOUR_OF_DAY);
        m=tiempo.get(Calendar.MINUTE);
        s=tiempo.get(Calendar.SECOND);
        if(m<10){
            mm="0"+String.valueOf(m);
        }else{
            mm=String.valueOf(m);
        }
        if(s<10){
            ss="0"+String.valueOf(s);
        }else{
            ss=String.valueOf(s);
        }
        
        if(formato){
            return hms=h+":"+mm+":"+ss;
        }else{
            if(h>=12){
                pmam+="pm";
            }else{
                pmam+="am";
            }
            
            if(h>12){
                h-=12;
                if(h<10){
                    hh="0"+String.valueOf(h);
                }else{
                    hh=String.valueOf(h);
                }
            }else if(h==12){
                hh=String.valueOf(h);
            }else{
                if(h<10){
                     hh="0"+String.valueOf(h);
                }else{
                    hh=String.valueOf(h);
                }
            }
            return hms=hh+":"+mm+":"+ss+pmam;
        }
    }

}
