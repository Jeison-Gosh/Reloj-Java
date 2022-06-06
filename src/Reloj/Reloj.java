package Reloj;

import java.lang.InterruptedException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Reloj implements Runnable {
    
    private boolean formato; 
    
    public Reloj(boolean formato){
        this.formato=formato;   
    }
    
    @Override
    public void run(){
        String horaActual = "";
        while(true){
            horaActual = getTime();
            if(formato){
               Componentes.clock1.setText(horaActual);
            }else{
               Componentes.clock2.setText(horaActual);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("--[Exepcion ubicada en clase Reloj]\n\n"+ex);
            }
        }
    }
    private String getTime() {
        int h,m,s;
        String hh="", mm="", ss="", pmam=" ", hms;
        Calendar tiempo=new GregorianCalendar();
        h=tiempo.get(Calendar.HOUR_OF_DAY);
        m=tiempo.get(Calendar.MINUTE);
        s=tiempo.get(Calendar.SECOND);
        
        if(m<10) mm="0"+String.valueOf(m);
        else mm=String.valueOf(m);
        
        if(s<10) ss="0"+String.valueOf(s);
        else ss=String.valueOf(s);

        if(formato){
            return hms=h+":"+mm+":"+ss;
        }else{
            if(h>=12) pmam+="pm";
            else pmam+="am";
            
            if(h>12){
                h-=12;
                hh=String.valueOf(h);
            }else if(h==0){
                hh="12";
            }else{
               hh=String.valueOf(h);
            }
            return hms=hh+":"+mm+":"+ss+pmam;
        }
    }

}
